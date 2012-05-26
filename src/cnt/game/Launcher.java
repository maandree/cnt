/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.game;
import cnt.messages.*;
import cnt.*;


/**
 * Launcher for this package
 *
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class Launcher implements Blackboard.BlackboardObserver
{
    /**
     * Constructor
     */
    private Launcher()
    {
	//Privatise default constructor
    }
    
    
    
    /**
     * Main launcher method for this package
     * 
     * @param  args  Startup arguments, unused
     */
    public static void launch(final String... args)
    {
	Blackboard.registerObserver(new Launcher());
    }
    
    
    
    /**
     * {@inheritDoc}
     */
    public void messageBroadcasted(final Blackboard.BlackboardMessage message)
    {
	if (message instanceof JoinGame)
	{
	    if (((JoinGame)message).remote == null)
		Engine.start();
	}
    }
    
}

