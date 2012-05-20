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
 * System message class
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public final class SystemMessage implements Blackboard.BlackboardMessage
{
    /**
     * Compatibility versioning for {@link Serializable}
     */
    private static final long serialVersionUID = 1L;
	
	
	
    /**
     * Constructor
     * 
     * @param  player   The player associated with the message, <code>null</code> if none
     * @param  message  The message
     */
    public SystemMessage(final Player player, final String message)
    {
	assert message != null : "Null message is not allowed";
	this.player = player;
	this.message = message;
    }
	
	
	
    /**
     * The player associated with the message, <code>null</code> if none
     */
    public final Player player;
	
    /**
     * The message
     */
    public final String message;
	
	
	
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
	final String assoc = this.player == null ? "without associated player" : ("associated with " + this.player.toString());
	return this.message == null ? "[no message, that's weird]" : ("System message " + assoc + ": " + this.message);
    }
	
    /**
     * {@inheritDoc}
     */
    public Boolean checkIntegrity()
    {
	return false; //For machine local use only
    }
    
}
