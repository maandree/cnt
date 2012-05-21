/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright Ⓒ 2012  Mattias Andrée, Peyman Eshtiagh,
 *                   Calle Lejdbrandt, Magnus Lundberg
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
	* @param message the serialized object to send
	*/
	public TCPSender(Socket socket, Serializable message) throws IOException
	{
		this.socket = socket;
		this.message = message;
		
		// test if we can get the input stream.	
		InputStream in = socket.getInputStream();
	}
	
	/**
	* Socket to use for sending
	*/
	Socket socket;
	
	/**
	* Message to be sent
	*/
	Serializable message;
	
	public void run()
	{
		ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));
		out.flush(); // Program freezes otherwise

		out.writeObject(message);
		out.flush();
	}
}	
