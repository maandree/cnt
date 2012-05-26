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
 * Join game message
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public final class JoinGame implements Blackboard.BlackboardMessage
{
    /**
     * Compatibility versioning for {@link Serializable}
     */
    private static final long serialVersionUID = 1L;
    
    
    
    /**
     * Constructor
     * 
     * @param  remote  The address to which to connect, <code>null</code> or empty if none
     */
    public JoinGame(final String remote)
    {
	this.remote = ((remote == null) || remote.isEmpty()) ? null : remote;
    }
    
    
    
    /**
     * The address to which to connect, <code>null</code> if none
     */
    public final String remote;
    
    
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
	return this.remote == null ? "New game cloud" : "Joining game cloud " + this.remote;
    }
    
    /**
     * {@inheritDoc}
     */
    public Boolean checkIntegrity()
    {
	return Boolean.FALSE;
    }
    
}
