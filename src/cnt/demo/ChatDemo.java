/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.demo;
import cnt.interaction.desktop.*;
import cnt.game.*;
import cnt.network.*;
import cnt.messages.*;
import cnt.*;

import java.awt.*;
import java.util.*;
import java.io.*;
import java.net.*;


/**
 * Network chat demo class
 *
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 * @author  Calle Lejdbrandt, <a href="mailto:callel@kth.se">callel@kth.se</a>
 */
public class ChatDemo
{
    /**
     * Non-constructor
     */
    private ChatDemo()
    {
	assert false : "You may not create instances of this class [ChatDemo].";
    }
    
    
    
    /**
     * This is the main entry point of the demo
     * 
     * @param  args  Startup argument, {name, may be server ? s : c, local public IP address, servers' port, remote peer's IP address}
     * 
     * @throws  Exception  On total failure
     */
    public static void main(final String... args) throws Exception
    {
	(new MainFrame()).setVisible(true);

	final char name = args[0].charAt(0);
	final boolean serverauth = args[1].charAt(0) == 's';
	final String pubip = args[2];
	final int serverport = Integer.parseInt(args[3]);
	final String remote = args[4];
	
	final BlackboardNetworking blackboardNetworking = new BlackboardNetworking();
	Blackboard.broadcastMessage(new SystemMessage(null, "BlackboardNetworking and all other *Networking instances created from chain."));
	
	
	Blackboard.broadcastMessage(new LocalPlayer(new Player(args[0], args[0].hashCode() | (255 << 24))));
	
	Blackboard.registerObserver(new Listener());
	
<<<<<<< Updated upstream
	synchronized(ChatDemo.class)
=======
	Blackboard.broadcastMessage(new SystemMessage(null, "Done setting up. Trying to connect to [" + remote + "]"));
	
	if (remote != null)
>>>>>>> Stashed changes
	{
		ChatDemo.class.wait();
	}
    }
}
