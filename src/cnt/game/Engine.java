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
     * The current board with all stationed blocks
     */
    private Board board = null;
    
    /**
     * The current falling shape
     */
    private Shape fallingShape = null;
    
    /**
     * The current player
     */
    private Player currentPlayer = null;
    
    /**
     * The local player
     */
    private Player localPlayer = null;
    
    /**
     * The momento of the falling shape at the beginning of the move
     */
    private Shape.Momento moveInitialMomento = null;
    
    /**
     * The momento of the falling shape at the end of the move
     */
    private Shape.Momento moveAppliedMomento = null;
    
    /**
     * The interval between falls
     */
    private double sleepTime = INITIAL_SLEEP_TIME;
    
    /**
     * The game thread
     */
    private Thread thread = null;
    
    /**
     * Whether the game is over
     */
    private boolean gameOver = true;
    
    /**
     * Shape for shapes with set player
     */
    private final WeakHashMap<Player, HashMap<Shape, SoftReference<Shape>>> shapeCache = new WeakHashMap<Player, HashMap<Shape, SoftReference<Shape>>>();
    
    /**
     * The current game score
     */
    private int score;
    
    /**
     * The next score at which to slow down the speed
     */
    private int slowDownScore = 1000;
    
    /**
     * Whether the current player is in pause mode
     */
    private boolean paused = false;
    
    /**
     * Pause monitor
     */
    private final Object pauseMonitor = new Object();
    
    /**
     * Whether the game is in emergency pause mode
     */
    private boolean empaused = false;
    
    /**
     * Emergancy pause monitor
     */
    private final Object empauseMonitor = new Object();
    
    /**
     * Matrix patcher
     */
    private final Patcher patcher = new Patcher();
    
    
    
    /**
     * Starts the engine
     */
    public void start()
    {
	gameOver = false;
	sleepTime = INITIAL_SLEEP_TIME / SLEEP_TIME_MULTIPLER; //the division will be nullified when the games starts by nextTurn()
	board = new Board();
	
	final Engine blackboardObserver = new Engine();
	Blackboard.registerObserver(blackboardObserver);
	Blackboard.registerThreadingPolicy(blackboardObserver, Blackboard.DAEMON_THREADING,
					   GamePlayCommand.class,
					   EmergencyPause.class,
					   PlayerDropped.class,
					   PlayerPause.class,
					   NextPlayer.class);
	
	Blackboard.broadcastMessage(new GameScore(score = 0));
	
	thread = new Thread()
	        {
		    /**
		     * {@inheritDoc}
		     */
		    public void run()
		    {
			for (;;)
			{
			    Engine.this.nextTurn();
			    
			    if (Engine.this.gameOver)
			    {
				Blackboard.broadcastMessage(new GameOver());
				Blackboard.unregisterObserver(blackboardObserver);
				return;
			    }
			    
			    for (;;)
			    {
				try
				{   Engine.this.sleep(Engine.this.sleepTime / 2);
				}
				catch (final InterruptedException err)
				{   if (Engine.this.currentPlayer == null)
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
				{   Engine.this.sleep(Engine.this.sleepTime / 2);
				}
				catch (final InterruptedException err)
				{   if (Engine.this.currentPlayer == null)
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
	
	thread.start();
    }
    
    
    /**
     * Broadcasts a matrix patch that removes a shape
     * 
     * @param  shape  The shape to remove
     */
    private void patchAway(final Shape shape)
    {   patcher.patchAway(shape);
    }
    
    /**
     * Broadcasts a matrix patch that removes a set of blocks
     * 
     * @param  blocks  The block pattern
     * @param  offX    Offset on the x-axis
     * @param  offY    Offset on the y-axis
     */
    private void patchAway(final boolean[][] blocks, final int offX, final int offY)
    {   patcher.patchAway(blocks, offX, offY);
    }
    
    /**
     * Broadcasts a matrix patch that adds a shape
     * 
     * @param  shape  The shape to add
     */
    private void patchIn(final Shape shape)
    {   patcher.patchIn(shape);
    }
    
    /**
     * Broadcasts a matrix patch that adds a set of blocks
     * 
     * @param  blocks  The block pattern
     * @param  offX    Offset on the x-axis
     * @param  offY    Offset on the y-axis
     */
    private void patchIn(final Block[][] blocks, final int offX, final int offY)
    {   patcher.patchIn(blocks, offX, offY);
    }
    
    
    /**
     * Invoked when a player drops out, the falling block is removed
     * if the dropped out player is the playing player
     */
    private void playerDropped(final Player player)
    {
	if (player.equals(currentPlayer))
	{
	    currentPlayer = null;
	    thread.interrupt();
	    
	    patchAway(fallingShape);
	    
	    fallingShape = null;
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
	synchronized (pauseMonitor)
	{
	    currentPlayer = player;
	    if (paused && player.equals(localPlayer))
		try
		{   pauseMonitor.wait();
		}
		catch (final InterruptedException err)
		{   //TODO what do we do know?
		}
	}
	
	try
	{
	    //fallingShape = POSSIBLE_SHAPES[(int)(Math.random() * POSSIBLE_SHAPES.length) % POSSIBLE_SHAPES.length].clone();
	    //fallingShape.setPlayer(player);
	    
	    fallingShape = POSSIBLE_SHAPES[(int)(Math.random() * POSSIBLE_SHAPES.length) % POSSIBLE_SHAPES.length];
	    
	    HashMap<Shape, SoftReference<Shape>> playerShapeCache = shapeCache.get(player);
	    Shape nshape;
	    if (playerShapeCache == null)
	    {
	        shapeCache.put(player, playerShapeCache = new HashMap<Shape, SoftReference<Shape>>());
		playerShapeCache.put(fallingShape, new SoftReference<Shape>(nshape = fallingShape.clone()));
		nshape.setPlayer(player);
	    }
	    else
	    {
		SoftReference<Shape> ref = playerShapeCache.get(fallingShape);
		if ((ref == null) || ((nshape = ref.get()) == null))
		{
		    playerShapeCache.put(fallingShape, new SoftReference<Shape>(nshape = fallingShape.clone()));
		    nshape.setPlayer(player);
		}
	    }
	    fallingShape = nshape;
	}
	catch (final CloneNotSupportedException err)
	{   throw new Error("*Shape.clone() is not implemented");
	}
	catch (final Throwable err)
	{   throw new Error("*Shape.clone() is not implemented correctly");
	}
	
	for (int r = 0, rn = (int)(Math.random() * 4); r < rn; r++)
	    fallingShape.rotate(true);
	
	fallingShape.setX((Board.WIDTH - fallingShape.getBlockMatrix()[0].length) >> 1);
	fallingShape.setY(0);
	
	gameOver = board.canPut(fallingShape, false) == false;
	if (gameOver)
	    do  {fallingShape.setY(fallingShape.getY() - 1); System.err.println("one up");}
	      while (board.canPut(fallingShape, true) == false);
	
	moveAppliedMomento = moveInitialMomento = fallingShape.store();
	
	patchIn(fallingShape);
	if (gameOver)
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
	
	if (fallingShape == null)
	{
	    System.err.println("What's happening, why do we not have a falling shape?");
	    return true;
	}
	
	patchAway(fallingShape);
	fallingShape.restore(moveInitialMomento = moveAppliedMomento);
	fallingShape.setY(fallingShape.getY() + 1);
	
	if (board.canPut(fallingShape, false) == false)
	{
	    fallingShape.restore(moveInitialMomento);
	    reaction();
	    fallingShape = null;
	    return false;
	}
	
	moveInitialMomento = moveAppliedMomento = fallingShape.store();
	
	patchIn(fallingShape);
	return true;
    }
    
    
    /**
     * Drops the falling block to the bottom
     * 
     * @throws  InterruptedException  Can only indicate the the player is leaving
     */
    private void drop() throws InterruptedException
    {
	if (fallingShape == null)
	{
	    System.err.println("What's happening, why do we not have a falling shape?");
	    return;
	}
	
	this.sleep(0);
	patchAway(fallingShape);
	fallingShape.restore(moveInitialMomento = moveAppliedMomento);
	
	for (;;)
	{
	    fallingShape.setY(fallingShape.getY() + 1);
	    
	    if (board.canPut(fallingShape, false) == false)
	    {
		fallingShape.setY(fallingShape.getY() - 1);
		reaction();
		fallingShape = null;
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
	
	if (fallingShape == null)
	{
	    System.err.println("What's happening, why do we not have a falling shape?");
	    return;
	}
	
	patchAway(fallingShape);
	fallingShape.restore(moveInitialMomento = moveAppliedMomento);	
	patchIn(fallingShape);
    }
    
    
    /**
     * Registrers a rotation, if possible, to the falling block
     * 
     * @param  clockwise  Whether to rotate clockwise
     */
    private void rotate(final boolean clockwise)
    {
	if (fallingShape == null)
	{
	    System.err.println("What's happening, why do we not have a falling shape?");
	    return;
	}
	
	fallingShape.rotate(clockwise);
	
	if (board.canPut(fallingShape, false))
	    moveAppliedMomento = fallingShape.store();
	else
	    moveAppliedMomento = moveInitialMomento;
	
	fallingShape.restore(moveInitialMomento);
    }
    
    
    /**
     * Registrers a horizontal movement, if possible, to the falling block
     * 
     * @param  incrX  The value with which to increase the left position
     */
    private void move(final int incrX)
    {
	if (fallingShape == null)
	{
	    System.err.println("What's happening, why do we not have a falling shape?");
	    return;
	}
	
	fallingShape.setX(fallingShape.getX() + incrX);
	
	if (board.canPut(fallingShape, false))
	    moveAppliedMomento = fallingShape.store();
	else
	    moveAppliedMomento = moveInitialMomento;
	
	fallingShape.restore(moveInitialMomento);
    }
    
    
    /**
     * Stations the falling block and deletes empty rows
     * 
     * @throws  InterruptedException  Can only indicate the the player is leaving
     */
    private void reaction() throws InterruptedException
    {
	patchIn(fallingShape);
	board.put(fallingShape);
	boolean reacted = false;
	
	sleep(0);
	for (;;)
	{
	    final int[] full = board.getFullRows();
	    if (full.length == 0)
		break;
	    Arrays.sort(full);
	    
	    final boolean[][] fullLine = new boolean[1][Board.WIDTH];
	    for (int x = 0; x < Board.WIDTH; x++)
		fullLine[0][x] = true;
	    
	    final Block[][] matrix = board.getMatrix();
	    
	    int sub = 0;
	    int row = full[full.length - 1];
	    
	    for (int y = 0; y <= row; y++)
	    {
		patchAway   (fullLine, 0, y);
		board.delete(fullLine, 0, y);
	    }
	    for (int y = sub; y < row; y++)
	    {
		patchIn  (new Block[][] {matrix[y]}, 0, y + 1);
		board.put(new Block[][] {matrix[y]}, 0, y + 1);
	    }
	    sleep(0);
	    
	    reacted = true;
	    score += 10;
	}
	
	if (slowDownScore >= 0)
	    while (score >= slowDownScore)
	    {
		slowDownScore <<= 1;
		sleepTime += 200;
	    }
	
	if (reacted)
	    Blackboard.broadcastMessage(new GameScore(score));
	currentPlayer = null;
    }
    
    
    /**
     * Performs everthing needed for a new turn and
     * sends a request for letting the next player start
     */
    void nextTurn()
    {
	sleepTime = sleepTime * SLEEP_TIME_MULTIPLER;
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
	synchronized (empauseMonitor)
	{
	    if (empaused)
		try
		{   empauseMonitor.wait();
		}
		catch (final InterruptedException err)
		{   //TODO what do we do now?
		}
	}
	
	patcher.dispatch();
	
	int time = milliseconds < 0.5 ? 0 : milliseconds < 100. ? 100 : (int)(milliseconds + 0.5);
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
				thread.interrupt();
			    break;
			    
			case DROP:
			    drop();
			    thread.interrupt();
			    break;
			    
			default:
			    throw new Error("Unrecognised GamePlayCommand.");
		}   }
	    }
	    else if (message instanceof NextPlayer) /* do not thread */
	    {
		if (((NextPlayer)message).player != null)
		{
		    synchronized (empauseMonitor)
		    {
			if (empaused)
			    try
			    {   empauseMonitor.wait();
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
		localPlayer = ((LocalPlayer)message).player;
	    else if (message instanceof PlayerPause)
		synchronized (pauseMonitor)
		{
		    if (((PlayerPause)message).player.equals(localPlayer))
		    {
			paused = ((PlayerPause)message).paused;
			if (paused == false)
			    pauseMonitor.notifyAll();
		    }
		}
	    else if (message instanceof EmergencyPause)
		synchronized (empauseMonitor)
		{
		    empaused = ((EmergencyPause)message).paused;
		    if (empaused == false)
			empauseMonitor.notifyAll();
		}
	}
	catch (final InterruptedException err)
	{
	    System.err.println("Are you leaving?");
	    return;
	}
    }
    
}

