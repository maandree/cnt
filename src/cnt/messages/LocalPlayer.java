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
 * Message to inform the subsystems which player the local user is
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public final class LocalPlayer implements Blackboard.BlackboardMessage
{
    /**
     * Compatibility versioning for {@link Serializable}
     */
    private static final long serialVersionUID = 1L;
	
	
	
    /**
     * Constructor
     * 
     * @param  player  The player
     */
    public LocalPlayer(final Player player)
    {
	assert player != null : "Null is not allowed";
	this.player = player;
    }
	
	
	
    /**
     * The player
     */
    public final Player player;
	
	
	
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
	return this.player == null ? "[no player, that's wierd]" : ("Local player: " + this.player.toString());
    }
	
    /**
     * {@inheritDoc}
     */
    public Boolean checkIntegrity()
    {
	return false; //For machine local use only
    }
}
