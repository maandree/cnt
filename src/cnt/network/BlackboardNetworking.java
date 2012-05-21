/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.network;
import cnt.messages.*;
import cnt.*;
import cnt.game.Player;

import java.util.*;
import java.io.*;


/**
 * Blackboard networking layer
 *
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class BlackboardNetworking implements Blackboard.BlackboardObserver
{
    /**
     * Constructor
     * 
     */
    public BlackboardNetworking()
    {
	Blackboard.registerObserver(this);

	this.gameNetworking = new GameNetworking(this);
    }
    
    
    
    /**
     * The next layer the networking stack
     */
    private final GameNetworking gameNetworking;
    
    /**
     * Blackboard message to ignore (with how many times) to prevent an infinite resonance loop
     */
    private final HashMap<Blackboard.BlackboardMessage, Integer> ignore = new HashMap<Blackboard.BlackboardMessage, Integer>();
    
    
    
    /**
     * {@inheritDoc}
     */
    public void messageBroadcasted(final Blackboard.BlackboardMessage message)
    {
	synchronized (this.ignore)
	{
	    if (ignore.containsKey(message))
	    {
		final int count = this.ignore.get(message).intValue();
		    
		if (count == 1)  this.ignore.remove(message);
		else             this.ignore.put(message, new Integer(count - 1));
		    
		return;
	    }
	}
	
	try
	{
	    System.err.println("Potationally forwarding: " + message);
	    if      (message instanceof GamePlayCommand)  this.gameNetworking.forward(message);
	    else if (message instanceof MatrixPatch)      this.gameNetworking.forward(message);
	    else if (message instanceof ChatMessage)      this.gameNetworking.forward(message);
	    else if (message instanceof PlayerJoined)     this.gameNetworking.forward(message);
	    else if (message instanceof PlayerDropped)    this.gameNetworking.forward(message);
	    else if (message instanceof GameScore)        this.gameNetworking.forward(message);
	    else if (message instanceof GameOver)         this.gameNetworking.forward(message);
	    else if (message instanceof PlayerOrder)      this.gameNetworking.forward(message);
	    else if (message instanceof SystemMessage)    ; /* Do nothing */
	    else if (message instanceof NextPlayer)
	    {
		if (((NextPlayer)message).player != null)
		    this.gameNetworking.forward(message);
	    }
	    else if (message instanceof UserMessage)
	    {
		UserMessage msg = (UserMessage)message;
		this.gameNetworking.chat(msg.message);
	    }
	    else if (message instanceof LocalPlayer)
	    {
		LocalPlayer msg = (LocalPlayer)message;
		this.gameNetworking.setLocalPlayer(msg.player);
	    }
	    else
		assert false : "Update message types in BlackboardNetworking";
	}
	catch (final IOException err)
	{
	    err.printStackTrace(System.err);
	    //FIXME error!
	}
    }
    
    
    /**
     * Wait for, receive, and locally broadcast a message
     * 
     * @throws  IOException             On networking exception
     * @throws  ClassNotFoundException  In the message type is not a part of the program
     */
    public Integer receiveAndBroadcast(final Serializable object) throws IOException, ClassNotFoundException
    {
	System.err.println("Received forward: " + object);
	if (object instanceof Blackboard.BlackboardMessage)
	    broadcastMessage((Blackboard.BlackboardMessage)object);
            
	// First message should contain a Player object so we can get a color-number
	if (object instanceof PlayerJoined)
	{
	    PlayerJoined message = (PlayerJoined)object;
	    Player _player = message.player;
	    return new Integer(_player.getColor());
	}
	else
	    return null;	
    }
    
    /**
     * Broadcasts a message
     * 
     * @param  message  The message to broadcast
     * 
     * @throws  IOException             On networking exception
     * @throws  ClassNotFoundException  In the message type is not a part of the program
     */
    protected void broadcastMessage(final Blackboard.BlackboardMessage message) throws IOException, ClassNotFoundException
    {
	synchronized (this.ignore)
	{
	    Integer count = this.ignore.get(message);
	    
	    if (count == null)  count = new Integer(1);
	    else                count = new Integer(count.intValue() + 1);
	    
	    this.ignore.put(message, count);
	    
	    Blackboard.broadcastMessage(message);
	}
    }

}

