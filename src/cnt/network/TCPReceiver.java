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
import cnt.util.*;

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
	* @param connection the incoming connection as a Socket
	* @param objectNetworking the ObjectNetworking instance to use for callback
	* @param connectionNetworking the ConnectionNetworking instace to map peer and socket in
	*/
	public TCPReceiver(Socket connection, ObjectNetworking objectNetworking, ConnectionNetworking connectionNetworking)
	{
		this.connection = connection;
		this.objectNetworking = objectNetworking;
		this.connectionNetworking = connectionNetworking;

	}
	
	/**
	* the Socket to use for incoming streams
	*/
	private final Socket connection;
	
	/**
	* the PipedInputStream to read from
	*/
	private final InputStream input = null;

	/**
	* the ObjectNetworking instance to send objects to
	*/
	private final ObjectNetworking objectNetworking;
	
	/**
	* the ConnectionNetworking instance to map incoming connections to
	*/
	private final ConnectionNetworking connectionNetworking;

	/**
	* Internal stream
	*/
	private final PipedOutputStream internalOutput;

	public void run()
	{
		
		// flag for setting if message is priority or not
		boolean prio = false;			
		
		try
		{

			this.input = new PipedInputStream(new BufferedInputStream(this.connection.getInputStream()));

		} catch (IOException ioe) 
		{
			// TODO: make some error handling happen
		} catch (ClassNotFoundException cnfe) 
		{
			// TODO: make some error handling happen
		}
			
		Blackboard.broadcastMessage(new SystemMessage(null, "Getting new connection"));
		
		this.objectNetworking.connect(this);

	}
	
	public byte[] read() {

		//* First message sent should be an new ID request or a PlayerJoined *//
		// Check to see what the message is all about
		char msgType = this.input.read();
		
		// Check what the message is and handle it accordingly
		if (msgType == ConnectionNetworking.PRIORITY)
		{
			prio = true;
			msgType = input.read();
		}
			
		while (true)
		{
			// Check to see what the message is all about
			String msgType = input.readByte();
			
			// Check what the message is and handle it accordingly
			if (msgType == ConnectionNetworking.PRIORITY)
			{
				prio = true;
				msgType = input.readByte();
			}
			switch (msgType)
			{
				case ConnectionNetworking.QUESTION:
					
					break;
				
				case ConnectionNetwokring.MESSAGE:
					this.speak();
					break;
	
				case ConnectionNetworking.OBJECT:
					this.forward();
					break;
				
				default:
					break; // Drop player who sends fulty data?
			}
		}	
	}
	
	protected void listen()
	{

	}

	protected void speak()
	{
	
	}

	protected void forward()
	{
	
	}
}
