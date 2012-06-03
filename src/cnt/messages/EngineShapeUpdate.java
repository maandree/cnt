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
 * Partial game engine update message with the falling shape
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public final class EngineShapeUpdate implements Blackboard.BlackboardMessage
{
    /**
     * Compatibility versioning for {@link java.io.Serializable}
     */
    private static final long serialVersionUID = 1L;
    
    
    
    /**
     * Constructor
     * 
     * @param  shape  The falling shape
     */
    public EngineShapeUpdate(final Shape shape)
    {
	this.shape = shape;
    }
    
    
    
    /**
     * The falling shape
     */
    public final Shape shape;
    
    
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
	return "Partial engine update: falling shape";
    }
	
    /**
     * {@inheritDoc}
     */
    public Boolean checkIntegrity()
    {
	return null;
    }
	
}
