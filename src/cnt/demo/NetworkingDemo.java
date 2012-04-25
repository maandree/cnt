/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright Ⓒ 2012  Mattias Andrée, Peyman Eshtiagh,
 *                   Calle Lejdbrandt, Magnus Lundberg
 *
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.demo;
import cnt.mock.*;
import cnt.network.*;
import cnt.*;

import java.awt.*;
import java.util.*;
import java.io.*;
import java.net.*;


/**
 * Networking demo class
 *
 * @author  Mattias Andrée, <a href="maandree@kth.se">maandree@kth.se</a>
 */
public class NetworkingDemo
{
    
    /**
     * Non-constructor
     */
    private NetworkingDemo()
    {
	assert false : "You may not create instances of this class [NetworkingDemo].";
    }
    
    
    
    
    /**
     * This is the main entry point of the program
     * 
     * @param  args  Start up arguments
     * 
     * @throws  IOException  On I/O exception (should not happen)
     */
    public static void main(final String... args) throws IOException
    {
	final ServerSocket server = new ServerSocket(9999);
	
	
	final Thread thread0 = new Thread()
	        {
		    /**
		     * {@inheritDoc}
		     */
		    @Override
		    public void run()
		    {
			try
			{
			    final Socket sock = server.accept();
			    final InputStream in = sock.getInputStream();
			    final OutputStream out = sock.getOutputStream();
			    
			    final BlackboardNetworking network0 = new BlackboardNetworking(new GameNetworking(new ObjectNetworking(in, out)));
			    
			    
			    System.out.println("network0: sending message: ChatMessage");
			    Blackboard.broadcastMessage(new Blackboard.ChatMessage("Mattias", Color.RED, "sending a message"));
			    
			    System.out.println("network0: sending message: SystemMessage");
			    Blackboard.broadcastMessage(new Blackboard.SystemMessage(null, null, "system message"));
			    
			    System.out.println("network0: sending message: UserMessage");
			    Blackboard.broadcastMessage(new Blackboard.UserMessage("local user chat message"));
			    
			    System.out.println("network0: sending message: MatrixPatch");
			    Blackboard.broadcastMessage(new Blackboard.MatrixPatch(new boolean[2][2], new Color[2][2], 0, 0));
			}
			catch (final Throwable err)
			{
			    err.printStackTrace(System.err);
			}
		    }
	        };

	final Thread thread1 = new Thread()
	        {
		    /**
		     * {@inheritDoc}
		     */
		    @Override
		    public void run()
		    {
			try
			{
			    final Socket sock = new Socket("127.0.0.1", 9999);
			    final InputStream in = sock.getInputStream();
			    final OutputStream out = sock.getOutputStream();
			    
			    final BlackboardNetworking network1 = new BlackboardNetworking(new GameNetworking(new ObjectNetworking(in, out)))
				    {
					/**
					 * {@inheritDoc}
					 */
					@Override
					protected void broadcastMessage(final Blackboard.BlackboardMessage message) throws IOException, ClassNotFoundException
					{
					    System.out.println("network1: received blackboard message: " + message.getClass().toString());
					}
				    };
			    
			    Blackboard.unregisterObserver(network1);
			    
			    
			    for (int i = 0; i < 4; i++)
				network1.receiveAndBroadcast();
			}
			catch (final Throwable err)
			{
			    err.printStackTrace(System.err);
			}
		    }
	        };
	
	thread0.start();
	thread1.start();
    }
    
}

