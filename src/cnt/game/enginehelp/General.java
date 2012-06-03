/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.game.enginehelp;
import cnt.game.*;
import cnt.messages.*;
import cnt.*;

import java.lang.ref.*;
import java.util.*;


/**
 * General game engine helper class
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class General
{
    /**
     * The possible, initial, shapes
     */
    private static final Shape[] POSSIBLE_SHAPES = {new TShape(), new IShape(), new OShape(),
						    new LShape(), new JShape(), new SShape(), new ZShape()};
    
    
    
    /**
     * Constructor
     * 
     * @param  engine  The game engine
     */
    public General(final Engine engine)
    {
	this.engine = engine;
	this.data = engine.data;
	this.reactor = engine.reactor;
    }
    
    
    
    /**
     * The game engine
     */
    private final Engine engine;
    
    /**
     * All data held by the engine
     */
    private final EngineData data;
    
    /**
     * Reactor engine
     */
    private final Reactor reactor;
    
    
    
    /**
     * Invoked when a player drops out, the falling block is removed
     * if the dropped out player is the playing player
     */
    public void playerDropped(final Player player)
    {
	if (player.equals(this.data.currentPlayer))
	{
	    this.data.currentPlayer = null;
	    this.data.thread.interrupt();
	    
	    this.data.patcher.patchAway(this.data.fallingShape);
	    
	    this.data.fallingShape = null;
	    this.engine.nextTurn();
	}
    }
    
    
    /**
     * Starts a new turn
     * 
     * @param  player  The player playing on the new turn
     */
    public void newTurn(final Player player)
    {
	synchronized (this.data.pauseMonitor)
	{
	    this.data.currentPlayer = player;
	    if (this.data.paused && player.equals(this.data.localPlayer))
		try
		{   this.data.pauseMonitor.wait();
		}
		catch (final InterruptedException err)
		{   //TODO what do we do know?
		}
	}
	
	try
	{
	    this.data.fallingShape = POSSIBLE_SHAPES[(int)(Math.random() * POSSIBLE_SHAPES.length) % POSSIBLE_SHAPES.length];
	    
	    HashMap<Shape, SoftReference<Shape>> playerShapeCache = this.data.shapeCache.get(player);
	    Shape nshape;
	    if (playerShapeCache == null)
	    {
	        this.data.shapeCache.put(player, playerShapeCache = new HashMap<Shape, SoftReference<Shape>>());
		playerShapeCache.put(this.data.fallingShape, new SoftReference<Shape>(nshape = this.data.fallingShape.clone()));
		nshape.setPlayer(player);
	    }
	    else
	    {
		SoftReference<Shape> ref = playerShapeCache.get(this.data.fallingShape);
		if ((ref == null) || ((nshape = ref.get()) == null))
		{
		    playerShapeCache.put(this.data.fallingShape, new SoftReference<Shape>(nshape = this.data.fallingShape.clone()));
		    nshape.setPlayer(player);
		}
	    }
	    this.data.fallingShape = nshape;
	}
	catch (final CloneNotSupportedException err)
	{   throw new Error("*Shape.clone() is not implemented");
	}
	catch (final Throwable err)
	{   throw new Error("*Shape.clone() is not implemented correctly");
	}
	
	for (int r = 0, rn = (int)(Math.random() * 4); r < rn; r++)
	    this.data.fallingShape.rotate(true);
	
	this.data.fallingShape.setX((Board.WIDTH - this.data.fallingShape.getBlockMatrix()[0].length) >> 1);
	this.data.fallingShape.setY(0);
	
	this.data.gameOver = this.data.board.canPut(this.data.fallingShape, false) == false;
	if (this.data.gameOver)
	    do  {this.data.fallingShape.setY(this.data.fallingShape.getY() - 1); System.err.println("one up");}
	      while (this.data.board.canPut(this.data.fallingShape, true) == false);
	
	this.data.moveAppliedMomento = this.data.moveInitialMomento = this.data.fallingShape.store();
	
	this.data.patcher.patchIn(this.data.fallingShape);
	if (this.data.gameOver)
	    this.data.patcher.dispatch();
    }
    
    
    /**
     * Stations the falling block and deletes empty rows
     */
    public void reaction()
    {
	this.data.patcher.patchIn(this.data.fallingShape);
	this.data.board.put(this.data.fallingShape);
	int scoreBefore = this.data.score;
	
	this.data.patcher.dispatch();
	this.reactor.reaction();
	
	if (this.data.slowDownScore >= 0)
	    while (this.data.score >= this.data.slowDownScore)
	    {
		this.data.slowDownScore <<= 1;
		this.data.sleepTime += 200;
	    }
	
	if (this.data.score != scoreBefore)
	    Blackboard.broadcastMessage(new GameScore(this.data.score));
	this.data.currentPlayer = null;
    }
    
    
    /**
     * Just like {@link Thread#sleep(int)}, but it also broadcasts all updates
     * 
     * @param  milliseconds  The number of milliseconds to sleep, <code>0</code> for not sleeping
     * 
     * @throws  InterruptedException  If the thread is interrupted
     */
    public void sleep(final double milliseconds) throws InterruptedException
    {
	synchronized (this.data.empauseMonitor)
	{
	    if (this.data.empaused)
		try
		{   this.data.empauseMonitor.wait();
		}
		catch (final InterruptedException err)
		{   //TODO what do we do now?
		}
	}
	
	this.data.patcher.dispatch();
	
	if (milliseconds >= 0.5)
	    Thread.sleep(milliseconds < 100. ? 100 : (int)(milliseconds + 0.5));
    }
    
}

