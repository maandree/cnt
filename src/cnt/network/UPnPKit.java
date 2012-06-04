/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.network;
import cnt.Blackboard;
import cnt.messages.*;
import cnt.game.Player;

// Classes needed for UPnP
import org.teleal.cling.*;
import org.teleal.cling.model.message.header.*;
import org.teleal.cling.model.message.*;
import org.teleal.cling.model.meta.*;
import org.teleal.cling.model.types.*;
import org.teleal.cling.model.action.*;
import org.teleal.cling.registry.*;
import org.teleal.cling.support.igd.*;
import org.teleal.cling.support.igd.callback.*;
import org.teleal.cling.support.model.*;
import org.teleal.cling.controlpoint.*;
import cnt.util.IGDListener;

import java.util.*;
import java.io.*;
import java.net.*;


/**
* @author Calle Lejdbrandt, <a href="mailto:callel@kth.se">callel@kth.se</a>
*/
class UPnPKit
{

    UpnpService upnpService = null;


	/**
	* Tries to find any UPnP enabled device that is a Internet Gateway Device.
	* If found a portforward, try to make a portforward.
	*
	* @param port the port to be forwarded
	*
	* @return returns <code>true</code> on succes, <code>false</code> other whise.
	*/
	public boolean createPortForward(int port)
	{
		// NOTE: This UPNP implementation is multithreaded. 
		// Any Device is found asynchronously.

		// monitor object to be used for the search for a device
	        final Object monitor = new Object();

		this.upnpService = new UpnpServiceImpl();

		//The IGDListener class is custom made and resides in util
		this.upnpService.getRegistry().addListener(new IGDListener(upnpService, monitor, port));
		// Set the service we want to search for (makes MUCH less network congestion if network has many UPnP devices)
		ServiceType _type = new UDAServiceType("WANIPConnection");

		//Initiate a standard search
		//this.upnpService.getControlPoint().search(new ServiceTypeHeader(_type));
		// Testing default serach
		this.upnpService.getControlPoint().search(new STAllHeader());
		try
		{
			// Devices are discovered asynchronously, but should be faster then 5 sec.
		        synchronized (monitor)
			{
			    monitor.wait(5000);
			}
		} catch (InterruptedException ie)
		{
			// {ignore and continue}
		}
		
		// If we found the correct device, we have also made a PortForardd. 
		// If no PortForward could be done, device is removed before this check. 
		// Probably... depending on timeout and asymchronous behaviour.
		if (this.upnpService.getRegistry().getDevices(_type).size() > 0)
		{
			Blackboard.broadcastMessage(new SystemMessage(null, "We have " + this.upnpService.getRegistry().getDevices(_type).size() + " IGD(s) "));
			return true;
		} else
		{
			Blackboard.broadcastMessage(new SystemMessage(null, "We have 0 IGDs"));
			return false;
		}
	}
	
	/**
	* Removes the portforwaring on any UPnP device that was discovered during startup connecting
	*
	*/
	public void removePortForward()
	{
		// Monitor object - see createPortForward
		final Object monitor = new Object();

		// RemoteService we are interested in - see createPortForward
		ServiceType _type = new UDAServiceType("WANIPConnection");
		
		// Se if we actually have a UpnpService and devices
		if (this.upnpService != null && this.upnpService.getRegistry().getDevices(_type).size() > 0)
		{
			// For all IGDs, remove portmappings
		    for (RemoteDevice device : (RemoteDevice[])(this.upnpService.getRegistry().getDevices(_type).toArray()))
			{
				RemoteService portMap = device.findService(_type);
				// This retrives the IGDListener instances and executes demapPort()
				// the iterator is needed as it's unknown what kind of collection is retrived from Cling
				
				IGDListener _listener = (IGDListener)this.upnpService.getRegistry().getListeners().iterator().next();
				_listener.demapPort(portMap, this.upnpService.getRegistry(), device);
			}
		}
	}	
}
