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
 * Command sent when the player whats to make a move
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public final class GamePlayCommand implements Blackboard.BlackboardMessage
{
    /**
     * Compatibility versioning for {@link java.io.Serializable}
     */
    private static final long serialVersionUID = 1L;
	
	
	
    /**
     * Constructor
     * 
     * @param  The move  The move
     */
    public GamePlayCommand(final Move move)
    {
	assert move != null : "Null is not allowed";
	this.move = move;
    }
	
	
	
    /**
     * The move
     */
    public final Move move;
	
	
	
    /**
     * The possible moves
     */
    public static enum Move
    {
	/**
	 * Move the block one step left
	 */
	LEFT ("Left (←)"),
	
	/**
	 * Move the block one step right
	 */
	RIGHT ("Right (→)"),
        
	/**
	 * Move the block one step down
	 */
	DOWN ("Down (↓)"),
	
	/**
	 * Drop the block all the way down
	 */
	DROP ("Drop (↡)"),
	
	/**
	 * Rotate the block 90° clockwise
	 */
	CLOCKWISE ("90° clockwise (↷)"),
	
	/**
	 * Rotate the block 90° anti-clockwise
	 */
        ANTICLOCKWISE ("90° anti-clockwise (↶)");
	
	
	
	/**
	 * Constructor
	 * 
	 * @param  String  Return value for {@link #toString()}
	 */
	Move(final String string)
	{
	    this.string = string;
	}
	
	
	
	/**
	 * Return value for {@link #toString()}
	 */
	private final String string;
	
	
	
	/**
	 * {@inheritDoc}
	 */
	public String toString()
	{
	    return string;
	}
    }
    
    
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
	return this.move == null ? "[no move, that's wierd]" : ("Move: " + this.move.toString());
    }
	
    /**
     * {@inheritDoc}
     */
    public Boolean checkIntegrity()
    {
	return Boolean.valueOf(this.move != null);
    }
}
