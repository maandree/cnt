/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright Ⓒ 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.game;

import java.io.*;


/**
 * Block shape class
 */
public class Shape implements Cloneable, Serializable
{
    /**
     * Shape:
     * <pre>
     *     XXX
     *     .X.
     * </pre>
     */
    public static final Shape T_SHAPE = new Shape(null);
    
    /**
     * Shape:
     * <pre>
     *     X..
     *     XXX
     * </pre>
     */
    public static final Shape L_SHAPE = new Shape(null);
    
    
    /**
     * Shape:
     * <pre>
     *     ..X
     *     XXX
     * </pre>
     */
    public static final Shape J_SHAPE = new Shape(null);
    
    /**
     * Shape:
     * <pre>
     *     .XX
     *     XX.
     * </pre>
     */
    public static final Shape S_SHAPE = new Shape(null);
    
    /**
     * Shape:
     * <pre>
     *     XX.
     *     .XX
     * </pre>
     */
    public static final Shape Z_SHAPE = new Shape(null);
    
    /**
     * Shape:
     * <pre>
     *     XX
     *     XX
     * </pre>
     */
    public static final Shape SQUARE_SHAPE = new Shape(null);
    
    
    /**
     * Shape:
     * <pre>
     *     X
     *     X
     *     X
     *     X
     * </pre>
     */
    public static final Shape PIPE_SHAPE = new Shape(null);
    
    
    
    /**
     * <p>Constructor</p>
     * <p>
     *     The shape is placed in the top center of the board
     * </p>
     * 
     * @param  matrix  The block's positions in the shape
     */
    public Shape(final boolean[][] matrix)
    {
	this.setX((Board.WIDTH - matrix[0].length) / 2);
	this.setY(0);
    }
    
    
    
    /**
     * Momento class for {@link Shape}
     */
    public static class Momento
    {
    }
    
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Shape clone() throws CloneNotSupportedException
    {
	return null;
    }
    
    
    /**
     * Splits the shape into multiple shapes, while deleting some rows
     * 
     * @param   deleteRows  The rows to delete
     * @return              The resulting shapes
     */
    public Shape[] split(final int[] deleteRows)
    {
	return null;
    }
    
    
    /**
     * Gets the left position of the shape
     * 
     * @return  The left position of the shape
     */
    public int getX()
    {
	return 0;
    }
    
    
    /**
     * Sets the left position of the shape
     * 
     * @param  value  The new left position of the shape
     */
    public void setX(final int value)
    {
    }
    
    
    /**
     * Gets the top position of the shape
     * 
     * @return  The top position of the shape
     */
    public int getY()
    {
	return 0;
    }
    
    
    /**
     * Sets the top position of the shape
     * 
     * @param  value  The new top position of the shape
     */
    public void setY(final int value)
    {
    }
    
    
    /**
     * Gets the player whom is playing or played the shape
     * 
     * @return  The player whom is playing or played the shape
     */
    public Player getPlayer()
    {
	return null;
    }
    
    
    /**
     * Sets the player whom is playing the shape
     * 
     * @param  value  The player whom is playing the shape
     */
    public void setPlayer(final Player value)
    {
    }
    
    
    /**
     * Rotates the shape
     * 
     * @param  clockwise  <code>true</code> for clockwise rotation, <code>false</code> for anti-clockwise rotation
     */
    public void rotate(final boolean clockwise)
    {
    }
    
    
    /**
     * Gets a matrix of the shapes block positions
     * 
     * @return  A matrix where <code>true</code> represents a block
     */
    public boolean[][] getMatrix()
    {
	return null;
    }
    
    
    /**
     * Stores the shape's current state to a {@link Momento}
     * 
     * @return  The state
     */
    public Momento store()
    {
	return null;
    }
    
    
    /**
     * Restores the shape's state from a {@link Momento}
     * 
     * @param  momento  The state
     */
    public void restore(final Momento momento)
    {
    }
    
}

