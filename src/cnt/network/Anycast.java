/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.network;

import java.io.*;


/**
 * Message sent to any one player
 */
public class Anycast extends NetworkMessage
{
    /**
     * Compatibility versioning for {@link Serializable}
     */
    private static final long serialVersionUID = 1L;
    
    
    
    /**
     * Constructor
     * 
     * @param  from     The ID of the player whom sent the message
     * @param  message  The message
     */
    public Anycast(final int from, final Serializable message)
    {
	super(from, message);
    }
    
}
