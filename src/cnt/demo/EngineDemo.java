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
 * Engine demo class
 *
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class EngineDemo
{
    /**
     * Non-constructor
     */
    private EngineDemo()
    {
	assert false : "You may not create instances of this class [EngineDemo].";
    }
    
    
    
    /**
     * This is the main entry point of the program
     * 
     * @param  args  Start up arguments
     */
    public static void main(final String... args) throws InterruptedException
    {
	final Color colour = new Color(36, 149, 190);   // NCS S 2050-B
	final Player player = new Player("The One", colour);
	
	(new MainFrame()).setVisible(true);
	
	Blackboard.registerObserver(new Blackboard.BlackboardObserver()
	        {
		    /**
		     * {@inheritDoc}
		     */
		    public void messageBroadcasted(final Blackboard.BlackboardMessage message)
		    {
			if (message instanceof Blackboard.NextPlayer)
			{
			    System.out.println("\033[33mNext player\033[0m");
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
	
	
	Thread.sleep(500);   System.out.println("RIGHT -->");  Blackboard.broadcastMessage(new Blackboard.GamePlayCommand(Blackboard.GamePlayCommand.Move.RIGHT));
	Thread.sleep(200);   System.out.println("LEFT <-- ");  Blackboard.broadcastMessage(new Blackboard.GamePlayCommand(Blackboard.GamePlayCommand.Move.LEFT));
	for (int i = 0; i < 10; i++)
	    { Thread.sleep(1000);  System.out.println("LEFT <-- ");  Blackboard.broadcastMessage(new Blackboard.GamePlayCommand(Blackboard.GamePlayCommand.Move.LEFT)); }
	
	Thread.sleep(500);   System.out.println("    DROP");  Blackboard.broadcastMessage(new Blackboard.GamePlayCommand(Blackboard.GamePlayCommand.Move.DROP));
	
	for (int i = 0; i < 10; i++)
	    { Thread.sleep(500);   System.out.println("RIGHT -->");  Blackboard.broadcastMessage(new Blackboard.GamePlayCommand(Blackboard.GamePlayCommand.Move.RIGHT)); }
	
	for (int i = 0; i < 5; i++)
	    { Thread.sleep(300);   System.out.println("  DOWN");  Blackboard.broadcastMessage(new Blackboard.GamePlayCommand(Blackboard.GamePlayCommand.Move.DOWN)); }
	
	Thread.sleep(7000);
	
	for (int i = 0; i < 4; i++)
	    { Thread.sleep(1000);  System.out.println("CLOCKWISE");  Blackboard.broadcastMessage(new Blackboard.GamePlayCommand(Blackboard.GamePlayCommand.Move.CLOCKWISE)); }
	
	for (int i = 0; i < 4; i++)
	    { Thread.sleep(1000);  System.out.println("ANTI-CLOCKWISE");  Blackboard.broadcastMessage(new Blackboard.GamePlayCommand(Blackboard.GamePlayCommand.Move.ANTICLOCKWISE)); }
	
    }
    
}

