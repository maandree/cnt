/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt;
import cnt.local.*;


/**
 * This is the main class of the program
 *
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class Program
{
    /**
     * Non-constructor
     */
    private Program()
    {
	assert false : "You may not create instances of this class [Program].";
    }
    
    
    
    /**
     * This is the main entry point of the program
     * 
     * @param  args  Startup arguments, unused
     */
    public static void main(final String... args)
    {
	for (final String arg : args)
	    if (arg.startsWith("h="))
	    {
		Friends.home = arg.substring(2);
		break;
	    }
	Friends.load();
	
	cnt.interaction.Launcher.launch(args);
	cnt.game.Launcher.launch(args);
	cnt.network.Launcher.launch(args);
    }
    
}

