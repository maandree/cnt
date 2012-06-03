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

import java.util.Arrays;
import java.util.Comparator;


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
    private ACDLinkedList<Player> ring = new ACDLinkedList<Player>();
    
    /**
     * The local player
     */
    private Player localPlayer = null;
    
    /**
     * Player comparator, comparing by colour
     */
    private final Comparator<Player> comparator = new Comparator<Player>()
            {
		/**
		 * {@inheritDoc}
		 */
		public int compare(final Player p, final Player q)
		{
		    return p.getID() - q.getID();
		}
	    };
    
    
    
    /**
     * {@inheritDoc}
     */
    public synchronized void messageBroadcasted(final Blackboard.BlackboardMessage message)
    {
	if (message instanceof PlayerJoined)
        {
	    final Player player = ((PlayerJoined)message).player;
	    if (this.ring.contains(player))
		return;
	    this.ring.insertBefore(player);
	    if (player.equals(this.localPlayer) == false)
		Blackboard.broadcastMessage(new PlayerOrder(this.ring));
	}
	else if (message instanceof PlayerDropped)
	{
	    final Player player = ((PlayerDropped)message).player;
	    if (this.ring.contains(player) == false)
		return;
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
	    else
		this.ring.jump(this.ring.find(player));
	}
	else if (message instanceof LocalPlayer)
	{
	    final Player player = ((LocalPlayer)message).player;
	    this.localPlayer = player;
	}
	else if (message instanceof PlayerOrder)
	{
	    final ACDLinkedList<Player> newRing = ((PlayerOrder)message).order;
	    if (newRing == this.ring)
		return;
	    System.err.println("\033[1;32mGot new player ring\033[0m");
	    this.ring = newRing;
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

