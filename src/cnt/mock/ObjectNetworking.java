/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.mock;

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
	this.input  = new ObjectInputStream(new BufferedInputStream(input));
    }
    
    
    
    /**
     * Stream for reading from the network
     */
    private final ObjectInputStream input;
    
    /**
     * Stream for writing to the network
     */
    private final ObjectOutputStream output;
    
    
    
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
     * Sends an object, but does to perform back-referencing
     *
     * @throws  IOException             Thrown if the program fails to receive data
     * @throws  ClassNotFoundException  Thrown if the received object is not a part of the program
     */
    public Serializable receive() throws IOException, ClassNotFoundException
    {
	return (Serializable)(this.input.readObject());
    }
    
}

