/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.game;
import cnt.game.enginehelp.*;
import cnt.messages.*;
import cnt.*;

import java.lang.ref.*;
import java.util.*;


/**
 * Game engine main class
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public strictfp class Engine implements Blackboard.BlackboardObserver
{
    /**
     * The initial interval between falls
     */
    private static final double INITIAL_SLEEP_TIME = 1000;
    
    /**
     * The value to multiply the sleep time each time the game speeds up;
     * should be slighly less than 1.
     */
    private static final double SLEEP_TIME_MULTIPLER = 0.995;
    
    
    
    //Has default constructor
    
    
    
    /**
     * All data held by the engine
     */
    public final EngineData data = new EngineData();
    
    /**
     * Shape mover and rotate
     */
    public final Mover mover = new Mover(this);
    
    /**
     * Reactor engine
     */
    public final Reactor reactor = new ClassicalReactor(this.data);
    
    /**
     * General helper
     */
    public final General general = new General(this);
    
    /**
     * Game monitor
     */
    public final Object gameMonitor = new Object();
    
    /**
     * Start monitor
     */
    public final Object startMonitor = new Object();
    
    
    
    /**
     * Starts the engine
     */
    public void start()
    {
	this.data.gameOver = false;
	this.data.sleepTime = INITIAL_SLEEP_TIME / SLEEP_TIME_MULTIPLER; //the division will be nullified when the games starts by nextTurn()
	this.data.board = new Board();
	
	Blackboard.registerObserver(this);
	Blackboard.registerThreadingPolicy(this, Blackboard.DAEMON_THREADING
					   ,GamePlayCommand.class
					   ,EmergencyPause.class
					   ,PlayerDropped.class
					   ,PlayerPause.class
					   ,NextPlayer.class
					   );
	
	Blackboard.broadcastMessage(new GameScore(this.data.score = 0));
	
	this.data.thread = new Thread()
	        {
		    /**
		     * Whether the game is one the first turn
		     */
		    private boolean firstTurn = true;
		    
		    
		    
		    /**
		     * {@inheritDoc}
		     */
		    public void run()
		    {   
			synchronized (Engine.this.startMonitor)
			{   try
			    {   Engine.this.startMonitor.wait();
			    }
			    catch (final InterruptedException err)
			    {   return;
			}   }
			    
			for (;;)
			{
			    synchronized (Engine.this.gameMonitor)
			    {   if ((firstTurn == false) || (Engine.this.data.localPlayer.getID() == Engine.this.data.localPlayer.getConnectedTo()))
				{   Engine.this.data.patcher.dispatch();
				    Engine.this.nextTurn();
				}
				if (firstTurn)
				    firstTurn = false;
				try
				{   Engine.this.gameMonitor.wait();
				}
				catch (final InterruptedException err)
				{   return;
			    }   }
			    
			    if (Engine.this.data.gameOver)
			    {
				Blackboard.broadcastMessage(new GameOver());
				Blackboard.unregisterObserver(Engine.this);
				return;
			    }
			    
			    for (;;)
			    {
				try
				{   Engine.this.sleep(Engine.this.data.sleepTime / 2);
				}
				catch (final InterruptedException err)
				{   if (Engine.this.data.currentPlayer == null)
					break;
				    continue;
				}
				
				try
				{   synchronized (Engine.this)
				    {   if (Engine.this.mover.fall() == false)
					    break;
				}   }
				catch (final InterruptedException err)
			        {   System.err.println("Are you leaving?");
				    return;
				}
				
				try
				{   Engine.this.sleep(Engine.this.data.sleepTime / 2);
				}
				catch (final InterruptedException err)
				{   if (Engine.this.data.currentPlayer == null)
					break;
				    continue;
				}
				
				try
				{   synchronized (Engine.this)
				    {   Engine.this.mover.move();
				}   }
				catch (final InterruptedException err)
			        {   System.err.println("Are you leaving?");
				    return;
				}
			    }
		    }   }
	        };
	
	this.data.thread.start();
    }
    
    
    /**
     * Invoked when a player drops out, the falling block is removed
     * if the dropped out player is the playing player
     */
    private void playerDropped(final Player player)
    {
	this.general.playerDropped(player);
    }
    
    
    /**
     * Starts a new turn
     * 
     * @param  player  The player playing on the new turn
     */
    private void newTurn(final Player player)
    {
	this.general.newTurn(player);
	moved();
    }
    
    
    /**
     * Stations the falling block and deletes empty rows
     */
    public void reaction()
    {
	this.general.reaction();
    }
    
    
    /**
     * Performs everthing needed for a new turn and
     * sends a request for letting the next player start
     */
    public void nextTurn()
    {
	this.data.sleepTime *= SLEEP_TIME_MULTIPLER;
	Blackboard.broadcastMessage(new EngineUpdate(this.data));
	Blackboard.broadcastMessage(new NextPlayer(null));
    }
    
    
    /**
     * Just like {@link Thread#sleep(int)}, but it also broadcasts all updates
     * 
     * @param  milliseconds  The number of milliseconds to sleep, <code>0</code> for not sleeping
     * 
     * @throws  InterruptedException  If the thread is interrupted
     */
    void sleep(final double milliseconds) throws InterruptedException
    {
	this.general.sleep(milliseconds);
    }
    
    
    /**
     * Invoke when the falling shape has moved or otherwise updated
     */
    public void moved()
    {
	Blackboard.broadcastMessage(new EngineShapeUpdate(this.data.fallingShape));
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void messageBroadcasted(final Blackboard.BlackboardMessage message)
    {
	try
	{
	    if (message instanceof GamePlayCommand)
		synchronized (Engine.class)
		{   if (this.data.localPlayer.equals(this.data.currentPlayer))
			switch (((GamePlayCommand)message).move)
			{
			    case LEFT:           this.mover.move(-1);       break;
			    case RIGHT:          this.mover.move(1);        break;
			    case CLOCKWISE:      this.mover.rotate(true);   break;
			    case ANTICLOCKWISE:  this.mover.rotate(false);  break;
				
			    case DOWN:
				if (this.mover.fall() == false)
				    this.data.thread.interrupt();
				break;
				
			    case DROP:
				this.mover.drop();
				this.data.thread.interrupt();
				break;
				
			    default:
				throw new Error("Unrecognised GamePlayCommand.");
		}       }
	    else if (message instanceof NextPlayer) /* do not thread */
	    {
		final Player player = ((NextPlayer)message).player;
		if (player != null)
		{   synchronized (this.data.empauseMonitor)
		    {   if (this.data.empaused)
			    try
			    {   this.data.empauseMonitor.wait();
			    }
			    catch (final InterruptedException err)
			    {   //TODO what do we do know?
			    }
		    }
		    if (player.equals(this.data.localPlayer))
			synchronized (this.gameMonitor)
			{
			    newTurn(player);
			    this.gameMonitor.notify();
			}
		}
	    }
	    else if (message instanceof PlayerDropped)
		playerDropped(((PlayerDropped)message).player);
	    else if (message instanceof LocalPlayer)
	    {
		this.data.localPlayer = ((LocalPlayer)message).player;
		synchronized (Engine.this.startMonitor)
		{   Engine.this.startMonitor.notifyAll();
		}
	    }
	    else if (message instanceof PlayerPause)
		synchronized (this.data.pauseMonitor)
		{
		    if (((PlayerPause)message).player.equals(this.data.localPlayer))
		    {
			this.data.paused = ((PlayerPause)message).paused;
			if (this.data.paused == false)
			    this.data.pauseMonitor.notifyAll();
		    }
		}
	    else if (message instanceof EmergencyPause)
		synchronized (this.data.empauseMonitor)
		{
		    this.data.empaused = ((EmergencyPause)message).paused;
		    if (this.data.empaused == false)
			this.data.empauseMonitor.notifyAll();
		}
	    else if (message instanceof EngineShapeUpdate)
		this.data.fallingShape = ((EngineShapeUpdate)message).shape;
	    else if (message instanceof EngineUpdate)
		this.data.update(((EngineUpdate)message).data);
	}
	catch (final InterruptedException err)
	{
	    System.err.println("Are you leaving?");
	    return;
	}
    }
    
}

