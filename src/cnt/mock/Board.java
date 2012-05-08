/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.mock;
import cnt.game.*;


/**
 * Game board with all stationed shapes
 */
public class Board
{
    /**
     * The width of the board
     */
    public static final int WIDTH = 10;
    
    /**
     * The height of the board
     */
    public static final int HEIGHT = 20;
    
    
    
    /**
     * Constructor
     */
    public Board()
    {
    }
    
    
    
    /**
     * Puts a shape on the board
     * 
     * @param  shape  The shape
     */
    public void put(final Shape shape)
    {
    }
    
    /**
     * Puts blocks on the board
     * 
     * @param  blocks  The blocks
     * @param  offX    Offset on the x-axis on the board
     * @param  offY    Offset on theyx-axis on the board
     */
    public void put(final Block[][] blocks, final int offX, final int offY)
    {
    }
    
    /**
     * Generates an array of the indices (y-position) of all full rows
     * 
     * @return  An array of the indices (y-position) of all full rows
     */
    public int[] getFullRows()
    {
	return null;
    }
    
    /**
     * Deletes blocks from the board
     * 
     * @param  deleteMatrix  Pattern to remove blocks in
     * @param  offX          The offset of the matrix <tt>deleteMatrix</tt> on the X-axis
     * @param  offY          The offset of the matrix <tt>deleteMatrix</tt> on the Y-axis
     */
    public void delete(final boolean[][] deleteMatrix, final int offX, final int offY)
    {
    }
    
    /**
     * Gets a matrix with all shapes on the board
     * 
     * @return  A matrix with all shapes on the board
     */
    public Block[][] getMatrix()
    {
	return null;
    }
    
    /**
     * Tests whether a shape can be put on the board
     * 
     * @param   shape        The shape
     * @param   ignoreEdges  Whether to allow blocks outside the edges
     * @return               <code>true</code> if the shape fits on the board, otherwise <code>false</code>
     */
    public boolean canPut(final Shape shape, final boolean ignoreEdges)
    {
	return false;
    }
    
}
