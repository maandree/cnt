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
     * Non-constructor
     */
    private PacketFactory()
    {
        assert false : "You may not create instances of this class [PacketFactory].";
    }
    
    
    
    /**
     * The local client's ID
     */
    private static int id = 0;
    
    
    
    /**
     * Sets the local client's ID
     * 
     * @param  The local client's ID
     */
    public static void setID(final int id)
    {
	PacketFactory.id = id;
    }
    
    
    /**
     * Creates a reconnection handshake message
     * 
     * @param  to  The ID of the receiver
     */
    public static Packet createConnectionHandshake()
    {
	return createAnycast(new Handshake(), false);
    }
    
    /**
     * Creates a reconnection handshake message
     * 
     * @param  to  The ID of the receiver
     */
    public static Packet createReconnectionHandshake(final int to)
    {
	return createUnicast(to, new Handshake(id), false);
    }
    
    /**
     * Creates a broadcast message
     * 
     * @param  message  The message to send
     * @param  urgent   Whether the message is urgent
     * @param  sentTo   The ID:s of everyone how have got the packet and is currently getting the packet
     */
    public static Packet createBroadcast(final Serializable message, final boolean urgent, final int... sentTo)
    {
	return new Packet(new Broadcast(id, message), urgent, sentTo);
    }
    
    /**
     * Creates an anycast message
     * 
     * @param  message  The message to send
     * @param  urgent   Whether the message is urgent
     * @param  sentTo   The ID:s of everyone how have got the packet and is currently getting the packet
     */
    public static Packet createAnycast(final Serializable message, final boolean urgent, final int... sentTo)
    {
	return new Packet(new Anycast(id, message), urgent, sentTo);
    }
    
    /**
     * Creates a unicast message
     * 
     * @param  to       The ID of the receiver
     * @param  message  The message to send
     * @param  urgent   Whether the message is urgent
     * @param  sentTo   The ID:s of everyone how have got the packet and is currently getting the packet
     */
    public static Packet createUnicast(final int to, final Serializable message, final boolean urgent, final int... sentTo)
    {
	return new Packet(new Whisper(id, to, message), urgent, sentTo);
    }
    
}
