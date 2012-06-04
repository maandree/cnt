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
import cnt.game.Player;

// Classes needed for UPnP
import org.teleal.cling.*;
import org.teleal.cling.model.message.header.*;
import org.teleal.cling.model.message.*;
import org.teleal.cling.model.meta.*;
import org.teleal.cling.model.types.*;
import org.teleal.cling.model.action.*;
import org.teleal.cling.registry.*;
import org.teleal.cling.support.igd.*;
import org.teleal.cling.support.igd.callback.*;
import org.teleal.cling.support.model.*;
import org.teleal.cling.controlpoint.*;
import cnt.util.IGDListener;

import java.util.*;
import java.io.*;
import java.net.*;


/**
* <p>Connection Networking layer</p>
* <p>
*   This class sets up the sockets to be used by the client. 
*   Provides socket sockets for higher levels of networking.
* </p>
*
* @author Calle Lejdbrandt, <a href="mailto:callel@kth.se">callel@kth.se</a>
*/
public class ConnectionNetworking
{
	/**
	* Default Constructor
	* <p>
	* The default constructor will try to start a new cloud and act server.
	*</p>
	* 
	* @param gameNetworking the GameNetworking instance to send objects to
	*/
	public ConnectionNetworking(GameNetworking gameNetworking, String playerName)
	{
	    this.upnpKit = new UPnPKit();
		this.gameNetworking = gameNetworking;
	
		Blackboard.broadcastMessage(new SystemMessage(null, "Starting up sockets..."));

		// Check if public ip is same as internal ip
		if (!getExternalIP().equals(getInternalIP()))
		{
			Blackboard.broadcastMessage(new SystemMessage(null, "Public and Local ip differ. Trying UPnP"));
			if(this.upnpKit.createPortForward(PORT))
			{
				startTCP();
			} else 
			{
				// We are trying to start a cloud, but couldn't. Game exits
				Blackboard.broadcastMessage(new GameOver());
			}
		} else
			startTCP();

		this.createPlayer(playerName);
	}
	
	/**
	* Constructor connecting to a cloud
	* <p>
	* Constructor which tries to connect to an already existing cloud.
	*
	* @param gameNetworking the gamenetworking instance to send objects to
	*
	* @param playerName name of local player
	*
	* @param foreignHost string with DNS name or IPv4 address to connect to
	*
	* @param port the port to make the connection to
	*/
	public ConnectionNetworking(GameNetworking gameNetworking, String playerName, String foreignHost, int port) 
	{
	    this.upnpKit = new UPnPKit();
		this.gameNetworking = gameNetworking;

		// Check if public ip is same as internal ip
		if (!getExternalIP().equals(getInternalIP()))
		{
			if(this.upnpKit.createPortForward(PORT))
			{
				startTCP();
			} else 
			{
				startLocalTCP();
			}
		} else
			startTCP();

		try 
		{
			Socket connection = this.connect(foreignHost, port, false);
			ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(connection.getInputStream()));
			ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(connection.getOutputStream()));

			// Handshake(ID (0< if asking), urgent?, ObjectOutputStream)
			this.send( PacketFactory.createConnectionHandshake(), output );
	
