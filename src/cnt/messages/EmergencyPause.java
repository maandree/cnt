/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.messages;
import cnt.*;


/**
 * Pause (unpause) triggered in case of emegency, such as networking problems
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public final class EmergencyPause implements Blackboard.BlackboardMessage
{
    /**
     * Compatibility versioning for {@link java.io.Serializable}
     */
    private static final long serialVersionUID = 1L;
	
	
	
    /**
     * Constructor
     * 
     * @param  paused  <code>true</code> if paused is triggered, otherwise, if unpaused is triggered, <code>false</code>
     */
    public EmergencyPause(final boolean paused)
    {
	this.paused = paused;
    }
    
    
    
    /**
     * <code>true</code> if the player paused, otherwise, if unpaused, <code>false</code>
     */
    public final boolean paused;
    
    
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
	return "Emergency" + (this.paused ? " pause" : " unpause");
    }
	
    /**
     * {@inheritDoc}
     */
    public Boolean checkIntegrity()
    {
	return Boolean.TRUE;
    }
}
