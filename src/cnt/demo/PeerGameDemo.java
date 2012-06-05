/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.demo;
import cnt.interaction.desktop.*;
import cnt.network.PlayerRing;
import cnt.messages.*;
import cnt.game.*;
import cnt.*;
import cnt.mock.*;


/**
 * Two player game demo class
 *
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class PeerGameDemo
{
    /**
     * Non-constructor
     */
    private PeerGameDemo()
    {
	assert false : "You may not create instances of this class [PeerGameDemo].";
    }
    
    
    
    /**
     * This is the main entry point of the demo
     * 
     * @param  args  Startup arguments, {name, may be server ? s : c, local public IP address, servers' port, remote peer's IP address}
     * 
     * @thorws  Exception  On total failure
     */
    public static void main(final String... args) throws Exception
    {
	(new Engine()).start();
	(new GameFrame()).setVisible(true);
	new PlayerRing();
	
	final char name = args[0].charAt(0);
	final boolean serverauth = args[1].charAt(0) == 's';
	final String pubip = args[2];
	final int serverport = Integer.parseInt(args[3]);
	final String remote = args[4];
	
	int id = args[0].hashCode() & 0xFFF;
	final Player me = new Player(args[0], null, id, null, null, 0, serverauth ? id : ~id);
	
	Blackboard.registerObserver(new Blackboard.BlackboardObserver()
	        {
		    private int score = 0;
		    
		    /**
		     * {@inheritDoc}
		     */
		    public void messageBroadcasted(final Blackboard.BlackboardMessage message)
		    {
			if (message instanceof GameScore)
			{   this.score = ((GameScore)message).score;
			}
			else if (message instanceof GameOver)
			{   System.out.println("\033[33mGame over (" + this.score + " points)!\033[0m");
			}
		    }
	        });
	
	
	final ConnectionNetworking connectionNetworking = new ConnectionNetworking(name, serverauth, pubip, serverport, remote);
	System.err.println("ConnectionNetworking created");
	
	final ObjectNetworking     objectNetworking     = new ObjectNetworking(connectionNetworking.globalIn, connectionNetworking.globalOut);
	System.err.println("ObjectNetworking created");
	
	final GameNetworking       gameNetworking       = new GameNetworking(objectNetworking);
	System.err.println("GameNetworking created");
	
	final BlackboardNetworking blackboardNetworking = new BlackboardNetworking(gameNetworking);
	System.err.println("BlackboardNetworking created");
	
	
	final Thread playThread = new Thread()
	        {
		    /**
		     * {@inheritDoc}
		     */
		    @Override
		    public void run()
		    {
			try
			{
			    for (int d; (d = System.in.read()) != -1;)
				switch (d)
				{
				    case 'q':  return;
				    case 's':  Blackboard.broadcastMessage(new GamePlayCommand(GamePlayCommand.Move.ANTICLOCKWISE));  break;
				    case 'd':  Blackboard.broadcastMessage(new GamePlayCommand(GamePlayCommand.Move.CLOCKWISE));      break;
				    case ' ':  Blackboard.broadcastMessage(new GamePlayCommand(GamePlayCommand.Move.DROP));           break;
				    case 'A':  Blackboard.broadcastMessage(new GamePlayCommand(GamePlayCommand.Move.ANTICLOCKWISE));  break;  //up arrow
				    case 'B':  Blackboard.broadcastMessage(new GamePlayCommand(GamePlayCommand.Move.DOWN));           break;  //down arrow
				    case 'C':  Blackboard.broadcastMessage(new GamePlayCommand(GamePlayCommand.Move.RIGHT));          break;  //right arrow
				    case 'D':  Blackboard.broadcastMessage(new GamePlayCommand(GamePlayCommand.Move.LEFT));           break;  //left arrow
				}
			}
			catch (final Throwable err)
			{
			    err.printStackTrace(System.err);
			}
		    }
	        };
	playThread.start();
	
	
	Blackboard.broadcastMessage(new LocalPlayer(me));
	Blackboard.broadcastMessage(new PlayerJoined(me));
	
	
	for (;;)
	    blackboardNetworking.receiveAndBroadcast();
    }
    
}
