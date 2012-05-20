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
 * Chat message class
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public final class ChatMessage implements Blackboard.BlackboardMessage
{
    /**
     * Compatibility versioning for {@link Serializable}
     */
    private static final long serialVersionUID = 1L;
	
	
	
    /**
     * Constructor
     * 
     * @param  player   The player sending the message
     * @param  message  The message
     */
    public ChatMessage(final Player player, final String message)
    {
	assert message != null : "Null message is not allowed";
	assert player != null : "Null player is not allowed";
	this.player = player;
	this.message = message;
    }
	
	
	
    /**
     * The player sending the message
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
	if ((this.player == null) && (this.message == null))
	    return "[no message from nobody, that's weired]";
	    
	if (this.player == null)
	    return "[message from nobody, that's weired]";
	    
	if (this.message == null)
	    return "[no message from somebody, that's weired]";
	    
	return "Message from " + this.player.toString() + ": " + this.message;
    }
	
    /**
     * {@inheritDoc}
     */
    public Boolean checkIntegrity()
    {
	return Boolean.valueOf((this.player != null) && (this.message != null));
    }
}
