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
public class ConnectionNetworking implements Blackboard.BlackboardObserver
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
	    this.outs = new Vector<OutputStream>();
	    final Vector<OutputStream> istreams = new Vector<OutputStream>();
	    mux(new BufferedInputStream(this.privateIn), istreams);
	    final Vector<ArrayDeque<byte[]>> outQueues = new Vector<ArrayDeque<byte[]>>();
	    final ServerSocket sock = new ServerSocket(serverport);
	    final Thread socketThread = new Thread()
	            {
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void run()
			{
			    for (;;)
				try
				{
				    final ArrayDeque<byte[]> inQueue = new ArrayDeque<byte[]>();
				    final ArrayDeque<byte[]> outQueue = new ArrayDeque<byte[]>();
				    synchronized (outQueues)
				    {
					outQueues.add(outQueue);
				    }
				    
				    
				    final PipedInputStream iin = new PipedInputStream();
				    final PipedOutputStream iout = new PipedOutputStream(iin);
				    synchronized (istreams)
				    {
					istreams.add(new BufferedOutputStream(iout));
				    }
				    
				    connectServer(sock.accept(), inQueue, outQueue, new BufferedInputStream(iin));
				    handleQueues(inQueue, outQueues);
				}
				catch (final Throwable err)
				{
				    if (ConnectionNetworking.this.localPlayer != null)
					Blackboard.broadcastMessage(new PlayerDropped(ConnectionNetworking.this.localPlayer));
				    return;
				}
			}
	            };
	    
	    socketThread.setDaemon(true);
	    socketThread.start();
	}
 	else
        {
	    this.outs = null;
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
    protected final HashSet<Player> joinedPlayers = new HashSet<Player>();
    
    /**
     * The local player
     */
    protected Player localPlayer = null;
    
    /**
     * Output streams for the server's clients
     */
    protected final Vector<OutputStream> outs;
    
    
    
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
			    write((int)(data[i]) & 255);
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
	final Thread thread = new Thread()
	        {
		    /**
		     * {@inheritDoc}
		     */
		    @Override
		    public void run()
		    {
			for (;;)
			{
			    final byte[] message;
			    synchronized (inQueue)
			    {
				if (inQueue.isEmpty())
				    try
				    {
					inQueue.wait();
				    }
				    catch (final InterruptedException err)
				    {
					return;
				    }
				message = inQueue.pollFirst();
			    }
			    final byte[] answer = ConnectionNetworking.this.getAnswer(message);
			    if (answer != null)
				synchronized (outQueue)
				{
				    outQueue.offerLast(answer);
				    outQueue.notifyAll();
				}
			}
		    }
	        };

	
        thread.setDaemon(true);
        thread.start();
    }
    
    
    /**
     * Handles system messaging
     * 
     * @param  inQueue s  Received system messages
     * @param  outQueues  Enqueued system messages
     */
    private void handleQueues(final ArrayDeque<byte[]> inQueue, final Vector<ArrayDeque<byte[]>> outQueues)
    {
	final Thread thread = new Thread()
	        {
		    /**
		     * {@inheritDoc}
		     */
		    @Override
		    public void run()
		    {
			for (;;)
			{
			    final byte[] message;
			    synchronized (inQueue)
			    {
				if (inQueue.isEmpty())
				    try
				    {
					inQueue.wait();
				    }
				    catch (final InterruptedException err)
				    {
					return;
				    }
				message = inQueue.pollFirst();
			    }
			    final byte[] answer = ConnectionNetworking.this.getAnswer(message);
			    if (answer != null)
				synchronized (outQueues)
				{
				    for (final ArrayDeque<byte[]> outQueue : outQueues)
					synchronized (outQueue)
					{
					    outQueue.offerLast(answer);
					    outQueue.notifyAll();
					}
				}
			}
		    }
	        };

	
        thread.setDaemon(true);
        thread.start();
    }
    
    
    
    /**
     * Multiplexes a stream
     * 
     * @param  origin   Stream to mux
     * @param  streams  mux ends
     */
    private void mux(final InputStream origin, final Vector<OutputStream> streams)
    {
	final Thread thread = new Thread()
	        {
		    /**
		     * {@inheritDoc}
		     */
		    @Override
		    public void run()
		    {
			final ArrayDeque<OutputStream> crashed = new ArrayDeque<OutputStream>();
			for (;;)
			{
			    final byte[] data;
			    int len;
			    synchronized (origin)
			    {
				try
				{
				    len = origin.available();
				    while (len == 0)
				    {
				        Thread.sleep(50);
					len = origin.available();
				    }
				    data = new byte[len];
				    len = origin.read(data, 0, len);
				}
				catch (final Throwable err)
				{
				    if (ConnectionNetworking.this.localPlayer != null)
					Blackboard.broadcastMessage(new PlayerDropped(ConnectionNetworking.this.localPlayer));
				    return;
				}
			    }
			    synchronized (streams)
			    {
				for (final OutputStream stream : streams)
				    try
				    {
					stream.write(data, 0, len);
					stream.flush();
				    }
				    catch (final Throwable err)
				    {
					crashed.offerLast(stream);
				    }
				
				OutputStream crash;
				while ((crash = crashed.pollLast()) != null)
				    streams.remove(crash);
			    }
			}
		    }
	        };

	
        thread.setDaemon(true);
        thread.start();
    }
    
    
    /**
     * Connects a server to a client
     * 
     * @param  socket    The connect Internet socket
     * @param  inQueue   Received system messages
     * @param  outQueue  Enqueued system messages
     * @param  cin       The clients mux:ed input stream
     * 
     * @thorws  IOException  On piping error
     */
    private void connectServer(final Socket socket, final ArrayDeque<byte[]> inQueue, final ArrayDeque<byte[]> outQueue, final InputStream cin) throws IOException
    {
	connect(socket, inQueue, outQueue, cin, this.privateOut, null);
    }
    
    
    /**
     * Connects a client to a server
     * 
     * @param  socket    The connect Internet socket
     * @param  inQueue   Received system messages
     * @param  outQueue  Enqueued system messages
     * 
     * @thorws  IOException  On piping error
     */
    private void connectClient(final Socket socket, final ArrayDeque<byte[]> inQueue, final ArrayDeque<byte[]> outQueue) throws IOException
    {
	connect(socket, inQueue, outQueue, this.privateIn, this.privateOut, this.localPlayer);
    }
    
    
    /**
     * Connects to a peer
     * 
     * @param  socket    The connect Internet socket
     * @param  inQueue   Received system messages
     * @param  outQueue  Enqueued system messages
     * @param  cin       The client's private message input stream
     * @param  cout      The client's private message output stream
     * @param  player    The player how droppes if the connection dies
     * 
     * @thorws  IOException  On piping error
     */
    private void connect(final Socket socket, final ArrayDeque<byte[]> inQueue, final ArrayDeque<byte[]> outQueue, final InputStream cin, final OutputStream cout, final Player player) throws IOException
    {
        final InputStream sin = socket.getInputStream();
        final OutputStream out = socket.getOutputStream();
	final Object mutex = new Object();
	final Player[] cplayer = { player };
	
	if (this.outs != null)
	    synchronized (this.outs)
	    {
		this.outs.add(out);
	    }
	
	final InputStream in;
	class MyInputStream extends InputStream
	{
	    /**
	     * Initialiser
	     */
	    {
		final Thread thread = new Thread()
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
					synchronized (MyInputStream.this)
					{
					    MyInputStream.this.wait();
					    synchronized (ConnectionNetworking.this.outs)
					    {
						for (final OutputStream os : ConnectionNetworking.this.outs)
						    if (os != out)
						    {
							os.write(MyInputStream.this.buf, 0, MyInputStream.this.ptr);
							os.flush();
						    }
					    }
					    MyInputStream.this.ptr = 0;
					}
				}
				catch (final Throwable err)
				{
				    System.err.println(err);
				    return;
				}
			    }
		        };
		thread.setDaemon(true);
		thread.start();
	    }
	    
	    
	    
	    /**
	     * Forward buffer
	     */
	    byte[] buf = new byte[128];
	    
	    /**
	     * Forward pointer
	     */
	    int ptr = 0;
	    
	    
	    
	    /**
	     * {@inheritDoc}
	     */
	    public int available() throws IOException
	    {
		return sin.available();
	    }
	    
	    /**
	     * {@inheritDoc}
	     */
	    public int read() throws IOException
	    {
		final int b = sin.read();
		synchronized (this)
		{
		    if (this.ptr == this.buf.length)
		    {
			final byte[] nbuf = new byte[this.ptr + 128];
			System.arraycopy(this.buf, 0, nbuf, 0, this.ptr);
			this.buf = nbuf;
		    }
		    this.buf[this.ptr++] = (byte)b;
		}
		return b;
	    }
	}
	in = this.outs == null ? sin : new MyInputStream();
	
	
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
                                
				if (ptr == 0)
			        {
				    for (int p; (p = in.read()) != 0;)
				    {
					if (p == 27)
					    p = in.read();
					cout.write(p);
				    }
				    cout.flush();
				}
				else if (ptr > 0)
				{
				    final int off = buf[0] == '+' ? 1 : 0;
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
				in.notify();
			    }
			}
                        catch (final Exception err)
			{
			    synchronized (cplayer)
			    {
				if (cplayer[0] == null)
				    return;
				Blackboard.broadcastMessage(new PlayerDropped(cplayer[0]));
				cplayer[0] = null;
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
			    for (;;)
				synchronized (outQueue)
				{
				    if (outQueue.isEmpty())
					outQueue.wait();
				    final byte[] data = outQueue.pollFirst();
				    if ((data != null) && (data.length > 0))
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
			    synchronized (cplayer)
			    {
				if (cplayer[0] == null)
				    return;
				Blackboard.broadcastMessage(new PlayerDropped(cplayer[0]));
				cplayer[0] = null;
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
				int p = cin.read();
				synchronized (mutex)
				{
				    out.write(p);
				    while ((p = cin.read()) != -1)
				    {
					out.write(p);
					if (p == 27)
					    out.write(cin.read());
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
			    synchronized (cplayer)
			    {
				if (cplayer[0] == null)
				    return;
				Blackboard.broadcastMessage(new PlayerDropped(cplayer[0]));
				cplayer[0] = null;
			    }
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
    
    
    /**
     * Handles a system message
     * 
     * @param   message  The message
     * @return           Return message, <code>null</code> if none
     */
    protected synchronized byte[] getAnswer(final byte[] message)
    {
	return null;
    }
    
}

