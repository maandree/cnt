/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.mock;

import java.io.*;
import java.net.*;


/**
 * Connection networking layer mock object
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class ConnectionNetworking
{
    /**
     * Constructor
     * 
     * @param  name        The name of the peer
     * @param  serverauth  Whether the peer may be the network's server
     * @param  pubip       The local public IP address
     * @param  serverport  The port the servers use
     * @param  remote      The remote public IP address
     */
    public ConnectionNetworking(final char name, final boolean serverauth, final String pubip, final int serverport, final String remote) throws IOException
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
    
    
    
    public  final PipedInputStream  globalIn   = new PipedInputStream();
    private final PipedOutputStream privateOut = new PipedOutputStream(globalIn);
    private final PipedInputStream  privateIn  = new PipedInputStream();
    public  final PipedOutputStream globalOut  = new PipedOutputStream(privateIn)
	    {
		private boolean started = false;
		
		@Override
		public void write(final int b) throws IOException
		{
		    if (started == false)
		    {
			super.write(10);
			started = true;
		    }
		    switch (b)
		    {
			case 0:
			case 27:
			    super.write(27);
			    break;
		    }
		    
		    super.write(b);
		}
		
		@Override
		public void flush() throws IOException
		{
		    super.write(0);
		    super.flush();
		    this.started = false;
		}
	    };
    
    
    
    private void connect(final Socket sock, final Object monitor, final String[] peers) throws IOException
    {
	final InputStream in = sock.getInputStream();
	final OutputStream out = sock.getOutputStream();
	final Object mutex = new Object();
	
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
				
				if (newpeers.isEmpty())
				{
				    for (int p; (p = in.read()) != 0;)
				    {
					if (p == 27)
					    p = in.read();
					privateOut.write(p);
				    }
				    privateOut.flush();
				}
				else
				{
				    synchronized (monitor)
				    {
					for (int i = 0, n = newpeers.length(); i < n; i++)
					    if (peers[0].indexOf(newpeers.charAt(i)) < 0)
						peers[0] += newpeers.charAt(i);
					
					if (peers[0].equals(oldpeers) == false)
					{
					    System.out.println("I can send to: " + peers[0]);
					    
					    monitor.notify();
					}
				    }
				}
			    }
			}
			catch (final IOException err)
			{
			    synchronized (ConnectionNetworking.this)
			    {
				System.err.print("\033[31m");
				System.err.println("error: IOException");
				err.printStackTrace(System.err);
				System.err.print("\033[0m");
			    }
			}
		    }
	        };
	
	final Thread threadSysOut = new Thread()
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
				    synchronized (mutex)
				    {
					for (int i = 0, n = peers[0].length(); i < n; i++)
					    out.write(peers[0].charAt(i));
					out.write('\n');
					out.flush();
				    }
				    
				    monitor.wait();
				}
			    }
			}
			catch (final InterruptedException err)
			{
			    synchronized (ConnectionNetworking.this)
			    {
				System.err.println("error: InterruptedException");
			    }
			}
			catch (final IOException err)
			{
			    synchronized (ConnectionNetworking.this)
			    {
				System.err.print("\033[33m");
				System.err.println("error: IOException");
				err.printStackTrace(System.err);
				System.err.print("\033[0m");
			    }
			}
		    }
	        };
	
	final Thread threadMsgOut = new Thread()
	        {
		    @Override
		    public void run()
		    {
			try
			{
			    for (;;)
			    {
				int p = privateIn.read();
				synchronized (mutex)
				{
				    out.write(p);
				    while ((p = privateIn.read()) != 0)
				    {
					out.write(p);
					if (p == 27)
					    out.write(privateIn.read());
					else if (p == 0)
					{
					    out.flush();
					    break;
					}
				    }
				}
			    }
			}
			catch (final IOException err)
			{
			    synchronized (ConnectionNetworking.this)
			    {
				System.err.print("\033[35m");
				System.err.println("error: IOException");
				err.printStackTrace(System.err);
				System.err.print("\033[0m");
			    }
			}
		    }
	        };
	
	threadIn.start();
	threadSysOut.start();
	//threadMsgOut.start();
    }
    
}

