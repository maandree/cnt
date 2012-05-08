/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt;
import cnt.game.*;

import java.util.*;
import java.awt.Color;
import java.io.Serializable;


/**
 * Overall game blackboard
 *
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class Blackboard
{
    /**
     * Do not thread
     */
    public static final ThreadingPolicy NO_THREADING = null;
    
    /**
     * Normal thread
     */
    public static final ThreadingPolicy THREADED;
    
    /**
     * Daemon thread
     */
    public static final ThreadingPolicy DAEMON_THREADING;
    
    /**
     * Nice thread
     */
    public static final ThreadingPolicy NICE_THREADING;
    
    /**
     * Nice daemon thread
     */
    public static final ThreadingPolicy NICE_DAEMON_THREADING;
    
    
    
    /**
     * Non-constructor
     */
    private Blackboard()
    {
	assert false : "You may not create instances of this class [Blackboard].";
    }
    
    
    
    /**
     * Class initialiser
     */
    static
    {
	THREADED = new ThreadingPolicy()
	        {
		    /**
		     * {@inheritDoc}
		     */
		    public Thread createThread(final Runnable runnable)
		    {
			final Thread thread = new Thread(runnable);
			thread.setDaemon(false);
			thread.setPriority(5); //normal: 5 of 1..10; corresponding nice value: 0
			return thread;
		    }
	        };
	
	DAEMON_THREADING = new ThreadingPolicy()
	        {
		    /**
		     * {@inheritDoc}
		     */
		    public Thread createThread(final Runnable runnable)
		    {
			final Thread thread = new Thread(runnable);
			thread.setDaemon(true);
			thread.setPriority(5); //normal: 5 of 1..10; corresponding nice value: 0
			return thread;
		    }
	        };

	NICE_THREADING = new ThreadingPolicy()
	        {
		    /**
		     * {@inheritDoc}
		     */
		    public Thread createThread(final Runnable runnable)
		    {
			final Thread thread = new Thread(runnable);
			thread.setDaemon(false);
			thread.setPriority(2); //below normal: 2 of 1..10; corresponding nice value: 3
			return thread;
		    }
	        };
	
	NICE_DAEMON_THREADING = new ThreadingPolicy()
	        {
		    /**
		     * {@inheritDoc}
		     */
		    public Thread createThread(final Runnable runnable)
		    {
			final Thread thread = new Thread(runnable);
			thread.setDaemon(true);
			thread.setPriority(2); //below normal: 2 of 1..10; corresponding nice value: 3
			return thread;
		    }
	        };
    }
    
    
    
    /**
     * Registrered observers
     */
    private static WeakHashMap<BlackboardObserver, Void> observers = new WeakHashMap<BlackboardObserver, Void>(); //simulate the missing cass: WeakHashSet
    
    /**
     * How to thread message observations
     */
    private static WeakHashMap<BlackboardObserver, HashMap<Class<? extends BlackboardMessage>, ThreadingPolicy>> observationThreading =
	       new WeakHashMap<BlackboardObserver, HashMap<Class<? extends BlackboardMessage>, ThreadingPolicy>>();
    
    
    
    /**
     * This interface is used for all event handled by the enclosing class
     */
    public static interface BlackboardMessage extends Serializable
    {
	//Marker interface
    }
    
    
    /**
     * This interface makes observersion on the enclosing class possible
     * 
     * @author  Mattias Andrée, <a href="maandree@kth.se">maandree@kth.se</a>
     */
    public static interface BlackboardObserver
    {
	/**
	 * This method is invoked when the a message is pinned on the blackboard
	 */
	public void messageBroadcasted(final Blackboard.BlackboardMessage message);
    }
    
    /**
     * Message observation threading policy
     */
    public static interface ThreadingPolicy
    {
	/**
	 * Creates a thread according to the policy
	 *
	 * @param   runnable  The 'run' implementation of the thread
	 * @return            The new thread
	 */
	public Thread createThread(final Runnable runnable);
    }
    
    
    
    /**
     * Registers a message type-wide observer
     *
     * @param  observer  The observer to register
     */
    public static void registerObserver(final BlackboardObserver observer)
    {
	observers.put(observer, null);
    }
    
    /**
     * Unregisters a message type-wide observer
     *
     * @param  observer  The observer to unregister
     */
    public static void unregisterObserver(final BlackboardObserver observer)
    {
	observers.remove(observer);
	observationThreading.remove(observer);
    }
    
    /**
     * Registers a threading policy for an observer and a message type
     * 
     * @param  observer     The observer
     * @param  messageType  The message type
     * @param  policy       The threading policy
     */
    public static void registerThreadingPolicy(final BlackboardObserver observer, final Class<? extends BlackboardMessage> messageType, final ThreadingPolicy policy)
    {
	HashMap<Class<? extends BlackboardMessage>, ThreadingPolicy> map = observationThreading.get(observer);
	if (map == null)
	{
	    map = new HashMap<Class<? extends BlackboardMessage>, ThreadingPolicy>();
	    observationThreading.put(observer, map);
	}
	map.put(messageType, policy);
    }
    
    /**
     * Broadcasts a message to all observers
     * 
     * @param  message  The message to broadcast
     */
    public static void broadcastMessage(final BlackboardMessage message)
    {
	final ArrayList<Thread> threads = new ArrayList<Thread>();
	
	for (final BlackboardObserver observer : observers.keySet())
	{
	    final ThreadingPolicy policy;
	    final Runnable runnable = new Runnable()
		    {
			/**
			 * {@inheritDoc}
			 */
			public void run()
			{
			    observer.messageBroadcasted(message);
			}
		    };
	    
	    final HashMap<Class<? extends BlackboardMessage>, ThreadingPolicy> map = observationThreading.get(observer);
	    if (map == null)
		policy = null;
	    else
		policy = map.get(message.getClass());
	    
	    if (policy == null)
		runnable.run();
	    else
		threads.add(policy.createThread(runnable));
	}
	
	for (final Thread thread : threads)
	    thread.start();
    }
    
    
    
    /**
     * Game matrix patch class
     * 
     * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
     */
    public static final class MatrixPatch implements BlackboardMessage
    {
	/**
	 * Constructor
	 * 
	 * @param  erase   A matrix where <code>true</code> indicates removal of block
	 * @param  blocks  A matrix where non-<code>null</code> indicates to add a block
	 * @param  offY    Top offset, where the first row in the matrices affect the game matrix
	 * @param  offX    Left offset, where the first column in the matrices affect the game matrix
	 */
	public MatrixPatch(final boolean[][] erase, final Color[][] blocks, final int offY, final int offX)
	{
	    this.erase = erase;
	    this.blocks = blocks;
	    this.offY = offY;
	    this.offX = offX;
	}
	
	
	
	/**
	 * A matrix where <code>true</code> indicates removal of block
	 */
	public final boolean[][] erase;
	
	/**
	 * A matrix where non-<code>null</code> indicates to add a block
	 */
	public final Color[][] blocks;
	
	/**
	 * Top offset, where the first row in the matrices affect the game matrix
	 */
	public final int offY;
	
	/**
	 * Left offset, where the first column in the matrices affect the game matrix
	 */
	public final int offX;
	
    }
    
    
    /**
     * Chat message class
     * 
     * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
     */
    public static final class ChatMessage implements BlackboardMessage
    {
	/**
	 * Constructor
	 * 
	 * @param  player   The player sending the message
	 * @param  colour   The colour of the player
	 * @param  message  The message
	 */
	public ChatMessage(final String player, final Color colour, final String message)
	{
	    this.player = player;
	    this.colour = colour;
	    this.message = message;
	}
	
	
	
	/**
	 * The player sending the message
	 */
	public final String player;
	
	/**
	 * The colour of the player
	 */
	public final Color colour;
	
	/**
	 * The message
	 */
	public final String message;
	
    }
    
    
    /**
     * System message class
     * 
     * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
     */
    public static final class SystemMessage implements BlackboardMessage
    {
	/**
	 * Constructor
	 * 
	 * @param  player   The player associated with the message, <code>null</code> if none
	 * @param  colour   The colour of the player, <code>null</code> iff <tt>player</tt> is <code>null</code>
	 * @param  message  The message
	 */
	public SystemMessage(final String player, final Color colour, final String message)
	{
	    this.player = player;
	    this.colour = colour;
	    this.message = message;
	}
	
	
	
	/**
	 * The player associated with the message, <code>null</code> if none
	 */
	public final String player;
	
	/**
	 * The colour of the player, <code>null</code> iff <tt>player</tt> is <code>null</code>
	 */
	public final Color colour;
	
	/**
	 * The message
	 */
	public final String message;
	
    }
    
    
    /**
     * Chat message sent by the local client's player
     * 
     * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
     */
    public static final class UserMessage implements BlackboardMessage
    {
	/**
	 * Constructor
	 * 
	 * @param  message  The message
	 */
	public UserMessage(final String message)
	{
	    this.message = message;
	}
	
	
	
	/**
	 * The message
	 */
	public final String message;
	
    }
    
    
    /**
     * <p>Command sent when the player whats to make a move.</p>
     * <p>
     *   Do not broadcast this command if it is not your turn.
     * </p>
     * 
     * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
     */
    public static final class GamePlayCommand implements BlackboardMessage
    {
	/**
	 * Constructor
	 * 
	 * @param  The move  The move
	 */
	public GamePlayCommand(final Move move)
	{
	    this.move = move;
	}
	
	
	
	/**
	 * The move
	 */
	public final Move move;
	
	
	
	/**
	 * The possible moves
	 */
	public static enum Move
	{
	    /**
	     * Move the block one step left
	     */
	    LEFT,
	    
	    /**
	     * Move the block one step right
	     */
	    RIGHT,
	    
	    /**
	     * Move the block one step down
	     */
	    DOWN,
	    
	    /**
	     * Drop the block all the way down
	     */
	    DROP,
	    
	    /**
	     * Rotate the block 90° clockwise
	     */
	    CLOCKWISE,
	    
	    /**
	     * Rotate the block 90° anti-clockwise
	     */
	    ANTICLOCKWISE,
	    
	}
	
    }
    
    /**
     * This message is broadcasted when a player has dropped out
     * 
     * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
     */
    public static final class PlayerDropped implements BlackboardMessage
    {
	/**
	 * Constructor
	 * 
	 * @param  player  The player
	 */
	public PlayerDropped(final Player player)
	{
	    this.player = player;
	}
	
	
	
	/**
	 * The player
	 */
	public final Player player;
	
    }
    
    /**
     * This message is broadcasted when the next player is request, and as a response
     * 
     * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
     */
    public static final class NextPlayer implements BlackboardMessage
    {
	/**
	 * Constructor
	 * 
	 * @param  player  The player, <code>null</code> if requested
	 */
	public NextPlayer(final Player player)
	{
	    this.player = player;
	}
	
	
	
	/**
	 * The player, <code>null</code> if requested
	 */
	public final Player player;
	
    }
    
    /**
     * Game over!
     * 
     * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
     */
    public static final class GameOver implements BlackboardMessage
    {
	//Has default constructor
    }
    
}

