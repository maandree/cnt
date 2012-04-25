/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright Ⓒ 2012  Mattias Andrée, Peyman Eshtiagh,
 *                   Calle Lejdbrandt, Magnus Lundberg
 *
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.mock;
import cnt.network.*;
import cnt.*;

import java.awt.*;
import java.io.*;


/**
 * Game networking layer mock object
 *
 * @author  Mattias Andrée, <a href="maandree@kth.se">maandree@kth.se</a>
 */
public class GameNetworking
{
    /**
     * Constructor
     * 
     * @param  objectNetworking  The next layer in the protocol stack
     */
    public GameNetworking(final ObjectNetworking objectNetworking)
    {
	this.objectNetworking = objectNetworking;
    }
    
    
    
    /**
     * The next layer in the protocol stack
     */
    private final ObjectNetworking objectNetworking;
    
    
    
    /**
     * Forward a message to the next layer in the protocol stack
     * 
     * @param  message  The message
     * 
     * @throws  IOException  On networking exception
     */
    public void forward(final Serializable message) throws IOException
    {
	System.out.println("forwarding: " + message.getClass().toString());
	this.objectNetworking.send(message);
    }
    
    
    /**
     * Invoke if the local user is sending a chat message
     * 
     * @param  message  The message
     */
    public void chat(final String message)
    {
	System.out.println("local chat message: " + message);
	Blackboard.broadcastMessage(new Blackboard.ChatMessage("You", Color.PINK, message));
    }
    
    
    /**
     * Wait for and receive a message
     * 
     * @return  The next message
     * 
     * @throws  IOException             On networking exception
     * @throws  ClassNotFoundException  If the message type is not a part of the program
     */
    public Serializable receive() throws IOException, ClassNotFoundException
    {
        return this.objectNetworking.receive();
    }
    
}

