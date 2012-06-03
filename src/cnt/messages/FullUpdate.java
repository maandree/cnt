/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.messages;
import cnt.*;

import java.util.HashMap;
import java.io.Serializable;


/**
 * Full system update message
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public final class FullUpdate implements Blackboard.BlackboardMessage
{
    /**
     * Compatibility versioning for {@link Serializable}
     */
    private static final long serialVersionUID = 1L;
    
    
    
    /**
     * Constructor
     */
    public FullUpdate()
    {
	this.gathering = true;
	this.data = new HashMap<Class<?>, Object>();
    }
    
    /**
     * Constructor
     * 
     * @param  data  Full system update data
     */
    private FullUpdate(final HashMap<Class<?>, Object> data)
    {
	this.gathering = false;
	this.data = data;
    }
    
    
    
    /**
     * Whether data is being gathered
     */
    private final boolean gathering;
    
    /**
     * Full system update data
     */
    public final HashMap<Class<?>, Object> data;
    
    
    
    /**
     * Creates a {@link FullUpdate} message that can be unicasted or broadcasted over the networking
     * 
     * @return  A {@link FullUpdate} message that can be unicasted or broadcasted over the networking
     */
    public FullUpdate getDistributable()
    {
	if (this.gathering == false)
	    throw new IllegalStateException();
	
	return new FullUpdate(this.data);
    }
    
    
    /**
     * Gets whether data is being gathered
     * 
     * @return  Whether data is being gathered
     */
    public boolean isGathering()
    {
	return this.gathering;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
	return (this.gathering ? "Gathering" : "Retreived") + " full updata data";
    }
	
    /**
     * {@inheritDoc}
     */
    public Boolean checkIntegrity()
    {
	return this.gathering ? Boolean.FALSE : null;
    }
	
}
