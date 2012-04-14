/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright Ⓒ 2012  Mattias Andrée, Peyman Eshtiagh,
 *                   Calle Lejdbrandt, Magnus Lundberg
 *
 * Project for prutt12 (DD2385), KTH.
 */
package cnt;
import cnt.gui.*;
import cnt.control.*;

import java.awt.Color;


/**
 * Game matrix demo class
 *
 * @author  Mattias Andrée, <a href="maandree@kth.se">maandree@kth.se</a>
 */
public class GameMatrixDemo
{
    /**
     * Non-constructor
     */
    private GameMatrixDemo()
    {
	assert false : "You may not create instances of this class [GameMatrixDemo].";
    }
    
    
    
    /**
     * This is the main entry point of the program
     * 
     * @param  args  Start up arguments
     */
    public static void main(final String... args) throws InterruptedException
    {
	(new MainFrame()).setVisible(true);
	
	final Color[] colours = {new Color(200, 20, 20),
				 new Color(160, 160, 20),
				 new Color(20, 175, 20),
				 new Color(20, 175, 175),
				 new Color(20, 20, 200),
				 new Color(160, 20, 160),
	                        };
	
	for (int x = 0, xn = colours.length; x < xn; x++)
	{
	    Thread.sleep(500);
	    
	    Blackboard.broadcastMessage(new Blackboard.MatrixPatch(null, new Color[][] {{colours[x]}}, 0, x));
	    
	    final boolean[][] erase = {{true}};
	    final Color[][] blocks = {{null}, {colours[x]}};
	    
	    for (int y = 0; y < 19; y++)
	    {
		Thread.sleep(500);
		Blackboard.broadcastMessage(new Blackboard.MatrixPatch(erase, blocks, y, x));
	    }
	}
    }
    
}

