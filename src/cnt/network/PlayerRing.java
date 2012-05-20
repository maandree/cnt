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
     * All players sorted by colour
     */
    private Player[] colourSorted = new Player[8];
    
    /**
     * The local player
     */
    private Player localPlayer = null;
    
    /**
     * The number of players
     */
    private int playerCount = 0;
    
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
		    return p.getColor() - q.getColor();
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
	    if (this.playerCount == this.colourSorted.length)
	    {
		final Player[] tmp = new Player[this.playerCount + 8];
		System.arraycopy(this.colourSorted, 0, tmp, 0, this.playerCount);
		this.colourSorted = tmp;
	    }
	    
	    int pos = Arrays.binarySearch(this.colourSorted, 0, this.playerCount, player, this.comparator);
	    if (pos >= 0)
	    {
		System.out.println("\033[0;1;31mID confliction\033[0m");
		//FIXME: ID confliction
	    }
	    System.arraycopy(this.colourSorted, ~pos, this.colourSorted, -pos, this.playerCount - ~pos); //safe for atleast sun-java 5,6 and openjdk 7
	    this.colourSorted[~pos] = player;
	    
	    if ((++this.playerCount >= 2) && (this.colourSorted[0].equals(this.localPlayer)))
		Blackboard.broadcastMessage(new PlayerOrder(this.ring));
	}
	else if (message instanceof PlayerDropped)
	{
	    final Player player = ((PlayerDropped)message).player;
	    if (this.ring.contains(player) == false)
		return;
	    this.ring.remove(this.ring.find(player));
	    
	    int pos = Arrays.binarySearch(this.colourSorted, 0, this.playerCount, player, this.comparator);
	    System.arraycopy(this.colourSorted, pos + 1, this.colourSorted, pos, this.playerCount - pos - 1);
	    
	    this.playerCount--;
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
	    int pc = 0;
	    for (final Player player : this.ring)
	    {
		if (Arrays.binarySearch(this.colourSorted, 0, this.playerCount, player, this.comparator) < 0)
		    Blackboard.broadcastMessage(new PlayerJoined(player));
		pc++;
	    }
	    this.playerCount = pc;
	    this.colourSorted = new Player[this.playerCount / 8 * 8 + 8];
	    int ptr = 0;
	    for (final Player player : this.ring)
		this.colourSorted[ptr++] = player;
	    Arrays.sort(this.colourSorted, 0, this.playerCount, this.comparator);
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

