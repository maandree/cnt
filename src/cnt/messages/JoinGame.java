/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.messages;
import cnt.*;


/**
 * Join game message
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public final class JoinGame implements Blackboard.BlackboardMessage
{
    /**
     * Compatibility versioning for {@link java.io.Serializable}
     */
    private static final long serialVersionUID = 1L;
    
    
    
    /**
     * Constructor
     * 
     * @param  name    The player name to use
     * @param  remote  The address to which to connect, <code>null</code> or empty if none
     * @param  port    The port to which to connect; or if <tt>remote</tt> is <code>null</code>, the port to use
     */
    public JoinGame(final String name, final String remote, final int port)
    {
	assert (name != null) && (name.isEmpty() == false) : "You must have a name";
	this.name = name;
	this.remote = ((remote == null) || remote.isEmpty()) ? null : remote;
	this.port = port;
    }
    
    
    
    /**
     * The player name to use
     */
    public final String name;
    
    /**
     * The address to which to connect, <code>null</code> if none
     */
    public final String remote;
    
    /**
     * The port to which to connect; or if {@link #remote} is <code>null</code>, the port to use
     */
    public final int port;
    
    
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
	return this.remote == null ? ("New game cloud on " + (port == 0 ? "random port" : ("port " + port)))
	                           : ("Joining game cloud " + this.remote + " on port " + port);
    }
    
    /**
     * {@inheritDoc}
     */
    public Boolean checkIntegrity()
    {
	return Boolean.FALSE;
    }
    
}
