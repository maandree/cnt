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
	
	final Color[] colours = {
	        /** /
		new Color(192, 71, 81),   // NCS S 2060-R
		new Color(0, 135, 189),   // NCS S 2060-B
		new Color(0, 159, 107),   // NCS S 2060-G
		new Color(200, 163, 33),  // NCS S 2060-Y
		/**/
	        /**/
	        new Color(205, 101, 108),  // NCS S 2050-R
		new Color(164, 110, 176),  // NCS S 2050-R50B
		new Color(36, 149, 190),   // NCS S 2050-B
		new Color(0, 160, 159),    // NCS S 2050-B50G
		new Color(50, 166, 121),   // NCS S 2050-G
		new Color(156, 173, 81),   // NCS S 2050-G50Y
		new Color(204, 173, 71),   // NCS S 2050-Y
		new Color(218, 128, 77),   // NCS S 2050-Y50R
	        /**/
		/** /
		new Color(226, 100, 111),  // NCS S 1060-R
	        new Color(0, 163, 213),    // NCS S 1060-B
	        new Color(16, 186, 131),   // NCS S 1060-G
	        new Color(245, 205, 61),   // NCS S 1060-Y
	        /**/
		/** /
	        new Color(237, 149, 153),  // NCS S 1040-R
	        new Color(202, 163, 215),  // NCS S 1040-R50B
	        new Color(109, 185, 214),  // NCS S 1040-B
	        new Color(96, 199, 196),   // NCS S 1040-B50G
	        new Color(122, 204, 166),  // NCS S 1040-G
	        new Color(192, 209, 125),  // NCS S 1040-G50Y
	        new Color(241, 212, 116),  // NCS S 1040-Y
	        new Color(251, 166, 115),  // NCS S 1040-Y50R
	        /**/
		/** /
	        new Color(237, 169, 171),  // NCS S 1030-R
	        new Color(206, 179, 211),  // NCS S 1030-R50B
	        new Color(144, 196, 217),  // NCS S 1030-B
	        new Color(138, 205, 202),  // NCS S 1030-B50G
	        new Color(153, 211, 178),  // NCS S 1030-G
	        new Color(200, 213, 146),  // NCS S 1030-G50Y
	        new Color(230, 208, 134),  // NCS S 1030-Y
	        new Color(252, 182, 140),  // NCS S 1030-Y50R
	        /**/
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

