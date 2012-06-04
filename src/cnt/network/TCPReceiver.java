/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.network;
import cnt.Blackboard;
import cnt.Blackboard.*;
import cnt.messages.*;
import cnt.game.Player;

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
     * @param  connectionNetworking  The {@link ConnectionNetworking} instace to map peer and socket in
     */
    public TCPReceiver(Socket connection, ConnectionNetworking connectionNetworking)
    {
	
	    this(connection, null, connectionNetworking);
    }

    /**
     * Constructor
     *
     * @param  connection            The incoming connection as a socket
     * @param  stream                The {@link ObjectInputStream} to use
     * @param  connectionNetworking  The {@link ConnectionNetworking} instace to map peer and socket in
     */
    public TCPReceiver(Socket connection, ObjectInputStream stream, ConnectionNetworking connectionNetworking)
    {
	this.connection = connection;
	this.input = stream;
	this.gameNetworking = connectionNetworking.gameNetworking;
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
    private ObjectInputStream input;

    /**
     * ID of connecting player
     */
    private int foreignID;
    
    
    
    /**
     * {@inheritDoc}
     */
	public void run()
	{
		
		/* prepair outgoing stream */
		ObjectOutputStream output;

		/* prepair id to map sockets and streams to */
		int peer;
		
		Packet packet;
		    
		try
		{
		if (this.input == null)
			this.input = new ObjectInputStream(new BufferedInputStream(this.connection.getInputStream()));

		packet = (Packet)(this.input.readObject());

		/* Start sorting the packet */
			if (packet.getMessage().getMessage() instanceof Handshake)
			{
				output = new ObjectOutputStream(new BufferedOutputStream(this.connection.getOutputStream()));
			
				Handshake message = (Handshake)packet.getMessage().getMessage();
				if (message.getID() < 0)
				{
					peer = this.connectionNetworking.getHighestID() + 1;
					output.writeObject(new HandshakeAnswer(peer, this.connectionNetworking.localID));
					output.flush();
				} else
					peer = message.getID();
			} else {
				this.connection.close();
				return;
			}
		} catch (Exception ioe)
		{
			return;
		}
		
		
		// Take ID and map the connection and peer in ConnectionNetworking
		this.connectionNetworking.sockets.put(peer, this.connection);
		this.connectionNetworking.inputs.put(peer, input);
		this.connectionNetworking.outputs.put(peer, output);
		try 
		{
			while(true)
			{
				packet = (Packet)this.input.readObject();
				if (packet.getMessage() instanceof Broadcast)
				{
					this.connectionNetworking.send(packet);
					if (packet.getMessage().getMessage() instanceof BlackboardMessage)
						this.gameNetworking.receive(packet);
					else if (packet.getMessage().getMessage() instanceof ConnectionMessage)
						System.err.println("\n\nGot a ConnectionMessage in a Broadcast while being connected, shouldn't happen\n");

				} else if (packet.getMessage() instanceof Whisper)
				{
					Whisper message = (Whisper)packet.getMessage();
					if (message.getReceiver() != this.connectionNetworking.localID)
						this.connectionNetworking.send(packet);
					else
					{
						if (packet.getMessage().getMessage() instanceof BlackboardMessage)
							this.gameNetworking.receive(packet);
						else if (packet.getMessage().getMessage() instanceof ConnectionMessage)
							System.err.println("\n\nGot a ConnectionMessage in a Whisper while being connected, shouldn't happen\n");
					}
				}
			}
			
		} catch (IOException ioe)
		{
			if (this.foreignID < this.connectionNetworking.localID)
				this.connectionNetworking.reconnect(this.foreignID);
		} catch (Exception err)
		{
			Blackboard.broadcastMessage(new PlayerDropped(Player.getInstance(this.foreignID)));
			try
			{
				this.connection.close();
			} catch (Exception ierr)
			{
				// Do nothing
			}
			return;
		}
	}
}
