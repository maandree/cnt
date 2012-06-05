/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.network;
import cnt.messages.*;
import cnt.*;


/**
 * Launcher for this package
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
	new PlayerRing();
	Blackboard.registerObserver(new Launcher());
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void messageBroadcasted(final Blackboard.BlackboardMessage message)
    {
	if (message instanceof JoinGame)
	{
	    final JoinGame game = (JoinGame)message;
	    
	    final BlackboardNetworking bn = new BlackboardNetworking();
	    final GameNetworking gn = new GameNetworking();
	    final ConnectionNetworking cn;
	    
	    if (game.remote == null)
		cn = new ConnectionNetworking(game.name);
	    else
		cn = new ConnectionNetworking(game.name, game.remote, game.port);
	    
	    bn.setGameNetworking(gn);
	    gn.setBlackboardNetworking(bn);
	    gn.setConnectionNetworking(cn);
	    cn.setGameNetworking(gn);
	}
    }
    
}

