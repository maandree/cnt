/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.network;
import cnt.Blackboard;
import cnt.messages.*;

import java.util.*;
import java.io.*;
import java.net.*;


/**
* The TCP handler for sending outgoing serialized objects
* 
* @author  Calle Lejdbrandt, <a href="mailto:callel@kth.se">callel@kth.se</a>
*/
public class TCPSender implements Runnable
{
    /**
     * Constructor
     *
     * @param  out      The socket output stream to which to send
     * @param  message  The serialized object to send
     */
    public TCPSender(ObjectOutputStream out, Serializable message) throws IOException
    {
	this.out = out;
	this.message = message;
    }
    
    
    
    /**
     * ObjectOutputStream to use for sending
     */
    private final ObjectOutputStream out;
    
    /**
     * Message to be sent
     */
    private final Serializable message;
    
    
    
    /**
     * {@inheritDoc}
     */
    public void run()
    {
	try
	{
	    Blackboard.broadcastMessage(new SystemMessage(null, "Starting output stream"));
	    ObjectOutputStream out = this.out;
	     
	    Blackboard.broadcastMessage(new SystemMessage(null, "Starting wirte"));
	     
	    out.writeObject(message);
	    out.flush();
	    Blackboard.broadcastMessage(new SystemMessage(null, "Finnished sending"));
	}
	catch (Exception err)
	{
	    Blackboard.broadcastMessage(new SystemMessage(null, "Error Sending"));
	}
    }
    
}	
