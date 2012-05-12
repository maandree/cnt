/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.demo;
import cnt.interaction.desktop.*;
import cnt.game.*;
import cnt.*;

import java.awt.Color;


/**
 * Main frame demo class
 *
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class MainFrameDemo
{
    /**
     * Non-constructor
     */
    private MainFrameDemo()
    {
	assert false : "You may not create instances of this class [MainFrameDemo].";
    }
    
    
    
    /**
     * This is the main entry point of the program
     * 
     * @param  args  Start up arguments
     */
    public static void main(final String... args) throws InterruptedException
    {
	(new MainFrame()).setVisible(true);
	
	Blackboard.registerObserver(new Blackboard.BlackboardObserver()
	        {
		    /**
		     * {@inheritDoc}
		     */
		    public void messageBroadcasted(final Blackboard.BlackboardMessage message)
		    {
			if (message instanceof Blackboard.UserMessage)
			{
			    final String msg = ((Blackboard.UserMessage)message).message;
			    Blackboard.broadcastMessage(new Blackboard.ChatMessage(new Player("Mattias", Color.BLUE.getRGB()), msg));
			}
		    }
	        });
	
	Blackboard.broadcastMessage(new Blackboard.SystemMessage(null, "New game started."));
	
	final Block[] blocks = {
	        new Block((new Color(205, 101, 108)).getRGB()),  // NCS S 2050-R
		new Block((new Color(164, 110, 176)).getRGB()),  // NCS S 2050-R50B
		new Block((new Color( 36, 149, 190)).getRGB()),  // NCS S 2050-B
		new Block((new Color(  0, 160, 159)).getRGB()),  // NCS S 2050-B50G
		new Block((new Color( 50, 166, 121)).getRGB()),  // NCS S 2050-G
		new Block((new Color(156, 173,  81)).getRGB()),  // NCS S 2050-G50Y
		new Block((new Color(204, 173,  71)).getRGB()),  // NCS S 2050-Y
		new Block((new Color(218, 128,  77)).getRGB()),  // NCS S 2050-Y50R
	        };
	
	for (int x = 0, xn = blocks.length; x < xn; x++)
	{
	    Thread.sleep(500);
	    
	    Blackboard.broadcastMessage(new Blackboard.MatrixPatch(null, new Block[][] {{blocks[x]}}, 0, x));
	    
	    final boolean[][] erase = {{true}};
	    final Block[][] add = {{null}, {blocks[x]}};
	    
	    for (int y = 0; y < 19; y++)
	    {
		Thread.sleep(500);
		Blackboard.broadcastMessage(new Blackboard.MatrixPatch(erase, add, y, x));
	    }
	}
    }
    
}

