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
import cnt.game.Shape;
import cnt.game.Player;

import java.io.*;


/**
* Shape class representing a I-shape
* 
* @author  Calle Lejdbrandt, <a href="mailto:callel@kth.se">callel@kth.se</a>
* @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
*/
public class IShape extends Shape
{
	private boolean flat = true;
	
	public IShape()
	{
		this.shape = new Block[4][4];
		
		int[][] placement = new int[][]{{0,0},{1,0},{2,0},{3,0}};
		for (int[] place : placement)
		{
			this.shape[place[0]][place[1]] = new Block();
		}
	}
    
    private IShape(final IShape original)
    {
	original.cloneData(this);
    }
    
    
    
    /**
     * Momento class for {@link IShape}
     */
    public static class Momento extends Shape.Momento
    {
        public Momento(final IShape shape)
        {
            super(shape);
            this.flat = shape.flat;
        }
            
        private final boolean flat;
    
        /**
         * Restores the shape's state
         * 
         * @param  Shape  The shape
         */
        public void restore(final Shape shape)
        {
            if (shape instanceof IShape == false)
                throw new Error("Wrong shape type");
            super.restore(shape);
            ((IShape)shape).flat = this.flat;
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
	// Rotates kinda wierd, but hard to find a good rotation of this block...
	public void rotate(final boolean clockwise)
	{
		Block[][] matrix = new Block[4][4];
		int[][] placement;
		if (this.flat)
		{
			placement = new int[][]{{0,0},{0,1},{0,2},{0,3}};
			this.flat = false;
		} else
		{
			placement = new int[][]{{0,0},{1,0},{2,0},{3,0}};
			this.flat = true;
		}
		
		for (int[] place : placement)
		{
			matrix[place[0]][place[1]] = this.shape[place[1]][place[0]];
		}

		this.shape = matrix;
	}
    
    public IShape clone()
    {
	return new IShape(this);
    }
}
