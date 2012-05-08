package cnt.demo;

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

/**
* Handles portforwarding for gameclients - DEMO
* 
* @author Calle Lejdbrandt, <a href="callel@kth.se">callel@kth.se</a>
*/
public class UPnPDemo implements Runnable 
{
	/**
	* Main method, starts a new thread
	*
	* @param args arguments for main method
	*/
	public static void main(final String... args) throws Exception 
	{
		// Set up multi-threading
		final Thread clientThread = new Thread(new UPnPDemo());
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
			UpnpService upnpService = new UpnpServiceImpl()
			{
				public void finalize() 
				{
					this.shutdown();
				}
			};
		
			// Add our custom made listener (defined below)
			System.out.println("Adding listener");
			upnpService.getRegistry().addListener( createIgdListener(upnpService) );

			// Start the search for devices that has the type of service we are looking for
			System.out.println("Starting search of devices");
			upnpService.getControlPoint().search( new STAllHeader() );
			
			try
			{
				Thread.sleep((5000));
			} catch (Exception e) {
			}
			
			upnpService.shutdown();
			
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
			ServiceType _service = new UDAServiceType("WANIPConnection");
			
			Service service = null;

			@Override
			public void remoteDeviceAdded(Registry registry, RemoteDevice device)
			{
				Service portMap = device.findService(_service);
				this.service = portMap;
				if (portMap != null)
				{
					System.out.println("Correct service discovered: " + device.getRoot().getDetails().getFriendlyName());
					// Execute the action that makes the portmapping happend. executeAction defined below
					executeAction(upnpService, portMap);

				} else 
				{
					registry.removeDevice(device);
				}
			}

			@Override
			public void remoteDeviceRemoved(Registry registry, RemoteDevice device)
			{
				Service portMap;
				if ((portMap = device.findService(_service)) != null)
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
			synchronized void executeAction(UpnpService upnpService, Service portMapService)
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
							
 							System.out.println("Successfully made a portforward: " + portMapping);
							try
							{
								Thread.sleep((1200));
							} catch (Exception e) {
							}
						}
						
						@Override
						public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg)
						{
							System.err.println("Something went wrong doing portforward");
							System.err.println(defaultMsg);
						}
					}
				);
				
				updatePortMappings(portMapService);
				try
				{
					Thread.sleep((2200));
				} catch (Exception e) {
				}
				
				// Execute de-mapping on router
                                upnpService.getControlPoint().execute
                                (
                                        new PortMappingDelete(portMapService, cntPort)
                                        {

                                                @Override
                                                public void success(ActionInvocation invocation)
                                                {

                                                        System.out.println("Successfully removed forwarded port: " + portMapping);
							try
							{
								Thread.sleep((1200));
							} catch (Exception e) {
							}
                                                }

                                                @Override
                                                public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg)
                                                {
                                                        System.err.println("Something went wrong removeing portforward");
                                                        System.err.println(defaultMsg);
                                                }
                                        }
                                );
				
				updatePortMappings(portMapService);

			} // end executeAction

			/**
			* Get all Portforwards. Code mostly copy/pasted from Wokrbench source
			*
			*/
	
			protected void updatePortMappings(final Service service) 
			{
			       // Don't block the EDT
				upnpService.getConfiguration().getAsyncProtocolExecutor().execute(new Runnable() 
				{
					public void run()
					{
		
		                		for (int i = 0; i < 65535; i++) 
						{ 
							// You can't have more than 65535 port mappings
		                    			// Synchronous execution! And we stop when we hit a 713 response code because there
	       		             			// is no other way to retrieve all mappings.
							GetGenericPortMappingCallback invocation = new GetGenericPortMappingCallback(i, upnpService, service);
	               		     			invocation.run();
		
	        	            			if (invocation.isStopRetrieval()) break;
		
	        	            			if (invocation.getMapping() != null) 
							{
	                        				System.out.println(invocation.getMapping());
	                    				}
	                			}
	            			}
	        		});
	    		}
	
	    		class GetGenericPortMappingCallback extends ActionCallback 
			{
	
	        		int index;
	        		PortMapping mapping;
	        		boolean stopRetrieval = false;
	
	        		GetGenericPortMappingCallback(int index, UpnpService upnpService, final Service service) 
				{
	           			super(new ActionInvocation(service.getAction("GetGenericPortMappingEntry")), upnpService.getControlPoint());
	            			this.index = index;
	            			getActionInvocation().setInput("NewPortMappingIndex", new UnsignedIntegerTwoBytes(index));
	        		}
	
	        		public PortMapping getMapping() 
				{
	            			return mapping;
	        		}
	
	        		@Override
	        		public void success(ActionInvocation invocation) 
				{
	            			mapping = new PortMapping(invocation.getOutputMap());
	        		}
	
	        		@Override
				public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) 
				{
	
	            			stopRetrieval = true;
	
	            			if (invocation.getFailure().getErrorCode() == 713) 
					{
	                			// This is the _only_ way how we can know that we have retrieved an almost-up-to-date
	                			// list of all port mappings! Yes, the designer of this API was and probably still is
	                			// a moron.
	
	            			} else 
					{
	             				System.out.println("Error occured getting Portforward Tabel");
	            			}
	        		}
	
	        		public boolean isStopRetrieval() 
				{
	            			return stopRetrieval;
	        		}
    			}
	
		}; // end custom listener
	} // end listener invocation
} // end class		
