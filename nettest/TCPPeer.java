import java.io.*;
import java.net.*;


/* ports:

   0            Autoselect dynamic port
   1–1022       Well-known ports   Requires superuser permissions
   1025–49150   Registered ports
   49153–65534  Dynamic, private or ephemeral ports
   
   TCP server requires port forwarding
   
 */


public class TCPPeer
{
    public static void main(final String... args) throws IOException
    {
	final char name = args[0].charAt(0);
	final boolean serverauth = args[1].charAt(0) == 's';
	final String pubip = args[2];
	final int serverport = Integer.parseInt(args[3]);
	final String remote = args[4];
	
	
	System.out.println("I am " + name +
			   ", and " + (serverauth ? "may" : "may not") +
			   " be server on\n [" + pubip + "]:" + serverport);
	System.out.println("And the remote server is [" + remote + "]:" + serverport);
	
	
	start(name, serverauth, pubip, serverport, remote);
    }
    
    
    /**
     * @param  name        The name of the peer
     * @param  serverauth  Whether the peer may be the network's server
     * @param  pubip       The local public IP address
     * @param  serverport  The port the servers use
     * @param  remote      The remote public IP address
     */
    public static void start(final char name, final boolean serverauth, final String pubip, final int serverport, final String remote) throws IOException
    {
	ServerSocket _server = null;
	
	if (serverauth)
	    try
	    {
		_server = new ServerSocket(serverport);
	    }
	    catch (final BindException err) //"Address already in use" | "Permission denied"
	    {
		_server = null;
	    }
	
	final ServerSocket server = _server;
	
	final String[] peers = { Character.toString(name) };
	final Object monitor = new Object();
	
	if (server != null)
	{
	    System.out.println("I am a server");
	    
	    final Thread threadServer = new Thread()
		    {
			@Override
			public void run()
			{
			    try
			    {
				for (;;)
				    connect(server.accept(), monitor, peers);
			    }
			    catch (final IOException err)
			    {
				System.out.println("Server error");
			    }
			};
		    };
	    
	    final Thread threadClient = new Thread()
		    {
                        @Override
			public void run()
			{
			    try
			    {
				connect(new Socket(remote, serverport), monitor, peers);
			    }
			    catch (final Throwable err)
			    {
				System.out.println("No remote server to connect to");
			    }
			}
		    };
	    
	    threadServer.start();
	    threadClient.start();
	}
	else
	{
	    System.out.println("I am a client");
	    
	    final Thread threadClient = new Thread()
		    {
			@Override
			public void run()
			{
			    try
			    {
				connect(new Socket(pubip, serverport), monitor, peers);
				System.out.println("I am connected to local server");
			    }
			    catch (final Throwable err)
			    {
				try
				{
				    connect(new Socket(remote, serverport), monitor, peers);
				    System.out.println("I am connected to remote server");
				}
				catch (final Throwable ierr)
				{
				    System.out.println("I could not connect to any server");
				}
			    }
			}
		    };
	    
	    threadClient.start();
	}
    }
    
    
    private static void connect(final Socket sock, final Object monitor, final String[] peers) throws IOException
    {
	final InputStream in = sock.getInputStream();
	final OutputStream out = sock.getOutputStream();
	
	final Thread threadIn = new Thread()
	        {
		    @Override
		    public void run()
		    {
			try
			{
			    for (;;)
			    {
				final String oldpeers = peers[0];
				
				String newpeers = new String();
				for (int p; (p = in.read()) != '\n';)
				    newpeers += (char)p;
				
				synchronized (monitor)
				{
				    for (int i = 0, n = newpeers.length(); i < n; i++)
					if (peers[0].indexOf(newpeers.charAt(i)) < 0)
					    peers[0] += newpeers.charAt(i);
				    
				    if (peers[0].equals(oldpeers) == false)
				    {
					System.out.println("I can send to: " + peers[0]);
					
					monitor.notifyAll();
				    }
				}
			    }
			}
			catch (final IOException err)
			{
			    synchronized (Peer.class)
			    {
				System.err.print("\033[31m");
				System.err.println("error: IOException");
				err.printStackTrace(System.err);
				System.err.print("\033[0m");
			    }
			}
		    }
	        };
	
	final Thread threadOut = new Thread()
	        {
		    @Override
		    public void run()
		    {
			try
			{
			    synchronized (monitor)
			    {
				for (;;)
				{
				    for (int i = 0, n = peers[0].length(); i < n; i++)
					out.write(peers[0].charAt(i));
				    out.write('\n');
				    out.flush();
				    
				    monitor.notifyAll();
				    monitor.wait();
				}
			    }
			}
			catch (final InterruptedException err)
			{
			    synchronized (Peer.class)
			    {
				System.err.println("error: InterruptedException");
			    }
			}
			catch (final IOException err)
			{
			    synchronized (Peer.class)
			    {
				System.err.print("\033[33m");
				System.err.println("error: IOException");
				err.printStackTrace(System.err);
				System.err.print("\033[0m");
			    }
			}
		    }
	        };
	
	threadIn.start();
	threadOut.start();
    }
    
}

