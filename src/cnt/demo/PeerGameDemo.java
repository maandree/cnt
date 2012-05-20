/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.demo;
import cnt.interaction.desktop.*;
import cnt.network.*;
import cnt.messages.*;
import cnt.game.*;
import cnt.*;
import cnt.mock.ConnectionNetworking;


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
	(new MainFrame()).setVisible(true);
	final PlayerRing ring = new PlayerRing();
	final Player[] player = { null };
	
	
	final char name = args[0].charAt(0);
	final boolean serverauth = args[1].charAt(0) == 's';
	final String pubip = args[2];
	final int serverport = Integer.parseInt(args[3]);
	final String remote = args[4];
	
	final Player me = new Player(args[0], args[0].hashCode() | (255 << 24));
	final Object monitor = new Object();
	
	
	Blackboard.registerObserver(new Blackboard.BlackboardObserver()
	        {
		    private int score = 0;
		    private int players = 0;
		    
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
			else if (message instanceof NextPlayer)
			{   player[0] = ((NextPlayer)message).player;
			}
			else if (message instanceof PlayerJoined)
			{   players++;
			    if (players == 2)
				synchronized (monitor)
			        {    monitor.notify();
				}
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
			    synchronized (monitor)
			    {   monitor.wait();
			    }
			    
			    //Engine.start();
			    
			    /*for (int d; (d = System.in.read()) != -1;)
				if (me.equals(player[0])) //order is important
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
				    }*/
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
