/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.messages;
import cnt.game.enginehelp.*;
import cnt.*;


/**
 * Full game engine update message
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public final class EngineUpdate implements Blackboard.BlackboardMessage
{
    /**
     * Compatibility versioning for {@link java.io.Serializable}
     */
    private static final long serialVersionUID = 1L;
    
    
    
    /**
     * Constructor
     * 
     * @param  data  Engine data
     */
    public EngineUpdate(final EngineData data)
    {
	this.data = data;
    }
    
    
    
    /**
     * Engine data
     */
    public final EngineData data;
    
    
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
	return "Full engine update";
    }
	
    /**
     * {@inheritDoc}
     */
    public Boolean checkIntegrity()
    {
	return null;
    }
	
}
