/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.network;

import java.util.*;
import java.io.*;


/**
 * Object networking layer
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class ObjectNetworking
{
    /**
     * Constructor
     * 
     * @param  input   Stream for reading from the network
     * @param  output  Stream for writing to the network
     *
     * @throws  IOException  Thrown if the program fails to set up object input/output network streaming (unlikely)
     */
    public ObjectNetworking(final InputStream input, final OutputStream output) throws IOException
    {
	this.output = new ObjectOutputStream(new BufferedOutputStream(output));
	this.output.flush(); //Program freezes otherwise
	this.input = input;
	
	synchronized (alternatives)
	{
	    alternatives.put(input, new ArrayDeque<Serializable>());
	}
	
	addAlternative(input, input);
    }
    
    
    
    /**
     * Stream for reading from the network
     */
    private final InputStream input;
    
    /**
     * Stream for writing to the network
     */
    private final ObjectOutputStream output;
    
    private static final HashMap<InputStream, ArrayDeque<Serializable>> alternatives = new HashMap<InputStream, ArrayDeque<Serializable>>();
    
    
    
    /**
     * Sends an object
     *
     * @throws  IOException  Thrown if the program fails to send the message
     */
    public synchronized void send(final Serializable object) throws IOException
    {
	this.output.writeObject(object);
	this.output.flush();
    }
    
    /**
     * Sends an object, but does to perform back-referencing
     *
     * @throws  IOException  Thrown if the program fails to send the message
     */
    public synchronized void sendUnique(final Serializable object) throws IOException
    {
	this.output.writeUnshared(object);
	this.output.flush();
    }
    
    /**
     * Receive an object
     *
     * @throws  ClassNotFoundException  Thrown if the received object is not a part of the program
     */
    public Serializable receive() throws ClassNotFoundException
    {
	final ArrayDeque<Serializable> queue;
	synchronized (alternatives)
        {
	    queue = alternatives.get(this.input);
	}
	final Serializable object;
	synchronized (queue)
	{
	    if (queue.isEmpty())
		try
		{
		    queue.wait();
		}
		catch (final InterruptedException err)
		{
		    return null;
		}
	     object = queue.pollLast();
	}
	if (object instanceof ClassNotFoundException)
	    throw (ClassNotFoundException)object;
	if (object instanceof Throwable)
	    throw new Error((Throwable)object);
	return object;
    }
    
    public static void addAlternative(final InputStream in, final InputStream alt) throws IOException
    {
	final ArrayDeque<Serializable> queue;
	synchronized (alternatives)
	{
	    queue = alternatives.get(in);
	}
	
	final ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(alt));
	
	for (;;)
	{
	    Serializable object;
	    try
	    {
		object = (Serializable)(input.readObject());
		if (object instanceof Throwable) //TODO: kick him
		    continue;
	    }
	    catch (final ClassNotFoundException err)
	    {
		object = err;
	    }
	    synchronized (queue)
	    {
		queue.offerLast(object);
		queue.notifyAll();
	    }
	}
    }
    
}

