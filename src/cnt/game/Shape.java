/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.game;

// Added this for clearety
import cnt.game.Board;

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
	public Shape[][] shape;

	/**
	* Current offsets from top-left corner
	*/
	public int x, y;

	/**
	* Returns the current Shape
	* 
	* @return The Shape object
	*/
	public getShape()
	{
		return this;
	}
	
	/**
	* Move the shape x and y steps. Positive integers are right, and up. Negative vice versa.
	* preforms colition checking
	*
	* @param x number of steps to move along x-axis
	* @param y number of steps to move along y-axis
	*/
	private move(final int x, final int y)
	{
		Board boardCopy = Board.getMatrix();
		
