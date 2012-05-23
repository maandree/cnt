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
	* the ObjectNetworking instance to send objects to
	*/
	private final ObjectNetworking objectNetworking;
	
	/**
	* the ConnectionNetworking instance to map incoming connections to
	*/
	private final ConnectionNetworking connectionNetworking;

	public void run()
	{
		ObjectInputStream input = null;
		try
		{
			Blackboard.broadcastMessage(new SystemMessage(null, "Getting something"));
			input = new ObjectInputStream(new BufferedInputStream(this.connection.getInputStream()));
			// Send message to ObjectNetworking layer, and wait for a ID number to be returned
			Integer peer = this.objectNetworking.receive((Serializable)input.readObject());
			// Take ID and map the connection and peer in ConnectionNetworking
			Blackboard.broadcastMessage(new SystemMessage(null, "Came from ID: " + peer));
			if (peer != null) {
				this.connectionNetworking.sockets.put(peer, this.connection);
				this.connectionNetworking.objectInputs.put(peer, input);
				ObjectOutputStream out =  new ObjectOutputStream(new BufferedOutputStream(this.connection.getOutputStream()));
				out.flush();
				this.connectionNetworking.objectOutputs.put(peer, out);
			}
			
			Blackboard.broadcastMessage(new SystemMessage(null, "We now have " + this.connectionNetworking.sockets.size() + " connections"));

		} catch (IOException ioe) 
		{
			// TODO: make some error handling happen
		} catch (ClassNotFoundException cnfe) 
		{
			// TODO: make some error handling happen
		}
	
		try 
		{
			while(true)
			{
				Blackboard.broadcastMessage(new SystemMessage(null, "Waiting for next message"));
				if (!this.connection.isInputShutdown())
					Blackboard.broadcastMessage(new SystemMessage(null, "Connection has alive instream"));
				Serializable message = (Serializable)input.readObject();
				Blackboard.broadcastMessage(new SystemMessage(null, "Receiving new message"));
				this.objectNetworking.receive(message);
			}
		} catch (IOException ioe)
		{
			Blackboard.broadcastMessage(new SystemMessage(null, "IOException receving messages"));
			Blackboard.broadcastMessage(new SystemMessage(null, "IOExceotion: " + ioe.getMessage()));
		} catch (ClassNotFoundException cnfe)
		{
			Blackboard.broadcastMessage(new SystemMessage(null, "ClassNotFoundException reading messages"));
		}
	}
}
