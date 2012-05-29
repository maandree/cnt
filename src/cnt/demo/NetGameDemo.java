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

import java.net.*;


/**
 * Game demo for multiple computers
 *
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class NetGameDemo
{
    /**
     * Non-constructor
     */
    private NetGameDemo()
    {
	assert false : "You may not create instances of this class [NetGameDemo].";
    }
    
    
    
    /**
     * This is the main entry point of the demo
     * 
     * @param  args  Startup arguments
     * 
     * @thorws  Exception  On total failure
     */
    public static void main(final String... args) throws Exception
    {
	(new GameFrame()).setVisible(true);
	final PlayerRing ring = new PlayerRing();
	final Player[] player = { null };
	        
        final String name = args[0];
        final String remote = args.length > 1 ? args[1] : null;
	
	final Player me = new Player(name, name.hashCode() | (255 << 24), null, null);
	final Object monitor = new Object();
	
	final Player[] lowest = {null};
	
	
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
			{
			    final Player player = ((PlayerJoined)message).player;
			    if ((lowest[0] == null) || (lowest[0].getID() > player.getID()))
				lowest[0] = player;
			    players++;
			    if (players == 2)
				synchronized (monitor)
			        {    monitor.notify();
				}
			}
		    }
	        });
	
	
	final BlackboardNetworking blackboardNetworking = new BlackboardNetworking();
        Blackboard.broadcastMessage(new SystemMessage(null, "BlackboardNetworking and all other *Networking instances created from chain."));
	
	if (remote != null)
	{
		blackboardNetworking.gameNetworking.objectNetworking.connectionNetworking.connect(
                        (Inet4Address)(Inet4Address.getByName(remote)),
			ConnectionNetworking.PORT,
			args.length > 2 ? Integer.parseInt(args[2]) : 1
                        );
	}

	
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
			    
			    if (me.equals(lowest[0]))
				Engine.start();
			    
			    for (int d; (d = System.in.read()) != -1;)
			        if (me.equals(player[0])) //order is important
				{
				    System.err.println("\033[32m" + me + " == " + player[0] + "\033[39m");
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
				else
				    System.err.println("\033[31m" + me + " != " + player[0] + "\033[39m");
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
    }
    
}
