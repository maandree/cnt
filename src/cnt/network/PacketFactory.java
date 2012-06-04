/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.network;

import java.io.Serializable;


/**
 * Network packet factory
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class PacketFactory
{
    /**
     * Constructor
     * 
     * @param  id  The local client's ID
     */
    public PacketFactory(final int id)
    {
	this.id = id;
    }
    
    
    
    /**
     * The local client's ID
     */
    public final int id;
    
    
    
    /**
     * Creates a broadcast message
     * 
     * @param  message  The message to send
     * @param  urgent   Whether the message is urgent
     * @param  sentTo   The ID:s of everyone how have got the packet and is currently getting the packet
     */
    public Packet createBroadcast(final Serializable message, final boolean urgent, final int... sentTo)
    {
	return new Packet(new Broadcast(this.id, message), urgent, sentTo);
    }
    
    /**
     * Creates an anycast message
     * 
     * @param  message  The message to send
     * @param  urgent   Whether the message is urgent
     * @param  sentTo   The ID:s of everyone how have got the packet and is currently getting the packet
     */
    public Packet createAnycast(final Serializable message, final boolean urgent, final int... sentTo)
    {
	return new Packet(new Anycast(this.id, message), urgent, sentTo);
    }
    
    /**
     * Creates a unicast message
     * 
     * @param  to       The ID of the receiver
     * @param  message  The message to send
     * @param  urgent   Whether the message is urgent
     * @param  sentTo   The ID:s of everyone how have got the packet and is currently getting the packet
     */
    public Packet createUnicast(final int to, final Serializable message, final boolean urgent, final int... sentTo)
    {
	return new Packet(new Whisper(this.id, to, message), urgent, sentTo);
    }
    
}
