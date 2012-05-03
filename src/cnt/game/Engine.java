/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.game;
import cnt.*;


/**
 * Game engine main class
 */
public class Engine implements Blackboard.BlackboardObserver
{
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
     * Starts the engine
     */
    public static void start()
    {
    }
    
    
    /**
     * Invoked when a player drops out, the falling block is removed
     * if the dropped out player is the playing player
     */
    private static void playerDropped(final Player player)
    {
    }
    
    
    /**
     * Starts a new turn
     * 
     * @param  player  The player playing on the new turn
     */
    private static void newTurn(final Player player)
    {
    }
    
    
    /**
     * Makes the falling block drop on step and apply the, if any, registrered modification
     */
    private static void fall()
    {
    }
    
    
    /**
     * Drops the falling block to the bottom
     */
    private static void drop()
    {
    }
    
    
    /**
     * Registrers a rotation, if possible, to the falling block
     * 
     * @param  clockwise  Whether to rotate clockwise
     */
    private static void rotate(final boolean clockwise)
    {
    }
    
    
    /**
     * Registrers a horizontal movement, if possible, to the falling block
     * 
     * @param  incrX  The value with which to increase the left position
     */
    private static void move(final int incrX)
    {
    }
    
    
    /**
     * Stations the falling block and deletes empty rows
     */
    private static void reaction()
    {
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void messageBroadcasted(final Blackboard.BlackboardMessage message)
    {
    }
    
    
}

