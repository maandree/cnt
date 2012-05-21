/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright Ⓒ 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.network;

// Blackboardclass to send messages with
import cnt.Blackboard;
import cnt.messages.*;

// Classes needed for TCP sockets
import java.util.*;
import java.io.*;

// Classes needed for UDP socket
import java.net.*;


/**
 * The TCP handler for an incoming connection
 * 
 * @author Calle Lejdbrandt <a href="mailto:callel@kth.se">callel@kth.se</a>
 */
public class TCPReceiver implements Runnable
{
    /**
     * Constructor - takes the incoming connection and an instance of ObjectNetworking and an instance of ConnectionNetworking
     *
     * @param  connection            the incoming connection as a Socket
     * @param  connectionNetworking  the ConnectionNetworking instace to map peer and socket in
     */
    public TCPReceiver(Socket connection, ConnectionNetworking connectionNetworking)
    {
	this.connection = connection;
	this.connectionNetworking = connectionNetworking;

    }
    
    
    
    /**
     * the Socket to use for incoming streams
     */
    private final Socket connection;
	
    /**
     * the ConnectionNetworking instance to map incoming connections to
     */
    private final ConnectionNetworking connectionNetworking;
    
    
    
    public void run()
    {
	try
	{
	    ObjectNetworking.addAlternative(connectionNetworking.globalIn, this.connection.getInputStream());
	    //FIXME put colour
	}
	catch (IOException ioe) 
	{
	    // TODO: make some error handling happen
	}
    }
}
