/**
 * Coop Network Tetris – A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.interaction.desktop;
import cnt.game.*;
import cnt.game.enginehelp.*;
import cnt.messages.*;
import cnt.*;

import javax.swing.*;


/**
 * Score displayer
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
@SuppressWarnings("serial")
public class ScoreLabel extends JLabel implements Blackboard.BlackboardObserver
{
    /**
     * Constructor
     */
    public ScoreLabel()
    {
	Blackboard.registerObserver(this);
    }
    
    
    
    /**
     * {@inheritDoc}
     */
    public synchronized void messageBroadcasted(final Blackboard.BlackboardMessage message)
    {
	if (message instanceof GameScore)
	{
	    final GameScore msg = (GameScore)message;
	    this.setText("Score: " + msg.score);
	}
	else if (message instanceof FullUpdate)
	{
	    final FullUpdate fullUpdate = (FullUpdate)message;
	    if (fullUpdate.isGathering() == false)
		this.setText("Score: " + ((EngineData)(fullUpdate.data.get(Engine.class))).score);
	}
    }
    
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {   return "(ScoreLabel)";
    }
    
}

