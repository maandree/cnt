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
* The TCP Server listening for inkomming connections
* 
* @author Calle Lejdbrandt <a href="mailto:callel@kth.se">callel@kth.se</a>
*/
public class TCPServer implements Runnable
{
    /**
     * Constructor - takes a ServerSocket, an ObjectNetworking instance, and a ConnectionNetworking instance
     *
     * @param  serverSocket          the socket to listen to for inkoming connections
     * @param  connectionNetworking  the ConnectionNetworking instance to use for mapping incoming connections 
     */
    public TCPServer(ServerSocket serverSocket, ConnectionNetworking connectionNetworking)
    {
	this.serverSocket = serverSocket;
	this.connectionNetworking = connectionNetworking;
    }
    
    
    
    /**
     * the ServerSocket instance to use as a TCP server
     */
    private final ServerSocket serverSocket;
    
    /**
     * the ConnectionNetworking instance to map incoming connections to
     */
    private final ConnectionNetworking connectionNetworking;
    
    
    
    public void run()
    {
	// Socket to use for incoming connection
	Socket in_conn = null;
		
	for (;;)
	{
	    try
	    {
		in_conn = this.serverSocket.accept();
	    } catch (IOException ioe)
	    {
		// TODO: make some error handling happen
	    }
	    
	    TCPReceiver receiver = new TCPReceiver(in_conn, this.connectionNetworking);
	    
	    Thread t = new Thread(receiver);
	    t.start();
	}
    }
}
