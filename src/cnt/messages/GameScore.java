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
 * Game score update message
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public final class GameScore implements Blackboard.BlackboardMessage
{
    /**
     * Compatibility versioning for {@link Serializable}
     */
    private static final long serialVersionUID = 1L;
	
	
	
    /**
     * Constructor
     * 
     * @param  score  The new game score
     */
    public GameScore(final int score)
    {
	assert score >= 0 : "You can not have negative scores.";
	this.score = score;
    }
	
	
	
    /**
     * The new game score
     */
    public final int score;
	
	
	
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
	return Integer.toString(this.score) + " points";
    }
	
    /**
     * {@inheritDoc}
     */
    public Boolean checkIntegrity()
    {
	return Boolean.valueOf(score >= 0);
    }
	
}

