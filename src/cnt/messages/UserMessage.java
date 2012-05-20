/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.messages;
import cnt.*;

import java.io.Serializable;


/**
 * Chat message sent by the local client's player
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public final class UserMessage implements Blackboard.BlackboardMessage
{
    /**
     * Compatibility versioning for {@link Serializable}
     */
    private static final long serialVersionUID = 1L;
	
	
	
    /**
     * Constructor
     * 
     * @param  message  The message
     */
    public UserMessage(final String message)
    {
	assert message != null : "Null is not allowed";
	this.message = message;
    }
	
	
	
    /**
     * The message
     */
    public final String message;
	
	
	
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
	return this.message == null ? "[no message, that's weird]" : ("Message from local player: " + this.message);
    }
	
    /**
     * {@inheritDoc}
     */
    public Boolean checkIntegrity()
    {
	return false; //For machine local use only
    }
}
