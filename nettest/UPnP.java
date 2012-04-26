package nettest;

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

/**
* Handles portforwarding for gameclients - testclass
* 
* @author Calle Lejdbrandt, <a href="callel@kth.se">callel@kth.se</a>
*/
public class UPnP implements Runnable 
{
	/**
	* Main method, starts a new thread
	*
	* @param args arguments for main method
	*/
	public static void main(final String... args) throws Exception 
	{
		// Set up multi-threading
		final Thread clientThread = new Thread(new UPnP());
		clientThread.setDaemon(false);
		clientThread.start();
	}
	
	/**
	* Run method to start executin in thread
	*/
	public void run()
	{
		try
		{
			// Set up the default Upnp Stack Service (implemented by Cling)
			System.out.println("Createing new Service");
			UpnpService upnpService = new UpnpServiceImpl();
		
			// Add our custom made listener (defined below)
			System.out.println("Adding listener");
			upnpService.getRegistry().addListener( createIgdListener(upnpService) );

			// Define the service we are looking for on the network (InternetGatewayDevices with WANIPConnection services)
			UDAServiceType igdService = new UDAServiceType("WANIPConnection");
			System.out.println("Setting service to look for: " + igdService);

			// Start the search for devices that has the type of service we are looking for
			System.out.println("Starting search of devices");
			upnpService.getControlPoint().search( new UDAServiceTypeHeader(igdService) );

			//sleeping 30 sec, if no devices are present in registry, exit
			System.out.println("Sleeping 30 sec to let devices aknowledge themselves");
			Thread.sleep(30000);
			
			if (upnpService.getRegistry().getDevices().size() == 0) 
			{
				System.out.println("No devices discovered in 30 sec, exiting");
				upnpService.shutdown();
			} else 
			{
				System.out.println("Devices discovered");
			}
			
		} catch (Exception e)
		{
			// Print error message and exit
			System.err.println("An Excpetion occured: " + e);
			System.exit(1);
		}
	}
	
	/**
	* Custom made UPnP listener
	*
	* @param upnpService The Upnp Stack service used
	* @return RegistryListener The listener used to listen for UPnP devies on the network
	*/
	private RegistryListener createIgdListener(final UpnpService upnpService)
	{
		// Create and return a new listener
		return new DefaultRegistryListener() {
			// The service we want to work with
			ServiceId _WICService = new UDAServiceId("WANIPConnection");

			@Override
			public void remoteDeviceAdded(Registry registry, RemoteDevice device)
			{
				Service portMap;
				if ((portMap = device.findService(_WICService)) != null)
				{
					System.out.println("Correct service discovered: " + portMap);
					// Execute the action that makes the portmapping happend. executeAction defined below
					executeAction(upnpService, portMap);
				}
			}

			@Override
			public void remoteDeviceRemoved(Registry registry, RemoteDevice device)
			{
				Service portMap;
				if ((portMap = device.findService(_WICService)) != null)
				{
					System.out.println("Serivice is disappering: " + portMap);
				}
			}
			
			/**
			* Method to define and set a portmapping when a IGD is found
			*
			* @param upnpService The UPnP Service stack used
			* @param portMap The service we want to use to set our portmapping
			*/
			void executeAction(UpnpService upnpService, Service portMapService)
			{
				// Set up portmap
				PortMapping cntPort = new PortMapping(45000, "192.168.0.44", PortMapping.Protocol.TCP, "CNT testport");
				
				// Execute mapping on router
				upnpService.getControlPoint().execute
				(
					new PortMappingAdd(portMapService, cntPort)
					{
						
						@Override
						public void success(ActionInvocation invocation)
						{
							
 							System.out.println("Successfully made a portforward: " + invocation.getOutput());
						}
						
						@Override
						public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg)
						{
							System.err.println("Something went wrong doing portforward");
							System.err.println(defaultMsg);
						}
					}
				);
				
				System.out.println("Sleeping 10 seconds before removeing portmap");
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
				}
				
				// Execute de-mapping on router
                                upnpService.getControlPoint().execute
                                (
                                        new PortMappingDelete(portMapService, cntPort)
                                        {

                                                @Override
                                                public void success(ActionInvocation invocation)
                                                {

                                                        System.out.println("Successfully removed forwarded port: " + invocation.getOutput());
                                                }

                                                @Override
                                                public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg)
                                                {
                                                        System.err.println("Something went wrong removeing portforward");
                                                        System.err.println(defaultMsg);
                                                }
                                        }
                                );
			} // end executeAction
		}; // end custom listener
	} // end listener invocation
} // end class		
