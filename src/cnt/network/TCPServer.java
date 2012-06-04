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

import java.util.*;
import java.io.*;
import java.net.*;


/**
* The TCP Server listening for incoming connections
* 
* @author  Calle Lejdbrandt, <a href="mailto:callel@kth.se">callel@kth.se</a>
*/
public class TCPServer implements Runnable
{
    /**
     * Constructor
     *
     * @param  serverSocket          The socket to listen to for inkoming connections
     * @param  connectionNetworking  The {@link ConnectionNetworking} instance to use for mapping incoming connections 
     */
    public TCPServer(ServerSocket serverSocket, ConnectionNetworking connectionNetworking)
    {
	this.serverSocket = serverSocket;
	this.connectionNetworking = connectionNetworking;
    }
    
    
    
    /**
     * The {@link ServerSocket} instance to use as a TCP server
     */
    private final ServerSocket serverSocket;
	
    /**
     * The {@link ConnectionNetworking} instance to map incoming connections to
     */
    private final ConnectionNetworking connectionNetworking;
    
    
    
    /**
     * {@inheritDoc}
     */
    public void run()
    {
	// Socket to use for incoming connection
	Socket in_conn = null;
	
	for (;;)
	{
	    try
	    {   in_conn = this.serverSocket.accept();
	    }
	    catch (IOException ioe)
	    {   // TODO: make some error handling happen
	    }
	    
	    TCPReceiver receiver = new TCPReceiver(in_conn, this.connectionNetworking);
	    
	    (new Thread(receiver)).start();
	}
    }
    
}

