/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.network;
import cnt.util.*;
import cnt.game.*;
import cnt.messages.*;
import cnt.*;


/**
 * The ring with all players
 *
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class PlayerRing implements Blackboard.BlackboardObserver
{
    /**
     * Constructor
     */
    public PlayerRing()
    {
	Blackboard.registerObserver(this);
    }
    
    
    
    /**
     * Ring with all players
     */
    private final ACDLinkedList<Player> ring = new ACDLinkedList<Player>();
    
    
    
    /**
     * {@inheritDoc}
     */
    public void messageBroadcasted(final Blackboard.BlackboardMessage message)
    {
	if (message instanceof PlayerJoined)
        {
	    final Player player = ((PlayerJoined)message).player;
	    this.ring.insertBefore(player);
	}
	else if (message instanceof PlayerDropped)
	{
	    final Player player = ((PlayerDropped)message).player;
	    this.ring.remove(this.ring.find(player));
	}
	else if (message instanceof NextPlayer)
	{
	    final Player player = ((NextPlayer)message).player;
	    if (player == null)
	    {
		Blackboard.broadcastMessage(new NextPlayer(this.ring.get()));
		this.ring.next();
	    }
	}
    }
    
    
    /**
     * Stops listening for player updates
     */
    public void stop()
    {
	Blackboard.unregisterObserver(this);
    }

}

