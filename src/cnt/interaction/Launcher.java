/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.interaction;


/**
 * Launcher for this package
 *
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class Launcher
{
    /**
     * Non-constructor
     */
    private Launcher()
    {
	assert false : "You may not create instances of this class [cnt.interaction.Launcher].";
    }
    
    
    
    /**
     * Main launcher method for this package
     * 
     * @param  args  Startup arguments, unused
     */
    public static void launch(final String... args)
    {
	cnt.interaction.desktop.Launcher.launch(args);
    }
    
}