			// Get answer
			HandshakeAnswer answer = null;
			try
			{
				answer = input.readObject();
			} catch (Exception err) {
				if (this.isServer)
					Blackboard.broadcastMessage(new SystemMessage(null, "Unable to conact friend."));
				else
				{
					Blackboard.broadcastMessage(new SystemMessage(null, "Unable to contact friend, and we are local. Game Over"));
					Blackboard.broadcastMessage(new GameOver());
				}
			}
			
			
			// By now we should have our ID and the ID from the host we connected to
			if (this.foreignID != null)
			{
				this.outputs.put(this.foreignID, output);
				TCPReceiver receiver = new TCPReceiver(connection, input, this.gameNetworking, this);
				Thread t = new Thread(receiver);
				t.start();
			}
			
		} catch (IOException ioe)
		{
			// blackboardmessage!
		}
		
		// Create the local player
		this.createPlayer(playerName);
		
	}
    


	/**
	* ObjectNetworker to communicate with
	*/
	public final GameNetworking gameNetworking;

	/**
	* Local player instance
	*/
	public final Player localPlayer;
	
	/**
	* Local UUID
	*/
	public final UUID localUUID;
	/**
	* Local players ID, needed before local Player can be created
	*/
	public final int localID;

	/**
	* Foreign players ID, needed before local Player can be created
	*/
	public final int foreignID;

	/**
	* The randomly picked port the client is running on
	*/
	public final int port;

	/**
	* Public ip. 
	*/
	public Inet4Address externalIP;

	/**
	* Local ip. 
	*/
	public Inet4Address internalIP;

	/**
	* Flag to set wheter we can act as a server. I.e. do we have access on public ip?
	*/
	public boolean isServer;

	/**
	* Map of current threaded connections to use to store sockets
	*/
	final HashMap<Integer, Socket> sockets = new HashMap<Integer, Socket>();

	/**
	* The output stream to write to
	*/
	final HashMap<Integer, ObjectOutputStream> outputs = new HashMap<Integer, ObjectOutputStream>();

	/**
	* Map of current threaded connections to use to store ObjectInputStremas
	*/
	final HashMap<Integer, ObjectInputStream> inputs = new HashMap<Integer, ObjectInputStream>();

	/**
	* The UPnP RemoteService being used for UPnP devices. 
	*/
	private UpnpService upnpService;
    
    private UPnPKit upnpKit;
    
    

	/**
	* Make a TCP serversocket to listen on incoming sockets
	*/
	private void startTCP() 
	{
		ServerSocket _server = null;
		try {
			if (this.port != null) 
			{
				_server = new ServerSocket(this.port);
			} else 
			{
				_server = new ServerSocket(0);
				this.port = _server.getLocalPort();
			}
		} catch (IOException err) 
		{
			Blackboard.broadcastMessage(new SystemMessage(null, "Error: Cannot start ServerSocket. Something is wrong."));
			return;
		}
		
		this.isServer = true;
		
		final Thread serverThread = new Thread(new TCPServer(_server, this.gameNetworking, this));
		serverThread.setDaemon(true);
		serverThread.start();
	}

	/**
	* Setup the client to be running locally. Without PublicIP or NAT.
	*/
	private void startLocalTCP()
	{
		this.isServer = false;
		
		Blackboard.broadcastMessage(new SystemMessage(null, "Running in local mode. Needs access to at least one server in cloud."));
		
		// Do nothing else, we can only use outgoing sockets
	}
	
	/**
	* Makes a connection to specefied IP and port
	*
	* @param host an Inet4Address to connect to
	* @param port an portnumber to connect to
	*
	* @return <code>Socket</code> on successfull connection. <code>null</code> otherwise.
	*/
	public final Socket connect(Inet4Address host, int port, boolean save)
	{
		Blackboard.broadcastMessage(new SystemMessage(null, "Initiating connection to ["+ host + ":" + port + "]"));
		Socket connection = null;
		try {
			connection = new Socket(host, port);
		} catch (IOException err) 
		{
			Blackboard.broadcastMessage(new SystemMessage(null, "Connection Failed"));
			return null;
		}
		
		if (save) //This is mainly for the first connection out, we don't want to start a listener until we now what ID to map it to 
		{
			TCPReceiver receiver = new TCPReceiver(connection, this.gameNetworking, this);
			Thread t = new Thread(receiver);
			t.start();
		}

		Blackboard.broadcastMessage(new SystemMessage(null, "Number of open sockets: " + this.sockets.size()));

		return connection;
	}
    
    
	/**
	* Sends a message that came from somewhere and should be routed on
	*/
	public void send(final Packet packet)
	{
	    send(packet, null);
	}

	/**
	* Sends a message thrue a specefied socket 
	*
	* @param connection Socket to use to send thrue
	*/
	public void send(final Packet packet, final ObjectOutputStream output)
	{
	    final int[] playerIDs;
	    //if (packet.getMessage() instanceof Broadcast)
	    if (packet.getMessage() instanceof Anycast == false)
	    {
		if (this.isConnected() == false)
		    return;
		
		final Set<Integer> keySet = this.outputs.keySet();
		final Integer[] _playerIDs = new Integer[keySet.size()];
		this.outputs.keySet().toArray(playerIDs);
		playerIDs = new int[_playerIDs.length];
		for (int i = 0, n = playerIDs.length; i < n; i++)
		    playerIDs = _playerIDs[i];
	    }
	    //else if (packet.getMessage() instanceof Whisper)
	    //{
	    //	playerIDs = new int[((Whisper)(packet.getMessage())).getReceiver()]; //may not be connected to us
	    //}
	    
	    
	    final ObjectOutputStream[] sendTo;
	    int ptr = 0;
	    if (packet.getMessage() instanceof Anycast)
	    {
		sendTo = new ObjectOutputStream[] { output };
		ptr = 1;
	    }
	    else
	    {
		sendTo = new ObjectOutputStream[playerIDs.length];
		for (final int player : playerIDs)
		    if (packet.addHasGotPacket(player))
			sendTo[ptr++] = this.outputs.get(player);
	    }
	    
	    for (int i = 0; i < ptr; i++)
		try
	        {
		    sendTo[i].writeObject(packet);
		    sendTo[i].flush();
		}
		catch (IOException ioe)
		{
		    if (output != null)
			Blackboard.broadcastMessage(new SystemMessage(null, "Special Send failed!"));
		    else
		    {
			System.err.println("\n\nErrer routing message to [" + id +"]: Skipping, he will get it in the full update he gets when he reconnects\n");
			if (id < this.localID)
			    this.reconnect(id);
		    }
		}
	}

	/**
	* Adds the ID that needs to be reconnected to the Reconnector
	*
	* @param id the player ID that lost connection
	*/
	public synchronized void reconnect(int id)
	{
		Reconnector.getInstance(this).addID(id);
	}

	/**
	* Removes ID that connected to us from Reconnector
	*
	* @param id the player ID the connected
	*/
	public synchronized void connected(int id)
	{
		Reconnector.getInstance(this).removeID(id);
	}

	/**
	* Check to see if there is any connections to send to
	*
	* @return true if there is connections false otherwhise
	*/
	private boolean isConnected()
	{
		if (this.sockets.isEmpty()) {
			Blackboard.broadcastMessage(new SystemMessage(null, "No connections found"));
			return false;
		}
		return true;
	}
	
	/**
	* Retrive the internatl IP from the local host.
	*
	* Sets the local ip, and also returns it as a Inet4Address object.
	*
	* @return internal ip
	*/
	private Inet4Address getInternalIP()
	{

		this.internalIP = Toolkit.getLocalIP();
		return this.internalIP;
	}
	
	/**
	* Retrives the external ip adress by adress lookup.
	*
	* Lookups the external ip for the host. Uses a cache and only does lookups every 5min.
	*
	* @return external ip
	*/
	private Inet4Address getExternalIP()
	{
		this.externalIP = (Inet4Address)Inet4Address.getByName(Toolkit.getPublicIP());
		return this.externalIP;	
	}
}
