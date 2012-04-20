/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright Ⓒ 2012  Mattias Andrée, Peyman Eshtiagh,
 *                   Calle Lejdbrandt, Magnus Lundberg
 *
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.network;

import java.io.*;


/**
 * Object networking layer
 *
 * @author  Mattias Andrée, <a href="maandree@kth.se">maandree@kth.se</a>
 */
public class ObjectNetworking
{
    /**
     * Constructor
     * 
     * @param  input   Stream for reading from the network
     * @param  output  Stream for writing to the network
     */
    public ObjectNetworking(final InputStream input, final OutputStream output) throws IOException
    {
	this.input  = new ObjectInputStream(input);
	this.output = new ObjectOutputStream(output);
    }
    
    
    
    /**
     * Stream for reading from the network
     */
    private final ObjectInputStream input;
    
    /**
     * Stream for writing to the network
     */
    private final ObjectOutputStream output;
    
    
    
    public void send(final Serializable object) throws IOException
    {
	this.output.writeObject(object);
	this.output.flush();
    }
    
    public void sendUnique(final Serializable object) throws IOException
    {
	this.output.writeUnshared(object);
	this.output.flush();
    }
    
    public Serializable receive() throws IOException, ClassNotFoundException
    {
	return (Serializable)(this.input.readObject());
    }
    
}

