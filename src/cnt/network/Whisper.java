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
 * Message sent to exactly one player
 */
public class Whisper extends NetworkMessage
{
    /**
     * Compatibility versioning for {@link Serializable}
     */
    private static final long serialVersionUID = 1L;
    
    
    
    /**
     * Constructor
     * 
     * @param  from     The ID of the player whom sent the message
     * @param  to       The ID of the playerto whom the message is sent
     * @param  message  The message
     */
    public Whisper(final int from, final int to, final Serializable message)
    {
	super(from, message);
	this.to = to;
    }
    
    
    
    /**
     * The ID of the player to whom the message is sent
     */
    protected int to;
    
    
    
    /**
     * Gets the ID of the player to whom the message is sent
     * 
     * @return  The ID of the player to whom the message is sent
     */
    public int getReceiver()
    {
	return this.to;
    }

}
