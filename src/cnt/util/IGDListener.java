/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.util;

// Packages needed for UPnP
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

//Blackboard class for communication
import cnt.Blackboard;
import cnt.messages.*;
//Network stuff
import java.net.*;

/**
* Listener class used to find Internet Gateway Devices on the network.
*
* @author Calle Lejdbrandt, <a href="mailto:callel@kth.se">callel@kth.se</a>
*/
public class IGDListener extends DefaultRegistryListener
{
	/**
	* Constructor with monitor object for synchronisation and port to portmap
	*
	* @param upnpService the UpnpService bein used by ConnectionNetworking instance
	* @param monitor the monitor object being used for synchronisation
	* @param port the port to forward in the IGD
	*/
	public IGDListener(final UpnpService upnpService, final Object monitor, final int port)
	{
		//Call superclass constructor, might not be needed. Don't know what it does explicitly.
		super();
		
		this.upnpService = upnpService;
		
		this.monitor = monitor;

		this.port = port;
	}
	
	/**
	* The service we are looking for in the UPnP Devices that are possibly on the network.
	*/
	final ServiceType serviceType = new UDAServiceType("WANIPConnection");
	/**
	* The UpnpService instance passed from ConnectionNetworking.
	*/
	final UpnpService upnpService;
	
	/**
	* The monitor object used for synchronisation
	*/
	final Object monitor;
	
	/**
	* The port to use
	*/
	final int port;

	/**
	* {@inheritDoc}
	*/
	@Override
	public void remoteDeviceAdded(final Registry registry, final RemoteDevice device)
	{
		RemoteService portMap = device.findService(serviceType);

		if (portMap != null)
		{
			Blackboard.broadcastMessage(new SystemMessage(null, "UPnP Device discovered"));
			
			// Make the portmap
			this.mapPort(portMap, registry, device);
			
		} else
		{
			// If the service wasn't discovered on the device. We remove the device
			registry.removeDevice(device);
		}
	}
	
	/**
	* {@inheritDoc}
	*/
	@Override
	public void remoteDeviceRemoved(final Registry registry, final RemoteDevice device)
	{
		RemoteService portMap;
		if ((portMap = device.findService(serviceType)) != null)
			Blackboard.broadcastMessage(new SystemMessage(null, "Internet Gateway is leaving network. This might bring connection errors."));
	}

	/**
	* Sets the portmapping on a IGD when it is found on the network
	*
	* @param upnpService the upnpSerivce given from ConnectionNetworking
	* @param portMap the service being used for portmapping
	*/
	synchronized public void mapPort(final RemoteService portMapService, final Registry registry, final RemoteDevice device)
	{
		// Set up the portmappin
		PortMapping cntPort = null;
		try {
			cntPort = new PortMapping(this.port, InetAddress.getLocalHost().getHostAddress(), PortMapping.Protocol.TCP, "CNT");
		} catch (UnknownHostException err) 
		{
			// Very unlikely that we can't find the localhost. Ignore for now. TODO: fix proper error handeling
		}

		// Execute the mapping on the IGD
		this.upnpService.getControlPoint().execute(new PortMappingAdd(portMapService, cntPort)
			{
				/**
				* {@inheritDoc}
				*/
				@Override
				public void success(final ActionInvocation invocation)
				{
				        synchronized (IGDListener.this.monitor)
					{
					    IGDListener.this.monitor.notifyAll();
					}
				}
				
				/**
				* {@ineheritDoc}
				*/
				@Override
				public void failure(final ActionInvocation invocation, final UpnpResponse operation, final String defaultMsg)
				{
					registry.removeDevice(device);
				}
			}
		);
	}
	/**
	* Removes the portmapping on a IGD
	*
	* @param upnpService the upnpSerivce given from ConnectionNetworking
	* @param portMap the service being used for portmapping
	*/
	synchronized public void demapPort(final RemoteService portMapService, final Registry registry, final RemoteDevice device)
	{
		// Set up the portmappin
		PortMapping cntPort = null;
		try {
			cntPort = new PortMapping(this.port, InetAddress.getLocalHost().getHostAddress(), PortMapping.Protocol.TCP, "CNT");
		} catch (UnknownHostException err)
		{
			// Very unlikely. TODO: setup proper error handling
		}
		// Execute the mapping on the IGD
		this.upnpService.getControlPoint().execute(new PortMappingDelete(portMapService, cntPort)
			{
				/**
				* {@inheritDoc}
				*/
				@Override
				public void success(final ActionInvocation invocation)
				{
				    synchronized (IGDListener.this.monitor)
				    {
					IGDListener.this.monitor.notifyAll();
				    }
				}
				/**
				* {@inheritDoc}
				*/
				@Override
				public void failure(final ActionInvocation invocation, final UpnpResponse operation, final String defaultMsg)
				{
					// {ignore}
				}
				
			}
		);
	}
}
