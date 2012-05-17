/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.game;
import cnt.game.Board;
import cnt.game.Block;
import cnt.game.Shape;
import cnt.game.Player;

import java.io.*;


/**
 * Shape class representing a J-shape
 * 
 * @author  Calle Lejdbrandt, <a href="mailto:callel@kth.se">callel@kth.se</a>
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class JShape extends Shape
{
    /**
     * Compatibility versioning for {@link Serializable}
     */
    private static final long serialVersionUID = 1L;
    
    
    
    /**
     * Constructor
     */
    public JShape()
    {
	this.states = new Block[4][3][3];
	this.shape = new Block[3][3];
		
	int[][] placement = new int[][] {{1,0},{1,1},{1,2},{0,2}};
	for (int[] place : placement)
	    this.shape[place[0]][place[1]] = new Block();
	
	this.states[0] = this.shape;
	
	int[][][] coords = new int[3][4][2];
	
	coords[0] = new int[][] {{0,1},{1,1},{2,1},{0,0}};
	coords[1] = new int[][] {{2,0},{1,0},{1,1},{1,2}};
	coords[2] = new int[][] {{2,2},{0,1},{1,1},{2,1}};
		
	int i = 1;
	for (int[][] coord : coords)
	{
	    Block[][] matrix = new Block[3][3];
	    for (int[] place : coord)
		matrix[place[0]][place[1]] = new Block();
	    
	    this.states[i++] = matrix;
	}
    }
    
    /**
     * Cloning constructor
     * 
     * @param  original  The shape to clone
     */
    private JShape(final JShape original)
    {
	original.cloneData(this);
	this.states = original.states;
	this.currState = original.currState;
    }
    
    
    
    /**
     * The shape's possible states
     */
    Block[][][] states;
    
    /**
     * The index of the shape's current state
     */
    int currState = 0;
    
    
    
    /**
     * Momento class for {@link JShape}
     */
    public static class Momento extends Shape.Momento
    {
	/**
	 * Constructor
	 * 
	 * @param  shape  The shape of which to save the state
	 */
        public Momento(final JShape shape)
        {
            super(shape);
            this.states = shape.states;
            this.currState = shape.currState;
        }
	
	
	
	/**
	 * See {@link JShape#states}
	 */
        private final Block[][][] states;
	
	/**
	 * See {@link JShape#currState}
	 */
	private final int currState;
	
	
	
        /**
         * Restores the shape's state
         * 
         * @param  Shape  The shape
         */
        public void restore(final Shape shape)
        {
            if (shape instanceof JShape == false)
                throw new Error("Wrong shape type");
            super.restore(shape);
            ((JShape)shape).states = this.states;
            ((JShape)shape).currState = this.currState;
        }
    }
    
    
    
    /**
     * {@inheritDoc}
     */
    public Momento store()
    {
        return new Momento(this);
    }
    
    

    /**
     * {@inheritDoc}
     */
    public void rotate(final boolean clockwise)
    {
	if (clockwise)
	    this.currState = (this.currState + 1) % 4;
	else
	    this.currState = (this.currState - 1) < 0
		             ? (this.currState + 3)
		             : (this.currState - 1);
		
	this.shape = this.states[this.currState];
    }
    
    /**
     * {@inheritDoc}
     */	
    public JShape clone()
    {
	return new JShape(this);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setPlayer(final Player value)
    {
	super.setPlayer(value);
	for (final Block[][] state : states)
	    for (final Block[] row : state)
		for (final Block block : row)
		    if (block != null)
			block.setColor(value.getColor());
    }
}
