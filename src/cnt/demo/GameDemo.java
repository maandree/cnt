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
     * @param  args  Startup arguments, unused
     * 
     * @thorws  IOException  On total failure
     */
    public static void main(final String... args) throws IOException
    {
	final Recorder rec = new Recorder("/dev/shm/recording.cnt");
	
	(new GameFrame()).setVisible(true);
	final PlayerRing ring = new PlayerRing();
	
	rec.start();
	
	Blackboard.broadcastMessage(new PlayerJoined(new Player("Mattias", null, "Mattias".hashCode() | (255 << 24), null)));
	Blackboard.broadcastMessage(new PlayerJoined(new Player("Peyman",  null, "Peyman" .hashCode() | (255 << 24), null)));
	Blackboard.broadcastMessage(new PlayerJoined(new Player("Calle",   null, "Calle"  .hashCode() | (255 << 24), null)));
	
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
	
	/*Engine.start();
	
	
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
		}*/
	
	ring.stop();
	rec.stop();
    }
    
}

