/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.messages;
import cnt.util.*;
import cnt.game.*;
import cnt.*;

import java.io.Serializable;


/**
 * Message to synchronise and information about player order
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public final class PlayerOrder implements Blackboard.BlackboardMessage
{
    /**
     * Compatibility versioning for {@link Serializable}
     */
    private static final long serialVersionUID = 1L;
    
    
    
    /**
     * Constructor
     * 
     * @param  order  A list with the order of the players
     */
    public PlayerOrder(final ACDLinkedList<Player> order)
    {
	this.order = order;
    }
    
    
    
    /**
     * A list with the order of the players
     */
    public final ACDLinkedList<Player> order;
    
    
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
	return this.order == null ? "[no list, that's wierd]" : this.order.toString();
    }
    
    /**
     * {@inheritDoc}
     */
    public Boolean checkIntegrity()
    {
	return null;
    }
    
}

