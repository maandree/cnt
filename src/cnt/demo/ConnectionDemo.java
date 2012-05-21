/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.demo;
import cnt.game.*;
import cnt.network.*;
import cnt.mock.ConnectionNetworking;
import cnt.messages.*;
import cnt.*;

import java.awt.*;
import java.util.*;
import java.io.*;
import java.net.*;


/**
 * Peer networking demo class
 *
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class ConnectionDemo
{
    /**
     * Non-constructor
     */
    private ConnectionDemo()
    {
	assert false : "You may not create instances of this class [ConnectionDemo].";
    }
    
    
    
    /**
     * This is the main entry point of the demo
     * 
     * @param  args  Startup arguments, {name, may be server ? s : c, local public IP address, servers' port, remote peer's IP address}
     * 
     * @throws  Exception  On total failure
     */
    public static void main(final String... args) throws Exception
    {
	final char name = args[0].charAt(0);
	final boolean serverauth = args[1].charAt(0) == 's';
	final String pubip = args[2];
	final int serverport = Integer.parseInt(args[3]);
	final String remote = args[4];
	
	
	final ConnectionNetworking connectionNetworking = new ConnectionNetworking(name, serverauth, pubip, serverport, remote);
	System.err.println("ConnectionNetworking created");
	
	
	final Thread readThread = new Thread()
	        {
		    /**
		     * {@inheritDoc}
		     */
		    @Override
		    public void run()
		    {
			try
			{
			    for (int d; (d = connectionNetworking.globalIn.read()) != -1;)
			    {
				System.out.write(d);
				if (d == 10)
				    System.out.flush();
			    }
			}
			catch (final Throwable err)
			{
			    err.printStackTrace(System.err);
			}
		    }
	        };
	readThread.start();
	System.err.println("Thread created");
	
	
	for (int d; (d = System.in.read()) != -1;)
	{
	    connectionNetworking.globalOut.write(d);
	    if (d == 10)
		connectionNetworking.globalOut.flush();
	}
    }
    
}

