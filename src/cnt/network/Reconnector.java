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
import cnt.game.*;
import cnt.util.*;
import cnt.*; //Blackboard class to send messages with

import java.util.*;
import java.net.*;
import java.io.*;


public class Reconnector implements BlackboardObserver
{
	
	/**
	* Constructor using Singleton pattern
	*/
	private Reconnector(ConnectionNetworking connectionNetworking)
	{
		this.connectionNetworking = connectionNetworking;
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
	 * Monitor object to use for internal monitoring of PlayerRejoins
	 */
	private final Object joined = new Object();

	/**
	* Set of IDs for the connections that are dead and we need to reconnect,
	*/
	public HashSet<Integer> deadIDs = new HashSet<Integer>();

	/**
	 * Checks if a player has joined in a specefic timeframe
	 */
	private boolean playerJoined = false;
    
    
    
	/**
	* Method to get the instance of Reconnector
	*
	* @return the Reconnector instance
	*/
	public static Reconnector getInstance(ConnectionNetworking connectionNetworking)
	{
	    if (instance == null)
		synchronized (Reconnector.class)
		{
		    if (instance == null)
			instance = new Reconnector(connectionNetworking);
		}
	    
	    return instance;
	}

	/**
	 * Initializer
	 */
	{
		Blackboard.registerObserver(this);
	}

	/**
	* Add an ID discovered to be dead and send notify to monitor to start reconnecting
	*
	* @param id Player id to add to the set
	*/
	public void addID(int id)
	{
		synchronized (this.deadIDs)
		{   	
			this.deadIDs.add(id);
			this.reconnect();
			
		}
	}

	/**
	* Remove an ID discovered to be connected again.
	*/
	public void removeID(int id)
	{
		synchronized (this.deadIDs)
		{
			this.deadIDs.remove(id);
		}
	}

	/**
	* Start trying to reconnect to IDs in the list.
	*/
	protected void reconnect()
	{
		while (true)
		{
			    synchronized (this.deadIDs)
			    {
				if (this.deadIDs.isEmpty())
				{
					System.err.println("\033[1;33mReconnector: deadIDs is Empty\033[0m");
					try
					{   this.deadIDs.wait();
					}
					catch (final InterruptedException err)
					{   
						System.err.println("\033[1;31mReconnector: EXCEPTION ENCOUNTERED\033[0m");
					}
				 }
				System.err.println("\033[1;33mReconnector: DeadIDs is NOT empty\033[0m");
			    }
		
			
			System.err.println("\033[1;33mReconnector: EmergencyPause in effect\033[0m");
			
			/* Check if selfdead */
			try
			{
				Toolkit.getPublicIP();
			} catch (IOException err)
			{
				Blackboard.broadcastMessage(new GameOver());
				Blackboard.broadcastMessage(new PlayerDropped(Player.getInstance(this.connectionNetworking.localID)));
				return;
			}
			System.err.println("\033[1;33mReconnector:Not selfdead \033[0m");
			
			if (this.deadIDs.contains(this.connectionNetworking.foreignID))
			{
				int id = this.connectionNetworking.foreignID; // less to type
				Socket connection = null;
				
				Socket dead = this.connectionNetworking.sockets.get(id);
				System.err.println("\033[1;33mDead socket is " + (dead == null ? "\033[1;31mNull\033m" : "\033[1;32mOK\033[0m"));
				if (dead != null)
				{
					connection = this.connectionNetworking.connect((Inet4Address)(dead.getInetAddress()), dead.getPort(), true);

					if (connection != null && handleConnection(connection, id))
						continue;
				}

				System.err.println("\033[1;33mReconnector: Couldn't connect back to our server\033[0m");
			
				ArrayList<Player> players = new ArrayList<Player>();
				for (int playerID : this.connectionNetworking.connectedIDs)
				{
					Player player = Player.getInstance(playerID);
					try
					{
						if (this.deadIDs.contains(playerID) == false && 
						    this.connectionNetworking.localID != playerID && 
						    playerID < this.connectionNetworking.localID && 
						    player.getConnectedTo() == this.connectionNetworking.foreignID && 
						    player.getPublicIP() == Toolkit.getPublicIP())
						{
							players.add(player);
						}
					} catch (IOException ioe)
					{
						continue;
					}
				}

				System.err.println("\033[1;33mReconnector: Trying own network\033[0m");
				System.err.println("\033[1;33mReconnector: Players empty: " + (players.isEmpty() ? "\033[1,31mEmpty\033[m" : "\033[1;32OK\033[0m"));
				if (players.isEmpty() == false)
				{
					
					Collections.sort(players);
					
					for (Player player : players)
					{
						try
						{
							connection = this.connectionNetworking.connect((Inet4Address)(InetAddress.getByName(player.getLocalIP())), player.getPort(), false);
						} catch (Exception e)
						{ 
							connection = null;
						}
						if (connection != null && handleConnection(connection, player.getID()))
							continue;
						else
						{
							try
							{
								connection = this.connectionNetworking.connect((Inet4Address)(InetAddress.getByName(player.getPublicIP())), player.getPort(), false);
							} catch (Exception e) {
								connection = null;
							}
							if (connection != null)
							{
								handleConnection(connection, player.getID());
								continue;
							}
						}
					}
				}

				System.err.println("\033[1;33mReconnector: Couldn't connect on my network\033[0m");

				for (int playerID : this.connectionNetworking.connectedIDs)
				{
					Player player = Player.getInstance(playerID);
					try
					{
						if (this.deadIDs.contains(playerID) == false &&
						    this.connectionNetworking.localID != playerID &&
						    playerID < this.connectionNetworking.localID &&
						    player.getConnectedTo() == this.connectionNetworking.foreignID &&
						    player.getPublicIP() != Toolkit.getPublicIP())
						{
							players.add(player);
						}
					} catch (IOException ioe)
					{
						continue;
					}
				}
				
				System.err.println("\033[1;33mReconnector: Trying other networks\033[0m");	
				System.err.println("\033[1;33mReconnector: Players empty: " + (players.isEmpty() ? "\033[1,31mEmpty\033[m" : "\033[1;32OK\033[0m"));
				if (players.isEmpty() == false)
				{
					Collections.sort(players);
					for (Player player : players)
					{	
						try
						{
							connection = this.connectionNetworking.connect((Inet4Address)(InetAddress.getByName(player.getLocalIP())), player.getPort(), false);
						} catch (Exception e)
						{
							connection = null;
						}
						if (connection != null)
						{
							handleConnection(connection, player.getID());
							continue;
						}
					}
				}
				
				System.err.println("\033[1;33mReconnector: Couldnt connect to neighbour on other network\033[0m");

				for (int playerID : this.connectionNetworking.connectedIDs)
				{
					Player player = Player.getInstance(playerID);
					try
					{
						if (this.deadIDs.contains(playerID) == false && 
						    this.connectionNetworking.localID != playerID && 
						    playerID < this.connectionNetworking.localID && 
						    player.getConnectedTo() != this.connectionNetworking.foreignID && 
						    player.getPublicIP() == Toolkit.getPublicIP())
						{
							players.add(player);
						}
					} catch (IOException ioe)
					{
						continue;
					}
				}

				System.err.println("\033[1;33mReconnector: Trying strangers on my network\033[0m");	
				System.err.println("\033[1;33mReconnector: Players empty: " + (players.isEmpty() ? "\033[1,31mEmpty\033[m" : "\033[1;32OK\033[0m"));
				
				if (players.isEmpty() == false)
				{
					Collections.sort(players);
					for (Player player : players)
					{
						try
						{
							connection = this.connectionNetworking.connect((Inet4Address)(InetAddress.getByName(player.getLocalIP())), player.getPort(), false);
						} catch (Exception e)
						{
							connection = null;
						}
						if (connection != null && handleConnection(connection, player.getID()))
							continue;
						else
						{
							try
							{
								connection = this.connectionNetworking.connect((Inet4Address)(InetAddress.getByName(player.getPublicIP())), player.getPort(), false);
							} catch (Exception e)
							{
								connection = null;
							}
							if (connection != null)
							{
								handleConnection(connection, player.getID());
								continue;
							}
						}
					}
				}
				
				System.err.println("\033[1;33mReconnector: Couldn't connect to NON neighbour on other network\033[0m");

				for (int playerID : this.connectionNetworking.connectedIDs)
				{
					Player player = Player.getInstance(playerID);
					try
					{
						if (this.deadIDs.contains(playerID) == false &&
						    this.connectionNetworking.localID != playerID &&
						    playerID < this.connectionNetworking.localID &&
						    player.getConnectedTo() != this.connectionNetworking.foreignID &&
						    player.getPublicIP() != Toolkit.getPublicIP())
						{
							players.add(player);
						}
					} catch (IOException ioe)
					{
						continue;
					}
				}
	
				System.err.println("\033[1;33mReconnector: Trying total strangers\033[0m");	
				System.err.println("\033[1;33mReconnector: Players empty: " + (players.isEmpty() ? "\033[1,31mEmpty\033[m" : "\033[1;32OK\033[0m"));

				if (players.isEmpty() == false)
				{
					Collections.sort(players);
					for (Player player : players)
					{
						try
						{
							connection = this.connectionNetworking.connect((Inet4Address)(InetAddress.getByName(player.getLocalIP())), player.getPort(), false);
						} catch (Exception e)
						{
							connection = null;
						}
						if (connection != null)
						{
							handleConnection(connection, player.getID());
							continue;
						}
					}
				}
				
				System.err.println("\033[1;33mReconnector: All is lost\033[0m");
					
			}
			
			for(;;)
			{
				synchronized (joined)
				{
					this.playerJoined = false;
					try
					{
						System.err.println("\033[1;33mReconnector: timeout 2 sec in effect\033[0m");
						joined.wait(2000);
					} catch (InterruptedException ie)
					{
						return;
					}
					if (this.playerJoined == false)
					{
						synchronized (this.deadIDs)
						{
							for (int playerID : this.deadIDs)
							{
								Blackboard.broadcastMessage(new PlayerDropped(Player.getInstance(playerID)));
							}

							this.deadIDs.clear();
						}
						break;
					}
				}
			}
			System.err.println("\033[1;33mReconnector: We are done\033[0m");

		}

	}
	private boolean handleConnection(Socket connection, int id)
	{
		try
		{	
	    		ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(connection.getOutputStream()));
			output.flush();
	    		ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(connection.getInputStream()));
				    		
	    		this.connectionNetworking.send(PacketFactory.createReconnectionHandshake(id), output);
				    		
	    		HandshakeAnswer answer = null;
	    		answer = (HandshakeAnswer)(input.readObject());

			FullUpdate update = (FullUpdate)(input.readObject());
			Blackboard.broadcastMessage(update);

			this.connectionNetworking.sockets.put(id, connection);
			this.connectionNetworking.inputs.put(id, input);
			this.connectionNetworking.outputs.put(id, output);
			this.connectionNetworking.foreignID = id;

			TCPReceiver receiver = new TCPReceiver(connection, input, this.connectionNetworking, id);
	    		Thread t = new Thread(receiver);
	    		t.start();

			Blackboard.broadcastMessage(new PlayerRejoined(Player.getInstance(id)));

			this.deadIDs.remove(id);
			return true;

		} catch (Exception err) {
			Blackboard.broadcastMessage(new PlayerDropped(Player.getInstance(id)));
			if (deadIDs.contains(id) == false)
				deadIDs.add(id);
		}

		return false;
	}

	public void messageBroadcasted(final BlackboardMessage message)
	{
		if (message instanceof PlayerRejoined)
		{
			synchronized (joined)
			{
				joined.notifyAll();
			}
		}
	}

}
