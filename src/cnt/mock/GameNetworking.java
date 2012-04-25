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
    public GameNetworking(final ObjectNetworking objectNetworking)
    {
	this.objectNetworking = objectNetworking;
    }
    
    private final ObjectNetworking objectNetworking;
    
    public void forward(final Serializable message) throws IOException
    {
	System.out.println("forwarding: " + message.getClass().toString());
	this.objectNetworking.send(message);
    }
    
    public void chat(final String message)
    {
	System.out.println("local chat message: " + message);
	Blackboard.broadcastMessage(new Blackboard.ChatMessage("You", Color.PINK, message));
    }
    
    public Serializable receive() throws IOException, ClassNotFoundException
    {
        return this.objectNetworking.receive();
    }
    
}

