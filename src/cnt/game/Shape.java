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
* @author Calle Lejdbrandt <a/ href="callel@kth.se">callel@kth.se</a>
*/
public abstract class Shape implements Cloneable, Serializable
{
	/**
	* The shape that we want
	*/
	public Block[][] shape;

	/**
	* Current offsets from top-left corner
	*/
	public int x = 0, y = 0;
	
	/**
	* The last state the shape was in
	*/	
	private Shape old_state;

	/**
	* Player owning the shape
	*/
	public Player player;

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

		for (int col = 0; col < shape.length; ++col)
		{
			for (int row = 0; row < shape[0].length; ++row)
			{
				if (shape[col][row] != null)
				{
					matrix[col][row] = true;
				}
			}
		}
		
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
		try
		{
			this.old_state = (Shape)this.clone();
			this.x = x;
		} catch (CloneNotSupportedException err)
		{
			System.out.println("Something went wrong cloneing a shape");
		}
	}
	
	/**
	* Saves a copy of the shapes state, then modifies it's top position
	*
	* @param y amount to move in up-down direction
	*/
	public void setY(final int y)
	{
		try
		{
			this.old_state = (Shape)this.clone();
			this.y = y;
		} catch (CloneNotSupportedException err)
		{
			System.out.println("Something went wrong cloneing a shape");
		}
	}
	
	/**
	* Restore and return the previous state the shape was in
	*
	* @return the shape object in it's last state
	*/
	public Shape restore()
	{
		this.x = this.old_state.getX();
		this.y = this.old_state.getY();
		this.shape = this.old_state.getBlockMatrix();
		
		return this.old_state;
	}

	/**
	* Returnss the current shape using * as marker for a block
	*/
	public String toString()
	{
		String strShape = "";
		for (int i = 0; i < this.shape[0].length; ++i)
		{
			for (int j = 0; j < this.shape.length; ++j)
			{
				if (this.shape[j][i] != null)
					strShape += "*";
				else
					strShape += " ";
			}
			strShape += "\n";
		}
		
		return strShape;
	}
	
	/**
	* Rotate the shape around its center.
	* 
	* @param clockwise if <code>true</code> we rotate clockwise (i.e. right), if <code>false</code> we rotate counterclockwise (i.e. left)
	*/
	public abstract void rotate(final boolean clockwise);
	
}
