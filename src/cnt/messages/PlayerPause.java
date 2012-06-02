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


/**
 * Broadcasted when a player pauses or unpauses
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public final class PlayerPause implements Blackboard.BlackboardMessage
{
    /**
     * Compatibility versioning for {@link java.io.Serializable}
     */
    private static final long serialVersionUID = 1L;
	
	
	
    /**
     * Constructor
     * 
     * @param  player  The player
     * @param  paused  <code>true</code> if the player paused, otherwise, if unpaused, <code>false</code>
     */
    public PlayerPause(final Player player, final boolean paused)
    {
	assert player != null : "Their must be a player";
	this.player = player;
	this.paused = paused;
    }
    
    
    
    /**
     * The player
     */
    public final Player player;
    
    /**
     * <code>true</code> if the player paused, otherwise, if unpaused, <code>false</code>
     */
    public final boolean paused;
    
    
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
	return this.player + (this.paused ? " paused" : " unpaused");
    }
	
    /**
     * {@inheritDoc}
     */
    public Boolean checkIntegrity()
    {
	return this.player == null ? Boolean.FALSE : null;
    }
}
