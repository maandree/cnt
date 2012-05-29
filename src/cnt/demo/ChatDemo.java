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
     * @param  args  Startup argument
     * 
     * @throws  Exception  On total failure
     */
    public static void main(final String... args) throws Exception
    {
	(new GameFrame()).setVisible(true);
	
	final String name = args[0];
	final String remote = args.length > 1 ? args[1] : null;
	
	final BlackboardNetworking blackboardNetworking = new BlackboardNetworking();
	Blackboard.broadcastMessage(new SystemMessage(null, "BlackboardNetworking and all other *Networking instances created from chain."));
	
	
	Blackboard.broadcastMessage(new LocalPlayer(new Player(name, name.hashCode() | (255 << 24), null, null)));
	
	Blackboard.registerObserver(new Listener());
	
	Blackboard.broadcastMessage(new SystemMessage(null, "Done setting up. Trying to connect to [" + remote + "]"));
	
	if (remote != null)
	{
	    blackboardNetworking.gameNetworking.objectNetworking.connectionNetworking.connect(
                         (Inet4Address)(Inet4Address.getByName(remote)),
			 ConnectionNetworking.PORT,
			 args.length > 2 ? Integer.parseInt(args[2]) : 1
		    );
	}
    }
}
