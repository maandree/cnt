
	}

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

// Classes needed for TCP sockets
import java.util.*;
import java.io.*;

// Classes needed for UDP socket
import java.net.*;

/**
* Connection Networking layer
*<p>
* This class sets up the connections to be used by the client. 
* Provides socket connections for higher levels of networking.
*
* @author Calle Lejdbrandt, <a href="callel@kth.se">callel@kth.se</a>
*/

public class ConnectionNetworking implements Runnable
{

	/**
	* Port tha game clients will be using
	*/
	public static final int PORT = 44923;

	/**
	* Default Constructor
	* <p>
	* The default constructor. Will try to make any kind of connection.
	* Order of connection importance (high -&gt; low):
	* Public TCP -&gt; Local TCP 
	* 
	* @param objectNetworking the instance of the ObjectNetworking class to use
	*
	* @throws IOException Thrown in case of network errors.
	*/
	public ConnectionNetworking(ObjectNetworking objectNetworking)
	{
		// Set the ObjectNetworker to use
		this.objectNetworker = objectNetworker;
	
		// Check if public ip is same as internal ip
		// The IP service only want us to check every 300 sec. So getExternalIP should make sure to execute the check only every 300 sec. Use local cache instead of constant lookup.
		if (!getExternalIP().equals(getInternalIP()))
		{
			if(createPortForward(ConnectionNetworking.PORT))
			{
				startTCP(ConnectionNetworking.PORT);
			} else 
			{
				startLokalTCP();
			}
		}
	}

	/**
	* ObjectNetworker to communicate with
	*/
	public final ObjectNetworker objectNetworker;

	/**
	* Public ip. Should not be final since an public ip might change at any time if dynamic
	*/
	public Inet4Adress externalIP;

	/**
	* Lokal ip. Not as probable as Public IP, but same reason.
	*/
	public Inet4Adress internalIP;

	/**
	* Flag to set wheter we can act as a server. I.e. do we have access on public ip?
	*/
	public boolean isServer;

	/**
	* List of peers in cloud. Needs to be able to be access from packet, but doesn't need to be public.
	*/
	final ACDLinkedList<T> peers = new ACDLinkedList<T>();

	/**
	* Map of current threaded connections to use if we are in lokal mode
	*/
	final HashMap<T, Socket> connections = new HashMap<T, Socket>();

	/**
	* Make a TCP serversocket to listen on incoming connections
	*
	* @param port Port to listen on
	*/
	private void startTCP(int port) 
	{
		try {
			ServerSocket _server = new ServerSocket(port);
		} catch (IOException err) 
		{
			Blackboard.broadcastMessage(new Blackboard.SystemMessage(null, null, "Error: Cannot listen to port. Port is busy. Is another game instance running?"));
			return
		}
		
		this.isServer = true;
		
		final Thread serverThread = new Thread(new TCPServer(_server, this.objectNetworker, this);
		serverThread.setDaemon(false);
		serverThread.start();
	}

	/**
	* Setup the client to be running lokaly. Without PublicIP or NAT.
	*/
	private void startLokalTCP()
	{
		this.isServer = false;
		
		Blackboard.broadcastMessage(new Blackboard.SystemMessage(null, null, "Running in local mode. Needs access to at least one server in cloud."));
		
		// TODO: Start new thread that checks all live connections as they are connected and saved
	}
	
	/**
	* Makes a connection to specefied IP and port
	*
	* @param host an Inet4Adress to connect to
	* @param port an int to use as portnumber
	*
	* @return <code>Socket</code> on successfull connection. <code>null</code> otherwise.
	*/
	private final Socket connect(Inet4Adress host, int port, ListNode<T> peer)
	{
		try {
			Socket connection = new Socket(host, port);
		} catch (IOException err) 
		{
			return null;
		} catch (UnknownHostException uhe)
		{
			Blackboard.broadcastMessage(new Blackboard.SystemMessage(null, null, "Unknown host: " + host.getCanonicalHostName() + "(" + host.getHostAdress() + ")"));
			return null;
		}
		
		// Always save live connections. TODO: lookup possible security issues with this.
		this.connections.put(peer, connection);
		
		return connection;
	}
	
	/**
	* Takes a serialized object and sends it to all known remote clients
	*
	* @param message Serialized object to send as message
	*/
	public void send(Serialized message)
	{
		// number of remote clients we managed to send the message to
		int numSent = 0;
		// number of known failures
		int numError = 0;


		if (this.peers != null && this.peers.isEmpty() == false)
		{
			/**
			* Create a arraylist of threads so we can start sending messages
			* to all remote clients as fast as possible.
			* Then we wait for all threads to finish.
			* This should speed up chatter in the cloud while keeping the cloud
			* synzronized.
			*/
			ArrayList<Thread> _threads = new ArrayList<Thread>();
	
			for (ListNode<T> peer : this.peers)
			{
				try 
				{
					// Always check to see if we have live connections. 
					if (this.connections.containsKey(peer.getItem()))
					{
						
						Thread _tmpThread = new Thread(new threadStreamer(this.connections.get(perr.getItem()), peer.getItem().getID(),  message));
						_tmpThread.start();
						_threads.add(_tmpThread);
					} else
					{
						
						Thread _tmpThread = new Thread(new threadStreamer(connect(Inet4Adress.getByHost(peer.getItem().getHost()), peer.getItem().getPort(), peer), peer.getItem().getID(), message));
						_tmpThread.start();
						_threads.add(_tmpThread);
					}
				} catch (Exception err)
				{
					numError++;
					Blackboard.broadcastMessage(new Blackboard.SystemMessage(null, null, "Error sending [ " + message + " ] to " + peer.getItem().getName()));
				}
			}
			
			// Loop thrue the arraylist with threads until all threads are done
			while(true)
			{
				boolean done = false;
				for (Thread current : (Thread)_threads)
				{
					if (current.isAlive() == true)
						done = true;
					else
						numSent++;
						_threads.
				}
				
				if (done)
					break;
			}

			// TODO: Do the checking if all clients got the message. Also to make "server" by makeing a thread that checks all live connections if we are lokal
