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

impoty java.util.*;

public class Reconnector
{
	
	/**
	* Constructor using Singleton pattern
	*/
	private Reconnector(ConnectionNetworking connectionNetworking)
	{
		this.connectionNetworking = connectionNetworking;
		this.reconnect();
	}
	
	/**
	* The instance of the Reconnector
	*/
	private static Reconnector instance;

	/**
	* ConnectionNetworking instance to use
	*/
	private final ConnectionNetworking connectionNetworking;
	/**
	* Monitor object to use for internal monitoring
	*/
	private Object montior = new Object();

	/**
	* Set of IDs for the connections that are dead and we need to reconnect,
	*/
	private HashSet<Integer> deadIDs = new HashSet<Integer>();
	/**
	* Method to get the instance of Reconnector
	*
	* @return the Reconnector instance
	*/
	public static getInstance(ConnectionNetworking connectionNetworking)
	{
		if (instance != null)
			return this.instance;
		else
		{
			this.instance = new Reconnector(connectionNetworking);
			return this.instance;
		}
	}

	/**
	* Add an ID discovered to be dead and send notify to monitor to start reconnecting
	*
	* @param id Player id to add to the set
	*/
	public serializable void addID(int id)
	{
		this,deadIDs.add(id);
		this.monitor.notify();
	}

	/**
	* Remove an ID discovered to be connected again.
	*/
	public serializable void removeID(int id)
	{
		if (this.deadIDs.contains(id))
			this.deadIDs.remove(id);
	}

	/**
	* Start trying to reconnect to IDs in the list.
	*/
	protected serializable void reconnect()
	{
		while (true)
		{
			if (this.deadIDs.isEmpty())
				monitor.wait()
			ACDLinkedList<Player> playerRing = PlayerRing.getRing();
	
			if (this.deadIDs.contains(this.connectionNetowkring.foreignID))
			{
				int id = this.connectionNetworking.foreignID; // less to type
				
				Socket dead = this.connectionNetworking.connections.get(id);
				Socket connection = new this.connectionNetworking.connect(dead.getLocalHostAddress(), dead.getLocalPort(), true);
				if (connection == null)
				{
					//* We couldn't connect so we dop the player
					Blackboard.broadcastMessage(new PlayerDroped(playerRing.get(id)));
					this.deadIDs.remove(id)
					continue;
				}
				
				ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(connection.getOutputStream()));
				ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(connection.getInputStream()));

				this.connectionNetworking.send(new Handshake(this.connectionNetworking.localID), true, output);
				
				HandshakeAnswer answer = null;
				try
				{
					answer = input.readObject();
				} catch (Exception err) {
					if (this.connectionNetworking.isServer)
					{
						Blackboard.broadcastMessage(new PlayerDropd(playerRing.get(id)));
						this.deadIDs.remove(id);
					} else
					{
						
