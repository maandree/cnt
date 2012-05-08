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
* Shape class representing a J-Shape
* 
* @author Calle Lejdbrandt <a/ href="callel@kth.se">callel@kth.se</a>
*/

public class JShape extends Shape
{	
	private Block[][][] states = new Block[4][3][3];
	private int currState = 0;
	public JShape(final Player player)
	{
		this.player = player;
		this.shape = new Block[3][3];
		
		int[][] placement = new int[][] {{1,0},{1,1},{1,2},{0,2}};
		for (int[] place : placement)
		{
			this.shape[place[0]][place[1]] = new Block(this.player.getID());
		}
		
		this.states[0] = this.shape
		
		int[][][] coords = new int[3][4][2];

		coords[0] = new int[][] {{0,1},{1,1},{2,1},{0,0}};
		coords[1] = new int[][] {{2,0},{1,0},{1,1},{1,2}};
		coords[2] = new int[][] {{2,2},{0,1},{1,1},{2,1}};
		
		int i = 0;
		for (int[][] coord : coords)
		{
			Block[][] matrix = new Block[3][3];
			for (int[] place : coord)
			{
				matrix[place[0]][place[1]] = new Block(this.player.getID());
			}
			
			this.states[i++] = matrix;
		}
				
		
		
	}

	public void rotate(final boolean clockwise)
	{
		if (clockwise)
		{
			this.currState = (this.currState + 1) % 3;
		} else
		{
			this.currState = (this.currState - 1) % 3;
		}
		
		this.shape = this.states[this.currState];
	}
}
