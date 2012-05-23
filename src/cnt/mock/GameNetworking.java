/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.mock;
import cnt.game.*;
import cnt.messages.*;
import cnt.*;

import java.awt.*;
import java.io.*;


/**
 * Game networking layer object
 *
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
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
     * The local player
     */
    private Player player = null;
    
    
    
    /**
     * Forward a message to the next layer in the protocol stack
     * 
     * @param  message  The message
     * 
     * @throws  IOException  On networking exception
     */
    public void forward(final Serializable message) throws IOException
    {
	this.objectNetworking.send(message);
    }
    
    
    /**
     * Invoke if the local user is sending a chat message
     * 
     * @param  message  The message
     */
    public void chat(final String message)
    {
	if (this.player != null)
	    Blackboard.broadcastMessage(new ChatMessage(this.player, message));
    }
    
    
    /**
     * Sets local player
     * 
     * @param  player  The local player
     */
    public void setLocalPlayer(final Player player)
    {
	this.player = player;
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

