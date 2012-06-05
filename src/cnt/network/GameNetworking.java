/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.network;
import cnt.game.*;
import cnt.messages.*;
import cnt.*;

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
     * @param  blackboardNetworking  The previous layer in the protocol stack
     */
    public GameNetworking(final BlackboardNetworking blackboardNetworking)
    { }
    
    
    
    /**
     * The previous layer in the protocol stack
     */
    public BlackboardNetworking blackboardNetworking;
    
    /**
     * The next layer in the protocol stack
     */
    public ConnectionNetworking connectionNetworking;

    /**
     * The local player
     */
    private Player player = null;
    
    /**
     * Set BlackboardNetworking instance to use
     */
    public void setBlackboardNetworking(BlackboardNetworking blackboardNetworking)
    {
	    this.blackboardNetworking = blackboardNetworking;
    }

    /**
     * Set ConnectionNetwroking instance to use
     */
    public void setConnectionNetworking(ConnectionNetworking connectionNetworking)
    {
	    this.connectionNetworking = connectionNetworking;
    }
    
    /**
     * Forward a message to the next layer in the protocol stack
     * 
     * @param  message  The message
     * 
     * @throws  IOException  On networking exception
     */
    public void forward(final Serializable message) throws IOException
    {
	this.connectionNetworking.send(PacketFactory.createBroadcast(message, false));
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
    public void receive(Serializable object)
    {
	try 
	{
	    if (object instanceof Blackboard.BlackboardMessage == false)
		return;
	    
	    if (((Blackboard.BlackboardMessage)object).checkIntegrity() == Boolean.FALSE)
		return;
	    
	    if (object instanceof FullUpdate)
	    {
		final FullUpdate update = (FullUpdate)object;
		if (update.isGathering() == false)
		{
		    for (final Player player : (Iterateable<Player>)(update.data.get(PlayerRing.class)))
			this.blackboardNetworking.receiveAndBroadcast(new PlayerJoined(player));
		}
	    }
	    
	    this.blackboardNetworking.receiveAndBroadcast(object);
	}
	catch (Exception err)
	{
	    return; 
	}
    }
    
}

