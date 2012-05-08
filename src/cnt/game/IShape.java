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
* Shape class representing a I-Shape
* 
* @author Calle Lejdbrandt <a/ href="callel@kth.se">callel@kth.se</a>
*/

public class IShape extends Shape
{
	private boolean flat = true;
	
	public IShape(final Player player)
	{
		this.player = player;
		this.shape = new Block[4][4];
		
		int[][] placement = new int[][]{{0,0},{1,0},{2,0},{3,0}};
		for (int[] place : placement)
		{
			this.shape[place[0]][place[1]] = new Block(this.player.getColor());
		}
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
}
