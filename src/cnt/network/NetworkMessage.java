/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.network;


/**
 * Abstract message
 */
public abstract class NetworkMessage
{
    /**
     * Constructor
     * 
     * @param  from     The ID of the player whom sent the message
     * @param  message  The message
     */
    public NetworkMessage(final int from, final Serializable message)
    {
	this.from = from;
	this.message = message;
    }
    
    
    
    /**
     * The ID of the player whom sent the message
     */
    protected int from;
    
    /**
     * The message
     */
    protected Serializable message;
    
    
    
    /**
     * Gets the ID of the player whom sent the message
     * 
     * @return  The ID of the player whom sent the message
     */
    public int getSender()
    {
	return this.from;
    }
    
    
    /**
     * Gets the message
     * 
     * @return  The message
     */
    public Serializable getMessage()
    {
	return this.message;
    }
    
}
