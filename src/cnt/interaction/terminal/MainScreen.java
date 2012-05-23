/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.interaction.terminal;


/**
 * This is the main screen of the program
 *
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class MainScreen extends Thread
{
    //┌┬─┐╔╦═╗▀▄
    //├┼─┤╠╬═╣
    //││ │║║ ║░▒▓
    //└┴─┘╚╩═╝
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void run()
    {
	try
	{
	    final String _screenWidth = getProperty(Property.COLS);
	    final String _screenHeight = getProperty(Property.ROWS);
	    
	    final int screenWidth = _screenWidth == null ? 80 : Integer.parseInt(_screenWidth);
	    final int screenHeight = _screenHeight == null ? 24 : Integer.parseInt(_screenHeight);
	    
	    initalise();
	    
	    //   Menu bar up here
	    //  ┌─────┬─────┬─────┐
	    //  │ 0   │ 2   │ 3   │   (numbers are in tab index order)
	    //  │     │     │     │
	    //  │     │     │     │
	    //  │     │     │     │
	    //  ├─────┴─────┤     │
	    //  │ 1         │     │
	    //  └───────────┴─────┘
	    //   Status bar down here  
	    //
	    //  0: Game area (The Tetris game session)
	    //  1: Chat area (Shat here)
	    //  2: User area (List of playing users)
	    //  3: Misc area (Contains another opened screen)
	    //
	    //  Global keys:
	    // 
	    //  ^+       scale up game area
	    //  ^-       scale down game area
	    //  ^L       redraw everything, and update terminal size
	    //  tab      next frame
	    //  backtab  previous frame
	    //  ^Q       rotate falling block anti-clockwise
	    //  ^W       rotate falling block anti-clockwise
	    //  ^E       rotate falling block clockwise
	    //  ^A       move falling block one step left
	    //  ^S       move falling block one step down
	    //  ^D       move falling block one step right
	    //  ^T       drop falling shape to bottom
	    //  ^P       pause/unpause
	    //  pause    pause/unpause
	    //  sysrq    exit program (twise for uncleanly)
	    //  ^C       exit program (three times, five times for uncleanly)
	    //  ^\ = ^4  system information dump from JVM
	    //  ^M       hide/show menu bar and status bar
	    //  page up    scroll message board one page up
	    //  page down  scroll message board one page down
	    //  
	    //  Game area keys:
	    //  
	    //  s        rotate falling block anti-clockwise
	    //  d        rotate falling block clockwise
	    //  spc      drop falling shape to bottom
	    //  up       rotate falling block anti-clockwise
	    //  down     move falling block one step down
	    //  left     move falling block one step left
	    //  right    move falling block one step right
	    //  S-up     rotate falling block clockwise
	    //  
	    //  Chat area keys:
	    //  
	    //  up       one line up
	    //  down     one line down
	    //  
	    //  User area keys:
	    //  
	    //  +        add as friend
	    //  up       one player up
	    //  down     one player down
	}
	finally
	{
	    terminate();
	}
    }
    
    
    
    /**
     * Status quo ante TTY settings
     */
    private final String stty;
    
    
    
    /**
     * Initialises a terminal
     */
    private static void initalise()
    {
	System.out.print("\033[?1049h");
	System.out.print("\033%G");
	System.out.flush();
	stty = Properties.getProperty(Property.STTY);
	execSystemProperty(LineRule.BREAK, "stty -icanon -echo -isig -ixon -ixoff".split(" "));
    }
    
    
    /**
     * Terminates the terminal
     */
    private static void terminate()
    {
	if (stty != null)
	    Properties.execSystemProperty(LineRule.BREAK, ("stty " + stty).split(" "));
	System.out.print("\033[?1049l");
	System.out.flush();
    }
    
    
    /**
     * Rules on how to parse line breaks
     */
    public static enum LineRule
    {
        /**
         * Ignore all line breaks
         */
        IGNORE,
	
	/**
	 * Stop parsing at first line break
	 */
        BREAK,
        
	/**
	 * Parse line breaks as any other character
	 */
        READ,
	
	/**
	 * Convert line breaks to blank spaces
	 */
	SPACE,
    }
    
    
    /**
     * Properties fetchable by {@link Properties#getProperty(Property)}
     */
    public static enum Property
    {
	/**
	 * The number of columns in the terminal
	 */
	COLS,
        
	/**
	 * The number of rows in the terminal
	 */
	ROWS,
        
	/**
	 * The current boolean settings for the TTY
	 */
	STTY,
	
	/**
	 * The current time
	 */
	TIME,
    }
    
    
    /**
     * Gets a property
     * 
     * @param   property  The property
     * @return            The property value
     */
    public static String getProperty(final Property property)
    {
        switch (property)
	{
            case COLS:
	    {
		String[] data = (" " + execSystemProperty(LineRule.IGNORE, "stty", "-a")).split(";");
		for (final String p : data)
		    if (p.startsWith(" columns "))
			return p.substring(" columns ".length());
		return null;
	    }
            case ROWS:
	    {
		String[] data = execSystemProperty(LineRule.IGNORE, "stty", "-a").split(";");
		for (final String p : data)
		    if (p.startsWith(" rows "))
			return p.substring(" rows ".length());
		return null;
	    }
            case STTY:
	    {
		String[] data = execSystemProperty(LineRule.SPACE, "stty", "-a").split(";");
		String rc = data[data.length - 1];
		while (rc.startsWith(" "))  rc = rc.substring(1);
		while (rc  .endsWith(" "))  rc = rc.substring(0, rc.length() - 1);
		while (rc.contains("  "))   rc = rc.replace("  ", " ");
		return rc;
	    }
	    case TIME:
		return execSystemProperty(LineRule.BREAK, "date");
            
            default:
                assert false : "No such property!";
                return null;
	}
    }
    
    
    /**
     * Gets or sets system properties by invoking another program
     * 
     * @param  lineRule  What to do with line breaks
     * @param  cmd       The command to run
     */
    public static String execSystemProperty(final LineRule lineRule, final String... cmd)
    {
        try
	{
	    byte[] buf = new byte[64];
	    int ptr = 0;
            
	    final ProcessBuilder procBuilder = new ProcessBuilder(cmd);
	    procBuilder.redirectInput(ProcessBuilder.Redirect.from(new File(stdout)));
	    final Process process = procBuilder.start();
	    final InputStream stream = process.getInputStream();
            
	    for (int d; (d = stream.read()) != -1; )
	    {
		if (d == '\n')
		    if      (lineRule == LineRule.BREAK)   break;
		    else if (lineRule == LineRule.IGNORE)  continue;
		    else if (lineRule == LineRule.SPACE)   d = ' ';
                
		if (ptr == buf.length)
		{
		    final byte[] nbuf = new byte[ptr + 64];
		    System.arraycopy(buf, 0, nbuf, 0, ptr);
		    buf = nbuf;
		}
		buf[ptr++] = (byte)d;
	    }
            
	    process.waitFor();
	    if (process.exitValue() != 0)
	    {
		System.err.println("cnt: error: " + cmd[0] + " exited with error code " + process.exitValue());
		return null;
	    }
            
	    return new String(buf, 0, ptr, "UTF-8");
	}
        catch (final Throwable err)
	{
	    System.err.println("cnt: error: failed to execute a system program: " + err.toString());
	    return null;
	}
    }
    
}
