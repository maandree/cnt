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
 * Game over!
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public final class GameOver implements Blackboard.BlackboardMessage
{
    /**
     * Compatibility versioning for {@link java.io.Serializable}
     */
    private static final long serialVersionUID = 1L;
	
	
	
    //Has default constructor
	
	
	
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
	return "Game over!";
    }
	
    /**
     * {@inheritDoc}
     */
    public Boolean checkIntegrity()
    {
	return Boolean.TRUE;
    }
	
}
