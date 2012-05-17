/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
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
 * Handles portforwarding for game clients demo
 * 
 * @author Calle Lejdbrandt, <a href="mailto:callel@kth.se">callel@kth.se</a>
 */
@SuppressWarnings({"unchecked", "rawtypes"}) //TODO: Can you fix these warning
public class UPnPDemo implements Runnable
{
    /**
     * The port to use for UPnP
     */
    static int PORT = 45000;
    
    /**
     * The local machines IP address
     */
    static String IP = "192.168.0.44";
    
    
    
    /**
     * Constructor
     */
    private UPnPDemo()
    {
	//Privatise default constructor
    }
    
    
    
    /**
     * This is the main entry point of the demoMain method, starts a new thread
     *
     * @param  args  Optional startup arguments, IP address (see {@link #IP}) and optionally port (see {@link #PORT})
     */
    public static void main(final String... args) throws Exception 
    {
	if (args.length > 0)
	    IP = args[0];
	if (args.length > 1)
	    PORT = Integer.parseInt(args[1]);
	
	// Set up multi-threading
	final Thread clientThread = new Thread(new UPnPDemo());
	
	clientThread.setDaemon(false);
	clientThread.start();
    }
    
    
    
    /**
     * Run method to start executing in the thread
     */
    public void run()
    {
	try
	{
	    // Set up the default Upnp Stack Service (implemented by Cling)
	    System.out.println("Createing new Service");
	    UpnpService upnpService = new UpnpServiceImpl()
		    {
			/**
			 * {@inheritDoc}
			 */
		 	public void finalize() 
			{
			    this.shutdown();
			}
		    };
		
	    // Add our custom made listener (defined below)
	    System.out.println("Adding listener");
	    upnpService.getRegistry().addListener(createIgdListener(upnpService));

	    // Start the search for devices that has the type of service we are looking for
	    System.out.println("Starting search of devices");
	    upnpService.getControlPoint().search(new STAllHeader());
			
	    try {
		Thread.sleep(5000);
	    }
	    catch (final InterruptedException err) {
		//Ignore
	    }
			
	    upnpService.shutdown();
	}
	catch (final Exception err)
	{
	    // Print error message and exit
	    System.err.println("An Excpetion occured: " + err.toString());
	    System.exit(-1);
	}
    }
    
