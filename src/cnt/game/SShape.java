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
* Shape class representing a T-Shape
* 
* @author Calle Lejdbrandt <a/ href="callel@kth.se">callel@kth.se</a>
*/

public class SShape extends Shape
{
	boolean flat = true;
	
	public SShape(final Player player)
	{
		this.player = player;
		this.shape = new Block[3][3];
		
		int[][] placement = new int[][] {{1,0},{2,0},{0,1},{1,1}};
		for (int[] place : placement)
		{
			this.shape[place[0]][place[1]] = new Block(this.player.getColor());
		}
		
	}

	public void rotate(final boolean clockwise)
	{
		Block[][] matrix = new Block[3][3];

		if (this.flat)
		{
			matrix[1][0] = this.shape[1][0];
			matrix[1][1] = this.shape[1][1];
			matrix[2][1] = this.shape[2][0];
			matrix[2][2] = this.shape[0][1];
			
			this.shape = matrix;
		} else
		{
			matrix[1][0] = this.shape[1][0];
			matrix[1][1] = this.shape[1][1];
			matrix[2][0] = this.shape[2][1];
			matrix[0][1] = this.shape[2][2];
			
			this.shape = matrix;

		}				
	}
}
