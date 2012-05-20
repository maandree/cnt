/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.messages;
import cnt.game.*;
import cnt.*;

import java.io.Serializable;


/**
 * This message is broadcasted when the next player is request, and as a response
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public final class NextPlayer implements Blackboard.BlackboardMessage
{
    /**
     * Compatibility versioning for {@link Serializable}
     */
    private static final long serialVersionUID = 1L;
	
	
	
    /**
     * Constructor
     * 
     * @param  player  The player, <code>null</code> if requested
     */
    public NextPlayer(final Player player)
    {
	this.player = player;
    }
	
	
	
    /**
     * The player, <code>null</code> if requested
     */
    public final Player player;
	
	
	
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
	return this.player == null ? "Request for next player" : ("Next player: " + this.player.toString());
    }
	
    /**
     * {@inheritDoc}
     */
    public Boolean checkIntegrity()
    {
	return Boolean.TRUE;
    }
}
