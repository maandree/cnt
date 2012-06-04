/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.network;

import java.util.*;


/**
 * Packet with a message, urgent flag and infrastructure data
 */
public class Packet implements Comparable<Packet>
{
    /**
     * Constructor
     * 
     * @param  message  The message with sender and possibly receiver or receivers
     * @param  urgent   Whether the message is urgent
     */
    public Packet(final NetworkMessage message, final boolean urgent)
    {
	this.message = message;
	this.urget = urgent;
	this.uuid = UUID.randomUUID();
	this.sentTo = new int[] { message.getSender() };
    }
    
    /**
     * Constructor
     * 
     * @param  message  The message with sender and possibly receiver or receivers
     * @param  urgent   Whether the message is urgent
     * @param  sentTo   The ID:s of everyone how have got the packet and is currently getting the packet, the local user should be included
     */
    public Packet(final NetworkMessage message, final boolean urgent, final int... sentTo)
    {
	this.message = message;
	this.urget = urgent;
	this.uuid = UUID.randomUUID();
	Arrays.sort(this.sentTo = sentTo);
    }
    
    
    
    /**
     * The message with sender and possibly receiver or receivers
     */
    private final NetworkMessage message;
    
    /**
     * Whether the message is urgent
     */
    private final boolean urgent;
    
    /**
     * The UUID for this message
     */
    private final UUID uuid;
    
    /**
     * The ID:s of everyone how have got the packet and is currently getting the packet
     */
    private int[] sentTo;
    
    
    
    /**
     * {@inheritDoc}
     */
    public int compareTo(final Packet other)
    {
	this.uuid.compareTo(other.uuid);
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
	return this.uuid.hashCode();
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object other)
    {
	if ((other == null) || (other instanceof Packet == false))
	    return false;
	
	if (other == this)
	    return true;
	
	return this.uuid.equals(((Packet)other).uuid);
    }
    
    
    /**
     * Gets the message with sender and possibly receiver or receivers
     * 
     * @return  The message with sender and possibly receiver or receivers
     */
    public NetworkMessage getMessage()
    {
	return this.message;
    }
    
    
    /**
     * Gets whether the message is urgent
     * 
     * @return  Whether the message is urgent
     */
    public boolean isUrgent()
    {
	return this.urgent;
    }
    
    
    /**
     * Returns the UUID for this message
     * 
     * @return  The UUID for this message
     */
    public UUID getUUID()
    {
	return this.uuid;
    }
    
    
    /**
     * Gets the ID:s of everyone how have got the packet and is currently getting the packet
     * 
     * @return The ID:s of everyone how have got the packet and is currently getting the packet
     */
    public synchronized int[] getSentTo()
    {
	return this.sentTo;
    }
    
    
    /**
     * Checks whether the packet has already been sent to a player
     * 
     * @param   id  The ID of that player
     * @return      Whether the packet has already been sent to a player
     */
    public synchronized boolean checkHasGotPacket(final int id)
    {
	return Arrays.binarySearch(this.sentTo, id) >= 0;
    }
    
    
    /**
     * Adds a player to the list of players how have already got or is
     * currently receiving the packet
     * 
     * @param   id  The ID of that player
     * @return      Whether the packet has already been sent to a player
     */
    public synchronized boolean addHasGotPacket(final int id)
    {
	int pos = Arrays.binarySearch(this.sentTo, id);
	if (pos >= 0)
	    return false;
	
	pos = ~pos;
	
	final int[] ids = new int[this.sendTo.length + 1];
	System.arraycopy(ids, 0, this.sendTo, 0, pos);
	System.arraycopy(ids, pos + 1, this.sendTo, pos + 1, this.sendTo.length - pos - 1);
	(this.sendTo = ids)[pos] = id;
	
	return true;
    }
    
}
