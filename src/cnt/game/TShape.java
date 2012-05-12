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
* Shape class representing a T-shape
* 
* @author  Calle Lejdbrandt, <a href="mailto:callel@kth.se">callel@kth.se</a>
* @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
*/
public class TShape extends Shape
{
    public TShape()
    {
	this.shape = new Block[3][3];
	    
	int[][] placement = new int[][] {{1,1},{0,1},{1,0},{2,1}};
	for (int[] place : placement)
	{
	    this.shape[place[0]][place[1]] = new Block();
	}
		
    }
    
    private TShape(final TShape original)
    {
	original.cloneData(this);
    }
    
    
    
    /**
     * Momento class for {@link TShape}
     */
    public static class Momento extends Shape.Momento
    {
        public Momento(final TShape shape)
        {
            super(shape);
        }
        
        /**
         * Restores the shape's state
         * 
         * @param  Shape  The shape
         */
        public void restore(final Shape shape)
        {
            if (shape instanceof TShape == false)
                throw new Error("Wrong shape type");
            super.restore(shape);
        }
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Momento store()
    {
        return new Momento(this);
    }
    
    

    public void rotate(final boolean clockwise)
    {
	if (clockwise) 
	    {
		this.shape = this.turn();
	    } else
	    {
		// 3 clockwise turns = 1 counterclockwise turn, so...
		for (int i = 0; i < 4; i++)
		    {
			this.shape = this.turn();
		    }
	    }
				
    }
	
    public Block[][] turn()
    {
	Block[][] matrix = new Block[3][3];
		
	if (this.shape[1][0] != null)  matrix[2][1] = this.shape[1][0];
	if (this.shape[2][1] != null)  matrix[1][2] = this.shape[2][1];
	if (this.shape[1][2] != null)  matrix[0][1] = this.shape[1][2];
	if (this.shape[0][1] != null)  matrix[1][0] = this.shape[0][1];
		
	matrix[1][1] = this.shape[1][1];
		
	return matrix;
    }
	
    public TShape clone()
    {
	return new TShape(this);
    }
}
