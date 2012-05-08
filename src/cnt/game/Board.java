/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.game;


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
     * Creates the matrix blocks
     */
    private final Block[][] blocks = new Block[HEIGHT][WIDTH];
    
    
    
    // Has default constructor
    
    
    
    /**
     * Puts a shape on the board
     * 
     * @param  shape  The shape
     */
    public void put(final Shape shape)
    {
    	put(shape.getBlocks(), shape.getX(), shape.getY());
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
			
			if(newBlocks[y][x] !=null)
			    
			    blocks[y+offY][x+offX] = newBlocks[y][x];
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
    	System.arraycopy(found, 0, rc, ptr);
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
    	for (int y = 0; y < deleteBlocks.length; y++)
	    
	    if ((0 <= y + offY) && (y + offY < HEIGHT))
		
		for (int x = 0; x < deleteBlocks[y].length; x++)
		    
		    if ((0 <= x + offX) && (x + offX < WIDTH))
			
			if(deleteBlocks[y][x])
			    
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
	
    	for (int y = 0; y <HEIGHT; y++)
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
    	for (int y = 0; y < newBlocks.length; y++)
	
	    if ((0 <= y + offY) && (y + offY < HEIGHT))
	    {
		for (int x = 0; x < newBlocks[y].length; x++)
		
		    if ((0 <= x + offX) && (x + offX < WIDTH))
		    {
			if ((newBlocks[y][x] != null) && (blocks[y+offY][x+offX] != null))
			    
			    return false;
		    }
		    else if (ignoreEdges == false)
			return false;
	    }
	    else if (ignoreEdges == false)
		return false;
	
	return true;
    }
    
}

