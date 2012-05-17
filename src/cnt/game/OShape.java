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
 * Shape class representing a O-shape
 * 
 * @author  Calle Lejdbrandt, <a href="mailto:callel@kth.se">callel@kth.se</a>
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class OShape extends Shape
{
    /**
     * Compatibility versioning for {@link Serializable}
     */
    private static final long serialVersionUID = 1L;
    
    
    
    /**
     * Constructor
     */
    public OShape()
    {
	this.shape = new Block[2][2];
	for (int i = 0; i < 2; i++)
	    for (int j = 0; j < 2; j++)
		this.shape[i][j] = new Block();
    }
    
    /**
     * Cloning constructor
     * 
     * @param  original  The shape to clone
     */
    private OShape(final OShape original)
    {
	original.cloneData(this);
    }
    
    
    
    /**
     * Momento class for {@link OShape}
     */
    public static class Momento extends Shape.Momento
    {
	/**
	 * Constructor
	 * 
	 * @param  shape  The shape of which to save the state
	 */
        public Momento(final OShape shape)
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
            if (shape instanceof OShape == false)
                throw new Error("Wrong shape type");
            super.restore(shape);
        }
    }
    
    
    
    /**
     * {@inheritDoc}
     */
    public Momento store() {
        return new Momento(this);
    }
    
    /**
     * {@inheritDoc}
     */	
    public OShape clone() {
	return new OShape(this);
    }
    
    /**
     * {@inheritDoc}
     */
    public void rotate(final boolean clockwise)
    {
        //indentity function
    }
}
