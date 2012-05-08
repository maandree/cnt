/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.game;
import cnt.*;
import cnt.mock.Board;

//TODO  Do send shape removal followed by shape adding, send then togather, may be by merge all at each sleep


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
    private static final int INITIAL_SLEEP_TIME = 1000;
    
    /**
     * The value to multiply the sleep time each time the game speeds up;
     * should be slighly less than 1.
     */
    private static final double SLEEP_TIME_MULTIPLER = 0.98;
    
    /**
     * The possible, initial, shapes
     */
    private static final Shape[] POSSIBLE_SHAPES = {Shape.T_SHAPE, Shape.PIPE_SHAPE, Shape.SQUARE_SHAPE,
						    Shape.L_SHAPE, Shape.J_SHAPE, Shape.S_SHAPE, Shape.Z_SHAPE};
    
    
    
    /**
     * <p>Constructor.</p>
     * <p>
     *   Used for {@link Blackboard} listening.
     * </p>
     */
    private Engine()
    {
        //Privatise default constructor
    }
    
    
    
    /**
     * The current board with all stationed blocks
     */
    private static Board board = null;
    
    /**
     * The current falling shape
     */
    private static Shape fallingShape = null;
    
    /**
     * The current player
     */
    private static Player currentPlayer = null;
    
    /**
     * The momento of the falling shape at the beginning of the move
     */
    private static Shape.Momento moveInitialMomento = null;
    
    /**
     * The momento of the falling shape at the end of the move
     */
    private static Shape.Momento moveAppliedMomento = null;
    
    /**
     * The interval between falls
     */
    private static int sleepTime = INITIAL_SLEEP_TIME;
    
    /**
     * Help monitor for the game thread, used to notify when a player has been found
     */
    private static Object threadingMonitor = new Object();
    
    /**
     * The game thread
     */
    private static Thread thread = null;
    
    /**
     * Whether the game is over
     */
    private static boolean gameOver = true;
    
    
    
    /**
     * Starts the engine
     */
    public static void start()
    {
	gameOver = false;
	sleepTime = (int)(INITIAL_SLEEP_TIME / SLEEP_TIME_MULTIPLER); //the division will be nullified when the games starts by nextTurn()
	board = new Board();
	
	final Engine blackboardObserver = new Engine();
	Blackboard.registerObserver(blackboardObserver);
	Blackboard.registerThreadingPolicy(blackboardObserver, Blackboard.DAEMON_THREADING,
					   Blackboard.GamePlayCommand.class,
					   Blackboard.PlayerDropped.class,
					   Blackboard.NextPlayer.class);
	
	thread = new Thread()
	        {
		    /**
		     * {@inheritDoc}
		     */
		    @Override
		    public void run()
		    {
			for (;;)
			{
			    synchronized (Engine.threadingMonitor)
			    {
				try
				{
				    Engine.threadingMonitor.wait();
				}
				catch (final InterruptedException err)
			        {
				    System.err.println("Are you leaving?");
				    return;
				}
				
				if (Engine.gameOver)
				    {}
			    }
			    
			    for (;;)
			    {
				try
				{
				    Thread.sleep(Engine.sleepTime);
				}
				catch (final InterruptedException err)
				{
				    if (Engine.currentPlayer == null)
					break;
				    continue;
				}
				
				try
				{
				    synchronized (Engine.class)
				    {
					if (Engine.fall() == false)
					    break;
				    }
				}
				catch (final InterruptedException err)
			        {
				    System.err.println("Are you leaving?");
				    return;
				}
			    }
			}
		    }
	        };
	
	thread.start();
	
	nextTurn();
    }
    
    
    /**
     * Broadcasts a matrix patch that removes a shape
     * 
     * @param  shape  The shape to remove
     */
    private static void patchAway(final Shape shape)
    {
	final int offX = shape.getX();
	final int offY = shape.getY();
	final boolean[][] blocks = shape.getBooleanMatrix();
	patchAway(blocks, offX, offY);
    }
    
    
    /**
     * Broadcasts a matrix patch that removes a set of blocks
     * 
     * @param  blocks  The block pattern
     * @param  offX    Offset on the x-axis
     * @param  offY    Offset on the y-axis
     */
    private static void patchAway(final boolean[][] blocks, final int offX, final int offY)
    {
	final Blackboard.MatrixPatch patch = new Blackboard.MatrixPatch(blocks, (Block[][])null, offY, offX);
	Blackboard.broadcastMessage(patch);
    }
    
    
    /**
     * Broadcasts a matrix patch that adds a shape
     * 
     * @param  shape  The shape to add
     */
    private static void patchIn(final Shape shape)
    {
	final int offX = shape.getX();
	final int offY = shape.getY();
	final Block[][] blocks = shape.getBlockMatrix();
	patchIn(blocks, offX, offY);
    }
    
    
    /**
     * Broadcasts a matrix patch that adds a set of blocks
     * 
     * @param  blocks  The block pattern
     * @param  offX    Offset on the x-axis
     * @param  offY    Offset on the y-axis
     */
    private static void patchIn(final Block[][] blocks, final int offX, final int offY)
    {
	final Blackboard.MatrixPatch patch = new Blackboard.MatrixPatch(null, blocks, offY, offX);
	Blackboard.broadcastMessage(patch);
    }
    
    
    /**
     * Invoked when a player drops out, the falling block is removed
     * if the dropped out player is the playing player
     */
    private static void playerDropped(final Player player)
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
    private static void newTurn(final Player player)
    {
	try
	{
	    fallingShape = POSSIBLE_SHAPES[(int)(Math.random() * POSSIBLE_SHAPES.length)].clone();
	}
	catch (final CloneNotSupportedException err)
	{
	    throw new Error("Shape.clone() is not implemented");
	}
	
	fallingShape.setPlayer(currentPlayer = player);
	
	gameOver = board.canPut(fallingShape, false) == false;
	if (gameOver)
	    do  fallingShape.setY(fallingShape.getY() - 1);
	      while (board.canPut(fallingShape, true) == false);
	
	moveAppliedMomento = moveInitialMomento = fallingShape.store();
	
	synchronized (threadingMonitor)
	{
	    threadingMonitor.notify();
	}
    }
    
    
    /**
     * Makes the falling block drop on step and apply the, if any, registrered modification
     * 
     * @param   return  Whether the fall was not interrupted
     * 
     * @throws  InterruptedException  Can only indicate the the player is leaving
     */
    private static boolean fall() throws InterruptedException
    {
	patchAway(fallingShape);
	fallingShape.restore(moveInitialMomento = moveAppliedMomento);
	fallingShape.setY(fallingShape.getY() + 1);
	
	if (board.canPut(fallingShape, false) == false)
	{
	    fallingShape.restore(moveInitialMomento);
	    reaction();
	    return false;
	}
	
	patchIn(fallingShape);
	return true;
    }
    
    
    /**
     * Drops the falling block to the bottom
     * 
     * @throws  InterruptedException  Can only indicate the the player is leaving
     */
    private static void drop() throws InterruptedException
    {
	patchAway(fallingShape);
	fallingShape.restore(moveInitialMomento = moveAppliedMomento);
	
	for (int i = 1;; i++)
	{
	    fallingShape.setY(fallingShape.getY() + i);
	    
	    if (board.canPut(fallingShape, false) == false)
	    {
		fallingShape.restore(moveInitialMomento);
		reaction();
		return;
	    }
	}
    }
    
    
    /**
     * Registrers a rotation, if possible, to the falling block
     * 
     * @param  clockwise  Whether to rotate clockwise
     */
    private static void rotate(final boolean clockwise)
    {
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
    private static void move(final int incrX)
    {
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
    private static void reaction() throws InterruptedException
    {
	patchIn(fallingShape);
	board.put(fallingShape);
	fallingShape = null;
	
	final int[] full = board.getFullRows();
	
	if (full.length > 0)
	{
	    final boolean[][] fullLine = new boolean[1][Board.WIDTH];
	    for (int x = 0; x < Board.WIDTH; x++)
		fullLine[0][x] = true;
	    
	    for (final int row : full)
	    {
		patchAway(fullLine, 0, row);
		board.delete(fullLine, 0, row);
	    }
	}
	
	final Block[][] matrix = board.getMatrix();
	
	int sub = 0;
	for (final int row : full)
	{
	    Thread.sleep(sleepTime);
	    
	    final Block[][] move = new Block[row - sub][];
		
	    for (int y = 0, n = row - sub; y < n; y++)
		move[y] = matrix[y + sub];
	    
	    sub++;
	    
	    patchIn(move, 0, sub);
	    board.put(move, 0, sub);
	}
	
	nextTurn();
    }
    
    
    /**
     * Performs everthing needed for a new turn and
     * sends a request for letting the next player start
     */
    private static void nextTurn()
    {
	sleepTime = (int)(sleepTime * SLEEP_TIME_MULTIPLER);
	
	Blackboard.broadcastMessage(new Blackboard.NextPlayer(null));
    }
    
    
    /**
     * {@inheritDoc}
     */
    public synchronized void messageBroadcasted(final Blackboard.BlackboardMessage message)
    {
	try
	{
	    if (message instanceof Blackboard.GamePlayCommand)
		synchronized (Engine.class)
		{
		    switch (((Blackboard.GamePlayCommand)message).move)
		    {
			case LEFT:           move(-1);       break;
			case RIGHT:          move(1);        break;
			case DROP:           drop();         break;
			case CLOCKWISE:      rotate(true);   break;
			case ANTICLOCKWISE:  rotate(false);  break;
			case DOWN:
			    if (fall() == false)
				thread.interrupt();
			    break;
			    
			default:
			    throw new Error("Unrecognised GamePlayCommand.");
		    }
		}
	    else if (message instanceof Blackboard.NextPlayer)     newTurn(((Blackboard.NextPlayer)message).player);
	    else if (message instanceof Blackboard.PlayerDropped)  playerDropped(((Blackboard.PlayerDropped)message).player);
	}
	catch (final InterruptedException err)
	{
	    System.err.println("Are you leaving?");
	    return;
	}
    }
    
}

