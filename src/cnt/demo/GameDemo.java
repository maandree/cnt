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
import java.io.IOException;


/**
 * Game demo class
 *
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class GameDemo
{
    /**
     * Non-constructor
     */
    private GameDemo()
    {
	assert false : "You may not create instances of this class [GameDemo].";
    }
    
    
    
    /**
     * This is the main entry point of the demo
     * 
     * @param  args  Start up arguments, you may enter 'clockwise' or 'anti-clockwise' constant rotations
     * 
     * @thorws  IOException  On total failure
     */
    public static void main(final String... args) throws IOException
    {
	final Color colour = new Color(36, 149, 190);
	final Player player = new Player("The One", colour.getRGB());
	
	(new MainFrame()).setVisible(true);
	
	Blackboard.registerObserver(new Blackboard.BlackboardObserver()
	        {
		    /**
		     * {@inheritDoc}
		     */
		    public void messageBroadcasted(final Blackboard.BlackboardMessage message)
		    {
			if (message instanceof Blackboard.NextPlayer) /*do not thread*/
			{
			    if (((Blackboard.NextPlayer)message).player == null)
				Blackboard.broadcastMessage(new Blackboard.NextPlayer(player));
			}
			else if (message instanceof Blackboard.GameOver)
			{
			    System.out.println("\033[33mGame over!\033[0m");
			}
		    }
	        });
	
	Engine.start();
	
	
	for (int d; (d = System.in.read()) != -1;)
	    switch (d)
	    {
		case 'q':  return;
		case 's':  Blackboard.broadcastMessage(new Blackboard.GamePlayCommand(Blackboard.GamePlayCommand.Move.ANTICLOCKWISE));  break;
		case 'd':  Blackboard.broadcastMessage(new Blackboard.GamePlayCommand(Blackboard.GamePlayCommand.Move.CLOCKWISE));      break;
		case ' ':  Blackboard.broadcastMessage(new Blackboard.GamePlayCommand(Blackboard.GamePlayCommand.Move.DROP));           break;
		case 'A':  Blackboard.broadcastMessage(new Blackboard.GamePlayCommand(Blackboard.GamePlayCommand.Move.CLOCKWISE));      break;  //up arrow
		case 'B':  Blackboard.broadcastMessage(new Blackboard.GamePlayCommand(Blackboard.GamePlayCommand.Move.DOWN));           break;  //down arrow
		case 'C':  Blackboard.broadcastMessage(new Blackboard.GamePlayCommand(Blackboard.GamePlayCommand.Move.RIGHT));          break;  //right arrow
		case 'D':  Blackboard.broadcastMessage(new Blackboard.GamePlayCommand(Blackboard.GamePlayCommand.Move.LEFT));           break;  //left arrow
	    }
    }
    
}

