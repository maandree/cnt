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
    private ACDLinkedList<Player> ring = new ACDLinkedList<Player>();
    
    /**
     * The local player
     */
    private Player localPlayer = null;
    
    
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public synchronized void messageBroadcasted(final Blackboard.BlackboardMessage message)
    {
	if (message instanceof PlayerJoined)
        {
	    System.err.println("\033[35m" + this.ring.toString() + "\033[0m");
	    final Player player = ((PlayerJoined)message).player;
	    if (this.ring.contains(player))
		return;
	    this.ring.insertBefore(player);
	    System.err.println("\033[1;35m" + this.ring.toString() + "\033[0m");
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
		this.ring.next();
		Blackboard.broadcastMessage(new NextPlayer(this.ring.get()));
	    }
	    else
		this.ring.jump(this.ring.find(player));
	}
	else if (message instanceof LocalPlayer)
	{
	    final Player player = ((LocalPlayer)message).player;
	    this.localPlayer = player;
	    if (this.ring.contains(player))
		return;
	    this.ring.insertBefore(player);
	}
	else if (message instanceof FullUpdate)
	{
	    final FullUpdate update = (FullUpdate)message;
	    if (update.isGathering())
		update.data.put(PlayerRing.class, this.ring);
	    else
		this.ring = (ACDLinkedList<Player>)(update.data.get(PlayerRing.class));
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

