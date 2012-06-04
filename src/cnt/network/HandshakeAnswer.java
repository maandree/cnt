/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.network;


public class HandshakeAnswer implements ConnectionMessage
{
    /**
     * Compatibility versioning for {@link Serializable}
     */
    private static final long serialVersionUID = 1L;
    
    
    
    /**
     * Constructor
     * 
     * @oaram  client  The ID of the peer whom connected
     * @oaram  server  The ID of the peer to whom was connected
     */
    public HandshakeAnswer(final int client, final int server)
    {
	this.client = client;
	this.server = server;
    }
    
    
    
    /**
     * The ID of the peer whom connected
     */
    public final int client;
    
    /**
     * The ID of the peer to whom was connected
     */
    public final int server;

}
