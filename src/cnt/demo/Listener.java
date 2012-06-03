/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.demo;
import cnt.messages.*;
import cnt.*;


/**
 * Helper class for {@link ChatDemo}
 */
class Listener implements Blackboard.BlackboardObserver
{
    public void messageBroadcasted(final Blackboard.BlackboardMessage message)
    {
	try
	{
	    if (message instanceof UserMessage)
		System.out.println(message);
	    else if (message instanceof ChatMessage)
		System.out.println(message);
	}
	catch (final Exception err)
	{
	    //Do nothing
	}
    }
}
