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


/**
 * Game engine main class
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class Engine implements Blackboard.BlackboardObserver
{
    /**
     * The initial interval between falls;
     */
    private static final int INITIAL_SLEEP_TIME = 1000;
    
    /**
     * The possible, initial, shapes
     */
    private static final Shape[] POSSIBLE_SHAPES = {Shape.T_SHAPE, Shape.PIPE_SHAPE, Shape.SQUARE_SHAPE,
						    Shape.L_SHAPE, Shape.J_SHAPE, Shape.S_SHAPE, Shape.Z_SHAPE};
    
    
    
    /**
     * <p>Constructor</p>
     * <p>
     *   Used for {@link Blackboard} listening
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
     * Starts the engine
     */
    public static void start()
    {
	sleepTime = INITIAL_SLEEP_TIME;
	board = new Board();
	
	final Engine blackboardObserver = new Engine();
	Blackboard.registerObserver(blackboardObserver);
	Blackboard.registerThreadingPolicy(blackboardObserver, Blackboard.GamePlayMessage.class, Blackboard.DAEMON_THREADING);
	
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
				    if (Engine.fall() == false)
					break;
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
     * Invoked when a player drops out, the falling block is removed
     * if the dropped out player is the playing player
     */
    private static void playerDropped(final Player player)
    {
	if (player.equals(currentPlayer))
	{
	    currentPlayer = null;
	    thread.interrupt();
	    //FIXME: patch away falling shape               #########################################################################################################
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
	fallingShape.restore(moveInitialMomento = moveAppliedMomento);
	
	fallingShape.setY(fallingShape.getY() + 1);
	
	if (board.canPut(fallingShape, false) == false)
	{
	    fallingShape.restore(moveInitialMomento);
	    reaction();
	    return false;
	}
	
	return true;
    }
    
    
    /**
     * Drops the falling block to the bottom
     * 
     * @throws  InterruptedException  Can only indicate the the player is leaving
     */
    private static void drop() throws InterruptedException
    {
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
	board.put(fallingShape);
	fallingShape = null;
	
	final int[] full = board.getFullRows();
	
	if (full.length > 0)
	{
	    final boolean[][] fullLine = new boolean[1][Board.WIDTH];
	    for (int x = 0; x < Board.WIDTH; x++)
		fullLine[0][x] = true;
	    
	    for (final int row : full)
		board.delete(fullLine, 0, row);
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
	    
	    board.put(move, 0, sub);
	}
	
	nextTurn();
    }
    
    
    /**
     * Sends a request for letting the next player start
     */
    private static void nextTurn()
    {
	//REQUEST NEXT PLAYER
    }
    
    
    /**
     * {@inheritDoc}
     */
    public synchronized void messageBroadcasted(final Blackboard.BlackboardMessage message)
    {
	try
	{
	    if (message instanceof Blackboard.GamePlayMessage)
	    {
		switch (((Blackboard.GamePlayMessage)message).move)
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
			throw new Error("Unrecognised GamePlayMessage.");
		}
	    }
	    
	    //NEXT PLAYER         newTurn(Player);
	    //PLAYER DROP         playerDropped(Player);
	}
	catch (final InterruptedException err)
	{
	    System.err.println("Are you leaving?");
	    return;
	}
    }
    
}

