/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.mock;
import cnt.mock.PipedInputStream;
import cnt.mock.PipedOutputStream;
import cnt.game.*;
import cnt.messages.*;
import cnt.*;

import java.io.*;
import java.net.*;
import java.util.*;


/**
 * Connection networking layer mock object
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class ConnectionNetworking import Blackboard.BlackboardOberserver
{
    /**
     * Constructor
     * 
     * @param  name        The name of the peer
     * @param  server      Whether the peer should be the network server
     * @param  pubip       (unused)
     * @param  serverport  The port the servers use
     * @param  serverhost  The server host
     */
    public ConnectionNetworking(final char name, final boolean server, final String pubip, final int serverport, final String serverhost) throws IOException
    {
	System.err.println("I am " + name);
	Blackboard.registerObserver(this);
	createPipes();
	
	
	if (server)
	{
	    final ServerSocket sock = new ServerSocket(serverhost, serverport);
	    final Thread socketThread = new Thread()
	            {
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void run()
			{
			    for (;;)
			    {
				final ArrayDeque<byte[]> inQueue = new ArrayDeque<byte[]>();
				final ArrayDeque<byte[]> outQueue = new ArrayDeque<byte[]>();
				connectServer(sock.accept());
				handleQueues(inQueue, outQueue);
			    }
			}
	            };
	    
	    socketThread.setDaemon(true);
	    socketThread.start();
	}
 	else
        {
	    final ArrayDeque<byte[]> inQueue = new ArrayDeque<byte[]>();
	    final ArrayDeque<byte[]> outQueue = new ArrayDeque<byte[]>();
	    connectClient(new Socket(serverhost, serverport), inQueue, outQueue);
	    handleQueues(inQueue, outQueue);
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
     * All joined players
     */
    public HashSet<Player> joinedPlayers;
    
    /**
     * The local player
     */
    public Player localPlayer;
    
    
    
    /**
     * {@inheritDoc}
     */
    public void messageBroadcasted(final Blackboard.BlackboardMessage message)
    {
	if (message instanceof LocalPlayer)
        {   this.localPlayer = ((LocalPlayer)message).player;
	}
	else if (message instanceof PlayerJoined)
	{   this.joinedPlayers.add(((PlayerJoined)message).player);
	}
	else if (message instanceof PlayerDropped)
	{   this.joinedPlayers.remove(((PlayerDropped)message).player);
	}
    }
    
    
    /**
     * Creates input and output pipes
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
     * Handles system messaging
     * 
     * @param  inQueue   Received system messages
     * @param  outQueue  Enqueued system messages
     */
    private void handleQueues(final ArrayDeque<byte[]> inQueue, final ArrayDeque<byte[]> outQueue)
    {
	final Thread threadIn = new Thread()
	        {
		    /**
		     * {@inheritDoc}
		     */
		    @Override
		    public void run()
		    {
		    }
	        };

	
	final Thread threadOut = new Thread()
	        {
		    /**
		     * {@inheritDoc}
		     */
                    @Override
		    public void run()
		    {
		    }
	        };
        
	
        threadIn.setDaemon(true);
        threadOut.setDaemon(true);
	
        threadIn.start();
        threadOut.start();
    }
    
    
    /**
     * Connects a client to a server
     * 
     * @param  socket    The connect Internet socket
     * @param  inQueue   Received system messages
     * @param  outQueue  Enqueued system messages
     */
    private void connectClient(final Socket socket, final ArrayDeque<byte[]> inQueue, final ArrayDeque<byte[]> outQueue)
    {
        final InputStream in = socket.getInputStream();
        final OutputStream out = socket.getOutputStream();
	final Object mutex = new Object();
	
	
	final Thread threadIn = new Thread()
	        {
		    /**
		     * {@inheritDoc}
		     */
                    @Override
		    public void run()
		    {
			byte[] buf = new byte[128];
			
                        try
			{
			    for (;;)
			    {
				int ptr = 0;
				for (int d; (d = in.read()) != '\n';)
				{
				    if (ptr == buf.length)
				    {
					final byte[] nbuf = new byte[ptr + 128];
					System.arraycopy(buf, 0, nbuf, 0, ptr);
					buf = nbuf;
				    }
				    buf[ptr++] = (byte)d;
				}
                                
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
				else if (ptr > 0)
				{
				    final int off = msg[0] == '+' ? 1 : 0;
				    final byte[] msg = new byte[ptr];
				    System.arraycopy(buf, off, msg, 0, ptr - off);
				    synchronized (inQueue)
				    {
					if (off == 0)
					    inQueue.offerFirst(msg);
					else
					    inQueue.offerLast(msg);
					inQueue.notifyAll();
				    }
				}
			    }
			}
                        catch (final Exception err)
			{
			    Blackboard.broadcastMessage(new PlayerDropped(ConnectionNetworking.this.localPlayer));
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
			    for (;;)
				synchronized (outQueue)
				{
				    if (outQueue.isEmpty())
					outQueue.wait();
				    final byte[] data = outQueue.pollFirst();
				    synchronized (mutex)
				    {
					out.write(data);
					out.write('\n');
					out.flush();
				    }
				}
			}
                        catch (final Throwable err)
		        {
			    Blackboard.broadcastMessage(new PlayerDropped(ConnectionNetworking.this.localPlayer));
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
                        catch (final Throwable err)
			{
			    Blackboard.broadcastMessage(new PlayerDropped(ConnectionNetworking.this.localPlayer));
			}
		    }
	        };
        
	
        threadIn.setDaemon(true);
        threadSysOut.setDaemon(true);
        threadMsgOut.setDaemon(true);
	
        threadIn.start();
        threadSysOut.start();
        threadMsgOut.start();
    }
    
}

