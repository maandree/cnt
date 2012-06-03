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
 * Game matrix patch class
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class MatrixPatch implements Blackboard.BlackboardMessage
{
    /**
     * Compatibility versioning for {@link java.io.Serializable}
     */
    private static final long serialVersionUID = 1L;
    
    
    
    /**
     * Constructor
     * 
     * @param  erase   A matrix where <code>true</code> indicates removal of block
     * @param  blocks  A matrix where non-<code>null</code> indicates to add a block
     * @param  offY    Top offset, where the first row in the matrices affect the game matrix
     * @param  offX    Left offset, where the first column in the matrices affect the game matrix
     */
    public MatrixPatch(final boolean[][] erase, final Block[][] blocks, final int offY, final int offX)
    {
	this.erase = erase;
	this.blocks = blocks;
	this.offY = offY;
	this.offX = offX;
	
	assert ((erase != null) || (blocks != null)) : "Matrix patches must contain something";
	assert this.checkIntegrity() == Boolean.TRUE : "Matrix patch contains null rows";
    }
    
    
    
    /**
     * A matrix where <code>true</code> indicates removal of block
     */
    public final boolean[][] erase;
	
    /**
     * A matrix where non-<code>null</code> indicates to add a block
     */
    public final Block[][] blocks;
	
    /**
     * Top offset, where the first row in the matrices affect the game matrix
     */
    public final int offY;
	
    /**
     * Left offset, where the first column in the matrices affect the game matrix
     */
    public final int offX;
	
	
	
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
	return "Matrix patch!";
    }
	
    /**
     * {@inheritDoc}
     */
    public Boolean checkIntegrity()
    {
	if ((this.erase == null) && (this.blocks == null))
	    return Boolean.FALSE;
	    
	if (this.erase != null)
	    for (final boolean[] row : this.erase)
		if (row == null)
		    return Boolean.FALSE;
	
	if (this.blocks != null)
	    for (final Block[] row : this.blocks)
		if (row == null)
		    return Boolean.FALSE;
	    
	return Boolean.TRUE;
    }
}
