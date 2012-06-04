/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.network;


/**
 * Message sent to all players
 */
public class Broadcast extends NetworkMessage
{
    /**
     * Constructor
     * 
     * @param  from     The ID of the player whom sent the message
     * @param  message  The message
     */
    public Broadcast(final int from, final Serializable message)
    {
	super(from, message);
    }
    
}
