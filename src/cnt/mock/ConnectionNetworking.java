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
     * <p>Constructor.</p>
     * <p>
     *   The thread calling constructor must be keep alive, otherwise the pipes ({@link #globalIn} and {@link #globalOut}) breaks.
     * </p>
     * 
     * @param  name        The name of the peer
     * @param  serverauth  Whether the peer may be the network's server
     * @param  pubip       The local public IP address
     * @param  serverport  The port the servers use
     * @param  remote      The remote public IP address
     */
    public ConnectionNetworking(final char name, final boolean serverauth, final String pubip, final int serverport, final String remote) throws IOException
    {
	System.err.println("I am " + name + " on " + pubip);
	if (serverauth == false)
	    System.err.println("I may not be a server");
	System.err.println("Servers use port " + serverport + ",");
	System.err.println("And my remote peer is " + remote);
	
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
	
	createPipes();
	
	if (server != null)
	{
	    System.err.println("I am a server");
	    
	    final Thread threadServer = new Thread()
		    {
			/**
			 * {@inheritDoc}
			 */
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
				System.err.println("Server error");
			    }
			};
		    };
	    
	    final Thread threadClient = new Thread()
		    {
			/**
			 * {@inheritDoc}
			 */
                        @Override
			public void run()
			{
			    try
			    {
				if (remote.equals(pubip) == false)
				    connect(new Socket(remote, serverport), monitor, peers);
				else
				    System.err.println("Remote server is local server");
			    }
			    catch (final Throwable err)
			    {
				System.err.println("No remote server to connect to");
			    }
			}
		    };
	    
	    threadServer.start();
	    threadClient.start();
	}
	else
	{
	    System.err.println("I am a client");
	    
	    final Thread threadClient = new Thread()
		    {
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void run()
			{
			    try
			    {
				connect(new Socket(pubip, serverport), monitor, peers);
				System.err.println("I am connected to local server");
			    }
			    catch (final Throwable err)
			    {
				try
				{
				    connect(new Socket(remote, serverport), monitor, peers);
				    System.err.println("I am connected to remote server");
				}
				catch (final Throwable ierr)
				{
				    System.err.println("I could not connect to any server");
				}
			    }
			}
		    };
	    
	    threadClient.start();
	}
    }
    
    
    
    /**
     * Message data input stream
     */
    public  PipedInputStream  globalIn;
    
    /**
     * The other end of {@link #globalIn}
     */
    private PipedOutputStream privateOut;
    
    /**
     * The other end of {@link #globalOut}
     */
    private PipedInputStream  privateIn;
    
    /**
     * Message data output stream
     */
    public  PipedOutputStream globalOut;
    
    
    
    /**
     * <p>Creates input and output pipes.</p>
     * <p>
     *   The thread calling method "must" be keep alive, otherwise the pipes ({@link #globalIn} and {@link #globalOut}) breaks.
     * </p>
     *
     * @throws  IOException  On I/O error, this should not happen
     */
    private void createPipes() throws IOException
    {
	this.privateOut = new PipedOutputStream();
	this.globalOut  = new PipedOutputStream()
	        {
		    /**
		     * Whether the an transmission has already started, without having finished
		     */
		    private boolean started = false;
		    
		    
		    
		    /**
		     * {@inheritDoc}
		     */
		    @Override
		    public void write(final byte[] data, final int off, final int len) throws IOException
		    {
			//* This is important *//
			for (int i = off, n = off + len; i < n; i++)
			    write((int)(data[i]) < 0 ? (256 + (int)(data[i])) : (int)(data[i]));
		    }
		    
		    /**
		     * {@inheritDoc}
		     */
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
		    
		    /**
		     * {@inheritDoc}
		     */			
		    @Override
		    public void flush() throws IOException
		    {
			if (this.started == false)
			    super.write(10);
			this.started = false;
			super.write(0);
			super.flush();
		    }
	        };
	
	this.globalIn  = new PipedInputStream(privateOut);
	this.privateIn = new PipedInputStream(globalOut);
    }
    
    
    /**
     * Starts all threads needed for data transfers
     * 
     * @param  sock     The connected socket
     * @param  monitor  Peer update monitor, it is shared for all invocation of the method by this instance of this class
     * @param  peers    Reference array (singleton array) with all peers
     * 
     * @throws  IOException  Throws if you are experiencing problems with getting your socket's I/O streams
     */
    private void connect(final Socket sock, final Object monitor, final String[] peers) throws IOException
    {
	final InputStream in = sock.getInputStream();
	final OutputStream out = sock.getOutputStream();
	final Object mutex = new Object();
	
	final Thread threadIn = new Thread()
	        {
		    /**
		     * {@inheritDoc}
		     */
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
		    /**
		     * {@inheritDoc}
		     */
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
		    /**
		     * {@inheritDoc}
		     */
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
				    while ((p = privateIn.read()) != -1)
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
	
	threadIn.start();      // Socket input reading thread
	threadSysOut.start();  // Peer update socket output thread
	threadMsgOut.start();  // Message sending socket outout and reading pipe input thread
    }
    
}

