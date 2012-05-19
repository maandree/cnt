/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt;
import cnt.interaction.desktop.*;

import java.io.*;


/**
 * Game session replayer
 *
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class Replayer //TODO: this class has only a rudamentary implementation
{
    /**
     * Non-constructor
     */
    public Replayer()
    {
	assert false : "You may not create instances of this class [Replayer].";
    }
    
    
    
    /**
     * This is the main entry point if this program
     * 
     * @param  args  Startup arguments, one argument which the file of the recording
     *
     * @throws  Throwable  On any exception
     */
    public static void main(final String... args) throws Throwable
    {
	final ObjectInputStream tape = new ObjectInputStream(
					 new BufferedInputStream(
					   new FileInputStream(
					     new File(args[0])
				       ) ) );
	
	(new MainFrame()).setVisible(true);
	
	while (tape.available() > 0)
	{
	    Thread.sleep(tape.readInt());
	    Blackboard.broadcastMessage((Blackboard.BlackboardMessage)(tape.readObject()));
	}
    }
    
}

