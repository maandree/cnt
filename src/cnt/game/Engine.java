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
public class Engine implements Blackboard.BlackboardObserver
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
    
    /**
     * The possible, initial, shapes
     */
    private static final Shape[] POSSIBLE_SHAPES = {new TShape(), new IShape(), new OShape(),
						    new LShape(), new JShape(), new SShape(), new ZShape()};
    
    
    
    //Has default constructor
    
    
    
    /**
     * All data held by the engine
     */
    final EngineData data = new EngineData();
    
    
    
    /**
     * Starts the engine
     */
    public void start()
    {
	this.data.gameOver = false;
	this.data.sleepTime = INITIAL_SLEEP_TIME / SLEEP_TIME_MULTIPLER; //the division will be nullified when the games starts by nextTurn()
	this.data.board = new Board();
	
	Blackboard.registerObserver(this);
	Blackboard.registerThreadingPolicy(this, Blackboard.DAEMON_THREADING,
					   GamePlayCommand.class,
					   EmergencyPause.class,
					   PlayerDropped.class,
					   PlayerPause.class,
					   NextPlayer.class);
	
	Blackboard.broadcastMessage(new GameScore(this.data.score = 0));
	
	this.data.thread = new Thread()
	        {
		    /**
		     * {@inheritDoc}
		     */
		    public void run()
		    {
			for (;;)
			{
			    Engine.this.nextTurn();
			    
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
				    {   if (Engine.this.fall() == false)
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
				    {   Engine.this.move();
				}   }
				catch (final InterruptedException err)
			        {   System.err.println("Are you leaving?");
				    return;
				}
			    }
			}
		    }
	        };
	
	this.data.thread.start();
    }
    
    
    /**
     * Invoked when a player drops out, the falling block is removed
     * if the dropped out player is the playing player
     */
    private void playerDropped(final Player player)
    {
	if (player.equals(this.data.currentPlayer))
	{
	    this.data.currentPlayer = null;
	    this.data.thread.interrupt();
	    
	    this.data.patcher.patchAway(this.data.fallingShape);
	    
	    this.data.fallingShape = null;
	    nextTurn();
	}
    }
    
    
    /**
     * Starts a new turn
     * 
     * @param  player  The player playing on the new turn
     */
    private void newTurn(final Player player)
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
	    //this.data.fallingShape = POSSIBLE_SHAPES[(int)(Math.random() * POSSIBLE_SHAPES.length) % POSSIBLE_SHAPES.length].clone();
	    //this.data.fallingShape.setPlayer(player);
	    
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
	    try
	    {
		this.sleep(0);
	    }
	    catch (final InterruptedException err)
	    {
		//Do nothing
	    }
    }
    
    
    /**
     * Makes the falling block drop on step and apply the, if any, registrered modification
     * 
     * @return  Whether the fall was not interrupted
     * 
     * @throws  InterruptedException  Can only indicate the the player is leaving
     */
    boolean fall() throws InterruptedException
    {
	this.sleep(0);
	
	if (this.data.fallingShape == null)
	{
	    System.err.println("What's happening, why do we not have a falling shape?");
	    return true;
	}
	
	this.data.patcher.patchAway(this.data.fallingShape);
	this.data.fallingShape.restore(this.data.moveInitialMomento = this.data.moveAppliedMomento);
	this.data.fallingShape.setY(this.data.fallingShape.getY() + 1);
	
	if (this.data.board.canPut(this.data.fallingShape, false) == false)
	{
	    this.data.fallingShape.restore(this.data.moveInitialMomento);
	    reaction();
	    this.data.fallingShape = null;
	    return false;
	}
	
	this.data.moveInitialMomento = this.data.moveAppliedMomento = this.data.fallingShape.store();
	
	this.data.patcher.patchIn(this.data.fallingShape);
	return true;
    }
    
    
    /**
     * Drops the falling block to the bottom
     * 
     * @throws  InterruptedException  Can only indicate the the player is leaving
     */
    private void drop() throws InterruptedException
    {
	if (this.data.fallingShape == null)
	{
	    System.err.println("What's happening, why do we not have a falling shape?");
	    return;
	}
	
	this.sleep(0);
	this.data.patcher.patchAway(this.data.fallingShape);
	this.data.fallingShape.restore(this.data.moveInitialMomento = this.data.moveAppliedMomento);
	
	for (;;)
	{
	    this.data.fallingShape.setY(this.data.fallingShape.getY() + 1);
	    
	    if (this.data.board.canPut(this.data.fallingShape, false) == false)
	    {
		this.data.fallingShape.setY(this.data.fallingShape.getY() - 1);
		reaction();
		this.data.fallingShape = null;
		return;
	    }
	}
    }
    
    
    /**
     * Applies any requested move, but does not make the shape fall
     * 
     * @throws  InterruptedException  Can only indicate the the player is leaving
     */
    void move() throws InterruptedException
    {
	this.sleep(0);
	
	if (this.data.fallingShape == null)
	{
	    System.err.println("What's happening, why do we not have a falling shape?");
	    return;
	}
	
	this.data.patcher.patchAway(this.data.fallingShape);
	this.data.fallingShape.restore(this.data.moveInitialMomento = this.data.moveAppliedMomento);
	this.data.patcher.patchIn(this.data.fallingShape);
    }
    
    
    /**
     * Registrers a rotation, if possible, to the falling block
     * 
     * @param  clockwise  Whether to rotate clockwise
     */
    private void rotate(final boolean clockwise)
    {
	if (this.data.fallingShape == null)
	{
	    System.err.println("What's happening, why do we not have a falling shape?");
	    return;
	}
	
	this.data.fallingShape.rotate(clockwise);
	
	if (this.data.board.canPut(this.data.fallingShape, false))
	    this.data.moveAppliedMomento = this.data.fallingShape.store();
	else
	    this.data.moveAppliedMomento = this.data.moveInitialMomento;
	
	this.data.fallingShape.restore(this.data.moveInitialMomento);
    }
    
    
    /**
     * Registrers a horizontal movement, if possible, to the falling block
     * 
     * @param  incrX  The value with which to increase the left position
     */
    private void move(final int incrX)
    {
	if (this.data.fallingShape == null)
	{
	    System.err.println("What's happening, why do we not have a falling shape?");
	    return;
	}
	
	this.data.fallingShape.setX(this.data.fallingShape.getX() + incrX);
	
	if (this.data.board.canPut(this.data.fallingShape, false))
	    this.data.moveAppliedMomento = this.data.fallingShape.store();
	else
	    this.data.moveAppliedMomento = this.data.moveInitialMomento;
	
	this.data.fallingShape.restore(this.data.moveInitialMomento);
    }
    
    
    /**
     * Stations the falling block and deletes empty rows
     * 
     * @throws  InterruptedException  Can only indicate the the player is leaving
     */
    private void reaction() throws InterruptedException
    {
	this.data.patcher.patchIn(this.data.fallingShape);
	this.data.board.put(this.data.fallingShape);
	boolean reacted = false;
	
	sleep(0);
	for (;;)
	{
	    final int[] full = this.data.board.getFullRows();
	    if (full.length == 0)
		break;
	    Arrays.sort(full);
	    
	    final boolean[][] fullLine = new boolean[1][Board.WIDTH];
	    for (int x = 0; x < Board.WIDTH; x++)
		fullLine[0][x] = true;
	    
	    final Block[][] matrix = this.data.board.getMatrix();
	    
	    int sub = 0;
	    int row = full[full.length - 1];
	    
	    for (int y = 0; y <= row; y++)
	    {
		this.data.patcher.patchAway(fullLine, 0, y);
		this.data.board.delete(fullLine, 0, y);
	    }
	    for (int y = sub; y < row; y++)
	    {
		this.data.patcher.patchIn(new Block[][] {matrix[y]}, 0, y + 1);
		this.data.board.put(new Block[][] {matrix[y]}, 0, y + 1);
	    }
	    sleep(0);
	    
	    reacted = true;
	    this.data.score += 10;
	}
	
	if (this.data.slowDownScore >= 0)
	    while (this.data.score >= this.data.slowDownScore)
	    {
		this.data.slowDownScore <<= 1;
		this.data.sleepTime += 200;
	    }
	
	if (reacted)
	    Blackboard.broadcastMessage(new GameScore(this.data.score));
	this.data.currentPlayer = null;
    }
    
    
    /**
     * Performs everthing needed for a new turn and
     * sends a request for letting the next player start
     */
    void nextTurn()
    {
	this.data.sleepTime *= SLEEP_TIME_MULTIPLER;
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
	
	int time = milliseconds < 0.5
	           ? 0 : milliseconds < 100.
	                 ? 100 : (int)(milliseconds + 0.5);
	
	if (time != 0)
	    Thread.sleep(time);
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void messageBroadcasted(final Blackboard.BlackboardMessage message)
    {
	try
	{
	    if (message instanceof GamePlayCommand)
	    {
		synchronized (Engine.class)
		{   switch (((GamePlayCommand)message).move)
		    {
			case LEFT:           move(-1);       break;
			case RIGHT:          move(1);        break;
			case CLOCKWISE:      rotate(true);   break;
			case ANTICLOCKWISE:  rotate(false);  break;
			    
			case DOWN:
			    if (fall() == false)
				this.data.thread.interrupt();
			    break;
			    
			case DROP:
			    drop();
			    this.data.thread.interrupt();
			    break;
			    
			default:
			    throw new Error("Unrecognised GamePlayCommand.");
		}   }
	    }
	    else if (message instanceof NextPlayer) /* do not thread */
	    {
		if (((NextPlayer)message).player != null)
		{
		    synchronized (this.data.empauseMonitor)
		    {
			if (this.data.empaused)
			    try
			    {   this.data.empauseMonitor.wait();
			    }
			    catch (final InterruptedException err)
			    {   //TODO what do we do know?
			    }
		    }
		    newTurn(((NextPlayer)message).player);
		}
	    }
	    else if (message instanceof PlayerDropped)
		playerDropped(((PlayerDropped)message).player);
	    else if (message instanceof LocalPlayer)
		this.data.localPlayer = ((LocalPlayer)message).player;
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
	}
	catch (final InterruptedException err)
	{
	    System.err.println("Are you leaving?");
	    return;
	}
    }
    
}

