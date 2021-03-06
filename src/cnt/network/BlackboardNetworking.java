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
 * @author  Calle Lejdbrandt, <a href="mailto:callel@kth.se">callel@kth.se</a>
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class BlackboardNetworking implements Blackboard.BlackboardObserver
{
    //Has default constructor
    
    
    
    /**
     * The next layer the networking stack
     */
    public GameNetworking gameNetworking;
    
    /**
     * Blackboard message to ignore (with how many times) to prevent an infinite resonance loop
     */
    private final HashMap<Blackboard.BlackboardMessage, Integer> ignore = new HashMap<Blackboard.BlackboardMessage, Integer>();
    
    
    
    /**
     * Set {@link GameNetworking} instance to use
     */
    public void setGameNetworking(GameNetworking gameNetworking)
    {
	this.gameNetworking = gameNetworking;
	Blackboard.registerObserver(this);
    }
    
    
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
	    if      (message instanceof ChatMessage)        this.gameNetworking.forward(message);
	    else if (message instanceof EmergencyPause)     this.gameNetworking.forward(message);
	    else if (message instanceof EngineShapeUpdate)  this.gameNetworking.forward(message);
	    else if (message instanceof EngineUpdate)       this.gameNetworking.forward(message);
	    else if (message instanceof FullUpdate)         ; /* Do nothing */
	    else if (message instanceof GameOver)           this.gameNetworking.forward(message);
	    else if (message instanceof GamePlayCommand)    ; /* Do nothing */
	    else if (message instanceof GameScore)          this.gameNetworking.forward(message);
	    else if (message instanceof JoinGame)           this.gameNetworking.forward(message);
	    else if (message instanceof MatrixPatch)        this.gameNetworking.forward(message);
	    else if (message instanceof PlayerDropped)      this.gameNetworking.forward(message);
	    else if (message instanceof PlayerJoined)       this.gameNetworking.forward(message);
	    else if (message instanceof PlayerPause)        this.gameNetworking.forward(message);
	    else if (message instanceof SystemMessage)       ; /* Do nothing */
	    else if (message instanceof LocalPlayer)
	    {
		LocalPlayer msg = (LocalPlayer)message;
		this.gameNetworking.setLocalPlayer(msg.player);
	    }
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
     */
    public void receiveAndBroadcast(final Serializable object) 
    {
	    broadcastMessage((Blackboard.BlackboardMessage)object);
    }
    
    /**
     * Broadcasts a message
     * 
     * @param  message  The message to broadcast
     */
    protected void broadcastMessage(final Blackboard.BlackboardMessage message)
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

