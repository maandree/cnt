/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.game;

import java.io.Serializable;


/**
 * Game board with all stationed shapes
 * 
 * @author  Peyman Eshtiagh
 */
public class Board implements Serializable
{
    /**
     * Compatibility versioning for {@link Serializable}
     */
    private static final long serialVersionUID = 1L;
    
    
    
    /**
     * The width of the board
     */
    public static final int WIDTH = 10;
    
    /**
     * The height of the board
     */
    public static final int HEIGHT = 20;
    
    
    
    // Has default constructor
    
    
    
    /**
     * The game board matrix with a stationed blocks
     */
    private final Block[][] blocks = new Block[HEIGHT][WIDTH];
    
    
    
    /**
     * Puts a shape on the board
     * 
     * @param  shape  The shape
     */
    public void put(final Shape shape)
    {
    	put(shape.getBlockMatrix(), shape.getX(), shape.getY());
    }
    
    
    /**
     * Puts a set of blocks on the board
     * 
     * @param  newBlocks  A matrix with the new blocks
     * @param  offX       The offset of the matrix <tt>newBlocks</tt> on the X-axis
     * @param  offY       The offset of the matrix <tt>newBlocks</tt> on the Y-axis 
     */
    public void put(final Block[][] newBlocks, final int offX, final int offY)
    {
    	for (int y = 0; y < newBlocks.length; y++)
	
	    if ((0 <= y + offY) && (y + offY < HEIGHT))
	    
		for (int x = 0; x < newBlocks[y].length; x++)
		
		    if ((0 <= x + offX) && (x + offX < WIDTH))
			
			if (newBlocks[y][x] !=null)
			    
			    blocks[y+offY][x+offX] = newBlocks[y][x];
	
	System.err.println("\033[32mputting: block at " + newBlocks[0].length + "x" + newBlocks.length + "+" + offX + "+" + offY + "\033[0m");
    }
    
    
    /**
     * Tests if a row is full
     * 
     * @param   row  The row to test
     * @return       Whether the row is full
     */
    private boolean isFull(final Block[] row)
    {
	for (final Block block : row)
	    if (block == null)
		return false;
	return true;
    }
    
    
    /**
     * Generates an array of the indices (y-position) of all full rows
     * 
     * @return  An array of the indices (y-position) of all full rows
     */
    public int[] getFullRows()
    {
    	int[] found = new int[HEIGHT];
    	int ptr = 0;

    	for (int y = 0; y < HEIGHT; y++)
    	   if (isFull(blocks[y]))
    	       found[ptr++] = y;

    	int[] rc = new int[ptr];
    	System.arraycopy(found, 0, rc, 0, ptr);
    	return rc;
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
    	for (int y = 0; y < deleteMatrix.length; y++)

	    if ((0 <= y + offY) && (y + offY < HEIGHT))

		for (int x = 0; x < deleteMatrix[y].length; x++)

		    if ((0 <= x + offX) && (x + offX < WIDTH))

			if (deleteMatrix[y][x])

			    blocks[y+offY][x+offX] = null;
    }
    
    
    /**
     * Gets a matrix with all shapes on the board
     * 
     * @return  A matrix with all shapes on the board
     */
    public Block[][] getMatrix()
    {
	Block[][] clone = new Block[HEIGHT][WIDTH];
	
    	for (int y = 0; y < HEIGHT; y++)
	    System.arraycopy(blocks[y], 0, clone[y], 0, WIDTH);
	
	return clone;
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
	final Block[][] newBlocks = shape.getBlockMatrix();
	final int offX = shape.getX();
	final int offY = shape.getY();
	
	System.err.println("\033[34mtesting put: block at " + newBlocks[0].length + "x" + newBlocks.length + "+" + offX + "+" + offY + "\033[0m");
	boolean rc = false;
	
	try
	{
	    for (int y = 0; y < newBlocks.length; y++)

		for (int x = 0; x < newBlocks[y].length; x++)

		    if ((0 <= y + offY) && (y + offY < HEIGHT) && (0 <= x + offX) && (x + offX < WIDTH))
		    {
			if ((newBlocks[y][x] != null) && (blocks[y+offY][x+offX] != null))

			    return rc = false; //YES =, not ==
		    }
		    else if ((ignoreEdges == false) && (newBlocks[y][x] != null))

			return rc = false;
	    
	    return rc = true;
	}
	finally //post-return code ☺
	{
	    System.err.println(rc ? "\033[35mfree\033[0m" : "\033[31mcollision\033[0m");
	}
    }
    
}

