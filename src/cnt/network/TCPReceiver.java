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
* The TCP handler for an incoming connection
* 
* @author  Calle Lejdbrandt, <a href="mailto:callel@kth.se">callel@kth.se</a>
*/
public class TCPReceiver implements Runnable
{
    /**
     * Constructor 
     *
     * @param  connection            The incoming connection as a socket
     * @param  gameNetworking        The {@link GameNetworking} instance to use for callback
     * @param  connectionNetworking  The {@link ConnectionNetworking} instace to map peer and socket in
     */
    public TCPReceiver(Socket connection, GameNetworking gameNetworking, ConnectionNetworking connectionNetworking)
    {
	this.connection = connection;
	this.gameNetworking = gameNetworking;
	this.connectionNetworking = connectionNetworking;
    }

    /**
     * Constructor
     *
     * @param  connection            The incoming connection as a socket
     * @param  stream                The {@link ObjectInputStream} to use
     * @param  gameNetworking        The {@link GameNetworking} instance to use for callback
     * @param  connectionNetworking  The {@link ConnectionNetworking} instace to map peer and socket in
     */
    public TCPReceiver(Socket connection, ObjectInputStream stream, GameNetworking gameNetworking, ConnectionNetworking connectionNetworking)
    {
	this.connection = connection;
	this.input = stream;
	this.gameNetworking = gameNetworking;
	this.connectionNetworking = connectionNetworking;

    }
    
    
    
    /**
     * the Socket to use for incoming streams
     */
    private final Socket connection;
	
    /**
     * the GameNetworking instance to send objects to
     */
    private final GameNetworking gameNetworking;
	
    /**
     * the ConnectionNetworking instance to map incoming connections to
     */
    private final ConnectionNetworking connectionNetworking;

    /**
     * the ObjectIntputStream to use
     */
    private final ObjectInputStream input;
    
    
    
    /**
     * {@inheritDoc}
     */
	public void run()
	{
		try
		{
			if (this.input == null)
				this.input = new ObjectInputStream(new BufferedInputStream(this.connection.getInputStream()));
			
			/* prepair outgoing stream */
			ObjectOutputStream output;

			Packet packet = this.input.readObject();

			/* Start sorting the packet */
			if (packet.getMessage().getMessage() instanceof Handshake)
			{
				Handshake message = packet.getMessage().getMessage();
				if (message.getID() < 0)
				{
					output = new ObjectOutpuStream(new BufferedOutputStream(this.connection.getOutputStream()));
					int id = this.connetionNetworking.getHighestID() + 1;

				
			}
			
			// Take ID and map the connection and peer in ConnectionNetworking
			Blackboard.broadcastMessage(new SystemMessage(null, "Came from ID: " + peer));
			if (peer != null) {
				this.connectionNetworking.sockets.put(peer, this.connection);
				this.connectionNetworking.inputs.put(peer, input);
				ObjectOutputStream out =  new ObjectOutputStream(new BufferedOutputStream(this.connection.getOutputStream()));
				out.flush();
				this.connectionNetworking.outputs.put(peer, out);
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
				this.gameNetworking.receive(message);
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
