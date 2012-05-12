/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.game;

// Added this for clarity
import cnt.game.Board;
import cnt.game.Block;

import java.io.*;


/**
* Shape class representing a shape of objects
* 
* @author  Calle Lejdbrandt, <a href="mailto:callel@kth.se">callel@kth.se</a>
* @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
*/
public abstract class Shape implements Cloneable, Serializable
{
    /**
     * The shape that we want
     */
    protected Block[][] shape;

    /**
     * Current X offset from top-left corner
     */
    protected int x = 0;

    /**
     * Current Y offset from top-left corner
     */
    protected int y = 0;

    /**
     * Player owning the shape
     */
    Player player = null;
    
    
    
    /**
     * Momento class for {@link Shape}
     */
    public static abstract class Momento
    {
        public Momento(final Shape shape)
        {
            this.shape = shape.shape;
            this.x = shape.x;
            this.y = shape.y;
        }
        
        
	/**
	 * The shape that we want
	 */
	private final Block[][] shape;

	/**
	 * Current X offset from top-left corner
	 */
	private final int x;

	/**
	 * Current Y offset from top-left corner
	 */
	private final int y;
    
    
        /**
         * Restores the shape's state
         * 
         * @param  Shape  The shape
         */
        public void restore(final Shape shape)
        {
            shape.shape = this.shape;
            shape.x = this.x;
            shape.y = this.y;
        }
    }
        
    
    
    /**
     * Returns the current Shape
     * 
     * @return The Shape object
     */
    public Shape getShape()
    {
	return this;
    }
	
    /**
     * returns the Blockmatrix that makes up a shape
     *
     * @return a Block[][] matrix that makes up the shape in the current position
     */
    public Block[][] getBlockMatrix()
    {
	return this.shape;
    }

    /**
     * returns a Booleanmatrix that makes up a shape
     *
     * @return a boolean[][] matrix that makes up the shape in the current position
     */
    public boolean[][] getBooleanMatrix()
    {
	boolean[][] matrix = new boolean[shape.length][shape[0].length];

	for (int col = 0; col < shape.length; col++)
	    for (int row = 0; row < shape[0].length; row++)
		if (shape[col][row] != null)
		    matrix[col][row] = true;
		
	return matrix;
    }
	
    /**
     * Return current left position of shape
     *
     * @return x the current left position for shape
     */
    public int getX()
    {
	return this.x;
    }
	
    /**
     * Return current top position
     *
     * @return y current top position
     */
    public int getY()
    {
	return this.y;
    }
	
    /**
     * Saves a copy of the shapes state, then modifies it's left position
     *
     * @param x amount to move in left-right direction
     */
    public void setX(final int x)
    {
	this.x = x;
    }
	
    /**
     * Saves a copy of the shapes state, then modifies it's top position
     *
     * @param y amount to move in up-down direction
     */
    public void setY(final int y)
    {
	this.y = y;
    }
    
    
    /**
     * Stores the shape's current state to a {@link Momento}
     * 
     * @return  The state
     */
    public abstract Momento store();
    
    
    /**
     * Restores the shape's state from a {@link Momento}
     * 
     * @param  momento  The state
     */
    public void restore(final Momento momento)
    {
        momento.restore(this);
    }

    /**
     * Gets the player whom is playing or played the shape
     * 
     * @return  The player whom is playing or played the shape
     */
    public Player getPlayer()
    {
	return this.player;
    }
    
    
    /**
     * Sets the player whom is playing the shape
     * 
     * @param  value  The player whom is playing the shape
     */
    public void setPlayer(final Player value)
    {
	this.player = value;
	for (final Block[] row : this.shape)
	    for (final Block block : row)
		block.setColor(value.getColor());
    }
    
    /**
     * Returnss the current shape using * as marker for a block
     */
    public String toString()
    {
	String strShape = "";
	for (int i = 0; i < this.shape[0].length; i++)
	{
	    for (int j = 0; j < this.shape.length; j++)
		if (this.shape[j][i] != null)
		    strShape += "*";
		else
		    strShape += " ";
	    
	    strShape += "\n";
	}
		
	return strShape;
    }
	
    /**
     * Rotate the shape around its center.
     * 
     * @param  clockwise  <code>true</code> to rotate clockwise (right),
     *                    <code>false</code> to rotate anticlockwise (left)
     */
    public abstract void rotate(final boolean clockwise);

    /**
     * {@inheritDoc}
     */
    public Shape clone() throws CloneNotSupportedException
    {
	throw new CloneNotSupportedException();
    }
    
    /**
     * Clones data into another {@link Shape}
     * 
     * @param  shape  Destination
     */
    protected void cloneData(final Shape shape)
    {
	shape.x = this.x;
	shape.y = this.y;
	shape.player = this.player;
	shape.shape = this.shape;
    }
    
}
