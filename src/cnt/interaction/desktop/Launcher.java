/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.interaction.desktop;
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
    
    
    
    ///**
    // * The started {@link LoginFrame}
    // */
    //private static LoginFrame loginFrame = null;
    
    /**
     * The started {@link GameFrame}
     */
    private static GameFrame gameFrame = null;
    
    
    
    /**
     * Main launcher method for this package
     * 
     * @param  args  Startup arguments, unused
     */
    public static void launch(final String... args)
    {
	//(loginFrame = new LoginFrame()).setVisible(true);
	//Blackboard.registerObserver(new Launcher);
	(new GameFrame()).setVisible(true);
    }
    
    
    
    /**
     * {@inheritDoc}
     */
    public void messageBroadcasted(final Blackboard.BlackboardMessage message)
    {
	//if (message instanceof JoinGame)
	//{
	//    loginFrame.setVisible(false);
	//    (gameFrame = new GameFrame()).setVisible(true);
	//}
    }
    
}

