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
 * The TCP handler for sending outgoing serialized objects
 * 
 * @author Calle Lejdbrandt <a href="mailto:callel@kth.se">callel@kth.se</a>
 */
public class TCPSender implements Runnable
{
    /**
     * Constructor - takes a Socket and a Serialized objcet to send
     *
     * @param socket the socket to send to
     * @param message the message to send
     */
    public TCPSender(Socket socket, byte[] message) throws IOException
    {
	this.socket = socket;
	this.message = message;
		
	// test if we can get the input stream.	
	socket.getInputStream();
    }
	
    /**
     * Socket to use for sending
     */
    Socket socket;
	
    /**
     * Message to be sent
     */
    byte[] message;
    
    public void run()
    {
	OutputStream out = new BufferedOutputStream(this.socket.getOutputStream());
	out.write(this.message);
	out.flush();
    }
}	
