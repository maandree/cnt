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
     * @param  gameNetworking The privous layer in the network stack
     *
     */
    public ObjectNetworking(GameNetworking gameNetworking)
    {
	this.gameNetworking = gameNetworking;
	this.connectionNetworking = new ConnectionNetworking(this);
    }
    
    
    
    /**
     * The previous layer in the network stack
     */
    private final GameNetworking gameNetworking;

    /**
     * The next layer in the network stack
     */
    private final ConnectionNetworking connectionNetworking;
    
    
    
    /**
     * Sends an object
     *
     * @throws  IOException  Thrown if the program fails to send the message
     */
    public synchronized void send(final Serializable object) throws IOException
    {
	this.connectionNetworking.send(object);
    }
    
    /**
     * Sends an object, but does to perform back-referencing
     * Does the same thing as send()
     *
     * @throws  IOException  Thrown if the program fails to send the message
     */
    public synchronized void sendUnique(final Serializable object) throws IOException
    {
	this.connectionNetworking.send(object);
    }
    
    /**
     * Recive an object,
     *
     * @throws  IOException             Thrown if the program fails to receive data
     * @throws  ClassNotFoundException  Thrown if the received object is not a part of the program
     */
    public Integer receive(Serializable object)
    {
	return this.gameNetworking.receive(object);
    }
    
}

