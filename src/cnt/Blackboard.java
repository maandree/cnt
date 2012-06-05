/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt;

import java.util.*;
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
    private static /*Weak*/HashMap<BlackboardObserver, Void> observers = new /*Weak*/HashMap<>(); /*//simulate the missing cass: WeakHashSet*/
    
    /**
     * How to thread message observations
     */
    private static /*Weak*/HashMap<BlackboardObserver, HashMap<Class<? extends BlackboardMessage>, ThreadingPolicy>> observationThreading = new /*Weak*/HashMap<>();
    
    /**
     * Persistant messages
     */
    private static HashMap<Class<? extends PersistantMessage>, PersistantMessage> persistants = new HashMap<>();
    
    /**
     * Concurrency monitor
     */
    private static Object monitor = new Object();
    
    
    
    /**
     * This interface is used for all event
     */
    public static interface BlackboardMessage extends Serializable
    {
	/**
	 * <p>
	 *   Checks to integrity of the message, if it fails, you should
	 *   kick be player who sent it. That client is faulty.
	 * </p>
	 * <p>
	 *   This mechanism is only for messages recieved over the network,
	 *   locally created instances are checked in the constructors using
	 *   the asserting mechanism.
	 * </p>
	 * 
	 * @return  {@link Boolean#TRUE} if the message checks out,
	 *          {@link Boolean#FALSE} if the message is corrupt, and
	 *          <code>null</code> if external examination is required.
	 */
	public Boolean checkIntegrity();
    }
    
    
    /**
     * Persisrant message are sent to new observers, until a new
     * instance is created of the same class.
     */
    public static interface PersistantMessage extends BlackboardMessage
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
	synchronized (monitor)
	{
	    System.err.println("BLACKBOARD.registerObserver(" + observer + ")");
	    observers.put(observer, null);
	    
	    final ArrayList<Thread> threads = new ArrayList<Thread>();
	    
	    for (final PersistantMessage message : persistants.values())
	    {
		final ThreadingPolicy policy;
		final Runnable runnable = new Runnable()
		    {
			/**
			 * {@inheritDoc}
			 */
			public void run()
			{   observer.messageBroadcasted(message);
			}
		    };
		
		final HashMap<Class<? extends BlackboardMessage>, ThreadingPolicy> map = observationThreading.get(observer);
		if (map == null)  policy = null;
		else              policy = map.get(message.getClass());
		
		if (policy == null)  runnable.run();
		else                 threads.add(policy.createThread(runnable));
	    }
	    
	    for (final Thread thread : threads)
		thread.start();
	}
    }
    
    
    /**
     * Unregisters a message type-wide observer
     *
     * @param  observer  The observer to unregister
     */
    public static void unregisterObserver(final BlackboardObserver observer)
    {
	synchronized (monitor)
	{
	    System.err.println("BLACKBOARD.unregisterObserver(" + observer + ")");
	    observers.remove(observer);
	    observationThreading.remove(observer);
	}
    }
    
    
    /**
     * Registers a threading policy for an observer and a message type
     * 
     * @param  observer     The observer
     * @param  messageType  The message type
     * @param  policy       The threading policy
     * 
     * @deprecated  Use overloading {@link #registerThreadingPolicy(BlackboardObserver, ThreadingPolicy, Class<? extends BlackboardMessage>...)} instead
     */
    @Deprecated
    public static void registerThreadingPolicy(final BlackboardObserver observer, final Class<? extends BlackboardMessage> messageType, final ThreadingPolicy policy)
    {
	registerThreadingPolicy(observer, policy, messageType);
    }
    
    
    /**
     * Registers a threading policy for an observer and some message types
     * 
     * @param  observer      The observer
     * @param  policy        The threading policy
     * @param  messageTypes  The message types, must be <code>Class<? extends BlackboardMessage></code>
     */
    @SuppressWarnings("unchecked")
    public static void registerThreadingPolicy(final BlackboardObserver observer, final ThreadingPolicy policy, final Class... messageTypes)
    {
	synchronized (monitor)
	{
	    HashMap<Class<? extends BlackboardMessage>, ThreadingPolicy> map = observationThreading.get(observer);
	    if (map == null)
	    {
		map = new HashMap<Class<? extends BlackboardMessage>, ThreadingPolicy>();
		observationThreading.put(observer, map);
	    }
	    for (final Class<? extends BlackboardMessage> messageType : messageTypes)
		map.put(messageType, policy);
	}
    }
    
    
    /**
     * Broadcasts a message to all observers
     * 
     * @param  message  The message to broadcast
     */
    public static void broadcastMessage(final BlackboardMessage message)
    {
	synchronized (monitor)
	{
	    if (message instanceof PersistantMessage)
		persistants.put(((PersistantMessage)message).getClass(), (PersistantMessage)message);
	    
	    System.err.println("BLACKBOARD.broadcastMessage(" + message.toString() + ")");
	    final ArrayList<Thread> threads = new ArrayList<Thread>();
	    
	    final Set<BlackboardObserver> keySet = observers.keySet();
	    final BlackboardObserver[] keys = new BlackboardObserver[keySet.size()];
	    keySet.toArray(keys);
	    
	    for (final BlackboardObserver observer : keys)
	    {
		System.err.println("BLACKBOARD.broadcastMessage() ==> " + observer.toString());
		final ThreadingPolicy policy;
		final Runnable runnable = new Runnable()
		        {
			    /**
			     * {@inheritDoc}
			     */
			    public void run()
			    {   observer.messageBroadcasted(message);
			    }
		    };
		
		final HashMap<Class<? extends BlackboardMessage>, ThreadingPolicy> map = observationThreading.get(observer);
		if (map == null)  policy = null;
		else              policy = map.get(message.getClass());
		
		if (policy == null)  runnable.run();
		else                  threads.add(policy.createThread(runnable));
	    }
	    
	    for (final Thread thread : threads)
		thread.start();
	    
	    System.err.println("BLACKBOARD.broadcastMessage() <<<<");
	}
    }
    
}
