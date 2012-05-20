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
public class PeerNetworkingDemo
{
    /**
     * Non-constructor
     */
    private PeerNetworkingDemo()
    {
	assert false : "You may not create instances of this class [PeerNetworkingDemo].";
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
	System.out.println("ConnectionNetworking created");
	
	final ObjectNetworking     objectNetworking     = new ObjectNetworking(connectionNetworking.globalIn, connectionNetworking.globalOut);
	System.out.println("ObjectNetworking created");
	
	final GameNetworking       gameNetworking       = new GameNetworking(objectNetworking);
	System.out.println("GameNetworking created");
	
	final BlackboardNetworking blackboardNetworking = new BlackboardNetworking(gameNetworking)
	        {
		    /**
		     * {@inheritDoc}
		     */
		    @Override
		    protected void broadcastMessage(final Blackboard.BlackboardMessage message) throws IOException, ClassNotFoundException
		    {
			System.out.println("received blackboard message: " + message.getClass().toString());
		    }
	      };
	System.out.println("BlackboardNetworking created");
	
	
	Blackboard.broadcastMessage(new Blackboard.LocalPlayer(new Player(args[0], args[0].hashCode() | (255 << 24))));
	System.out.println("Local player set");
	
	
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
			    for (;;)
				blackboardNetworking.receiveAndBroadcast();
			}
			catch (final Throwable err)
			{
			    err.printStackTrace(System.err);
			}
		    }
	        };
	readThread.start();
	System.out.println("Thread created");
	
	
	final Scanner sc = new Scanner(System.in);
	while (sc.hasNext())
	{
	    final String line = sc.nextLine();
	    System.out.println("Sending: " + line);
	    Blackboard.broadcastMessage(new Blackboard.UserMessage(line));
	}
    }
    
}