    /**
     * Custom made UPnP listener
     * 
     * @param   upnpService  The Upnp Stack service used
     * @return               The listener used to listen for UPnP devies on the network
     */
    private RegistryListener createIgdListener(final UpnpService upnpService)
    {
	// Create and return a new listener
	return new DefaultRegistryListener()
	        {
		    /**
		     * The service we want to work with
		     */
		    ServiceType _service = new UDAServiceType("WANIPConnection");
		    
		    /**
		     * TODO: What is this for?
		     */
		    Service service = null;
		    
		    
		    
		    /**
		     * {@inheritDoc}
		     */
		    @Override
		    public void remoteDeviceAdded(final Registry registry, final RemoteDevice device)
		    {
			Service portMap = device.findService(_service);
			this.service = portMap;
			
			if (portMap != null)
			{
			    System.out.println("Correct service discovered: " + device.getRoot().getDetails().getFriendlyName());
			    // Execute the action that makes the portmapping happend. executeAction defined below
			    executeAction(upnpService, portMap);
			}
			else
			    registry.removeDevice(device);
		    }
		    
		    /**
		     * {@inheritDoc}
		     */
		    @Override
		    public void remoteDeviceRemoved(final Registry registry, final RemoteDevice device)
		    {
			Service portMap;
			if ((portMap = device.findService(_service)) != null)
			    System.out.println("Serivice is disappering: " + portMap);
		    }
		    
		    /**
		     * Method to define and set a portmapping when a IGD is found
		     *
		     * @param  upnpService  The UPnP Service stack used
		     * @param  portMap      The service we want to use to set our portmapping
		     */
		    synchronized void executeAction(final UpnpService upnpService, final Service portMapService)
		    {
			// Set up portmap
			PortMapping cntPort = new PortMapping(UPnPDemo.PORT, UPnPDemo.IP, PortMapping.Protocol.TCP, "CNT testport");
			
			// Execute mapping on router
			upnpService.getControlPoint().execute(new PortMappingAdd(portMapService, cntPort)
			        {
				    /**
				     * {@inheritDoc}
				     */
				    @Override
				    public void success(final ActionInvocation invocation)
				    {
					System.out.println("Successfully made a portforward: " + portMapping);
					try {  Thread.sleep(1200);  }  catch (final InterruptedException err) { /* Ignore */ }
				    }
			    
				    /**
				     * {@inheritDoc}
				     */
				    @Override
				    public void failure(final ActionInvocation invocation, final UpnpResponse operation, final String defaultMsg)
				    {
					System.err.println("Something went wrong doing portforward");
					System.err.println(defaultMsg);
				    }
			        });
			
			updatePortMappings(portMapService);
			try {
			    Thread.sleep(2200);
			}
			catch (final InterruptedException err) {
			    //Ignore
			}
			
			// Execute de-mapping on router
			upnpService.getControlPoint().execute(new PortMappingDelete(portMapService, cntPort)
			        {
				    /**
				     * {@inheritDoc}
				     */
				    @Override
				    public void success(ActionInvocation invocation)
				    {
					System.out.println("Successfully removed forwarded port: " + portMapping);
					try {  Thread.sleep(1200);  }  catch (final InterruptedException err) { /* Ignore */ }
				    }
				    
				    /**
				     * {@inheritDoc}
				     */
				    @Override
				    public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg)
				    {
					System.err.println("Something went wrong removeing portforward");
					System.err.println(defaultMsg);
				    }
			        });
			
			updatePortMappings(portMapService);
			
		    } // end of executeAction()
		    
		    /**
		     * Get all portforwards. Code mostly copied from Workbench source.
		     * 
		     * @param  service  A {@link Service}
		     */
		    protected void updatePortMappings(final Service service)
		    {
			// Don't block the EDT
			upnpService.getConfiguration().getAsyncProtocolExecutor().execute(new Runnable()
			        {
				    /**
				     * {@inheritDoc}
				     */
				    public void run()
				    {
					for (int i = 0; i < 65535; i++) 
					{ 
					    // You can't have more than 65535 port mappings
					    // Synchronous execution! And we stop when we hit a 713 response code because there
					    // is no other way to retrieve all mappings.
					
					    GetGenericPortMappingCallback invocation = new GetGenericPortMappingCallback(i, upnpService, service);
					    invocation.run();
					
					    if (invocation.isStopRetrieval())
						break;
					
					    if (invocation.getMapping() != null) 
						System.out.println(invocation.getMapping());
					}
				    }
			        });
		    }
		    
		    /**
		     * Port mapping callback class
		     */
		    class GetGenericPortMappingCallback extends ActionCallback 
		    {
			/**
			 * Constructor
			 * 
			 * @param  index        TODO: What is this for?
			 * @param  upnpService  TODO: What is this for?
			 * @param  service      TODO: What is this for?
			 */
			public GetGenericPortMappingCallback(int index, UpnpService upnpService, final Service service) 
			{
			    super(new ActionInvocation(service.getAction("GetGenericPortMappingEntry")), upnpService.getControlPoint());
			    
			    this.index = index;
			    getActionInvocation().setInput("NewPortMappingIndex", new UnsignedIntegerTwoBytes(index));
			}
			
			
			
			/**
			 * TODO: What is this for, and is it even used?
			 */
			int index;
			
			/**
			 * Port mapping, see {@link PortMapping}
			 */
			PortMapping mapping;
			
			/**
			 * See {@link #isStopRetrieval()}
			 */
			boolean stopRetrieval = false;
			
			
			
			/**
			 * {@inheritDoc}
			 */
			public PortMapping getMapping() 
			{
			    return mapping;
			}
			
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void success(final ActionInvocation invocation) 
			{
			    this.mapping = new PortMapping(invocation.getOutputMap());
			}
			
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void failure(final ActionInvocation invocation, final UpnpResponse operation, final String defaultMsg) 
			{
			    this.stopRetrieval = true;
			    
			    if (invocation.getFailure().getErrorCode() == 713) 
			    {
				// This is the _only_ way how we can know that we have retrieved an almost-up-to-date
				// list of all port mappings! Yes, the designer of this API was and probably still is
				// a moron.
			    }
			    else 
				System.out.println("Error occured getting Portforward Tabel");
			}
			
			/**
			 * {@inheritDoc}
			 */
			public boolean isStopRetrieval() 
			{
			    return this.stopRetrieval;
			}
		    }
		    
	    }; // end of `return new DefaultRegistryListener()`
    } // end of createIgdListener()
}
