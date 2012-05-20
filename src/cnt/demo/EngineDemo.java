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
import cnt.messages.*;
import cnt.*;

import java.awt.Color;


/**
 * Engine demo class
 *
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class EngineDemo
{
    /**
     * Non-constructor
     */
    private EngineDemo()
    {
	assert false : "You may not create instances of this class [EngineDemo].";
    }
    
    
    
    /**
     * This is the main entry point of the demo
     * 
     * @param  args  Startup arguments, you may enter 'clockwise' or 'anti-clockwise' constant rotations
     * 
     * @thorws  InterruptedException  On thread interruption
     */
    public static void main(final String... args) throws InterruptedException
    {
	final Color colour = new Color(36, 149, 190);
	final Player player = new Player("The One", colour.getRGB());
	
	(new MainFrame()).setVisible(true);
	
	Blackboard.registerObserver(new Blackboard.BlackboardObserver()
	        {
		    /**
		     * {@inheritDoc}
		     */
		    public void messageBroadcasted(final Blackboard.BlackboardMessage message)
		    {
			if (message instanceof NextPlayer) /*do not thread*/
			{
			    if (((NextPlayer)message).player == null)
			    {
				System.out.println("\033[33mNext player\033[0m");
				Blackboard.broadcastMessage(new NextPlayer(player));
			    }
			    else
				System.out.println("\033[35m(Next player)\033[0m");
			}
			else if (message instanceof GameOver)
			{
			    System.out.println("\033[33mGame over!\033[0m");
			}
			else if (message instanceof MatrixPatch)
			{
			    final MatrixPatch patch = (MatrixPatch)message;
			    
			    final boolean[][] erase = patch.erase;
			    final Block[][] blocks = patch.blocks;
			    final int offY = patch.offY;
			    final int offX = patch.offX;
			    final int[][] matrix = new int[20][10];
			    
			    if (erase != null)
				for (int y = offY < 0 ? -offY : 0, h = erase.length; y < h; y++)
				    {
					final int Y = y + offY;
					if (Y >= matrix.length)
					    break;
					
					for (int x = offX < 0 ? -offX : 0, w = erase[y].length; x < w; x++)
					    if (erase[y][x])
						if (x + offX < matrix[Y].length)
						    matrix[Y][x + offX] |= 1;
						else
						    break;
				    }
			    
			    if (blocks != null)
				for (int y = offY < 0 ? -offY : 0, h = blocks.length; y < h; y++)
				    {
					final int Y = y + offY;
					    if ((Y >= matrix.length) || (0 > Y))
						break;
					    
					    for (int x = offX < 0 ? -offX : 0, w = blocks[y].length; x < w; x++)
						if (blocks[y][x] != null)
						    if (x + offX < matrix[Y].length)
							matrix[Y][x + offX] |= 2;
						    else
							break;
				    }
			    
			    for (int y = 0; y < 10; y++)
			    {
				for (int x = 0; x < 10; x++)
				    System.out.print("\033[4" + matrix[(y << 1) | 0][x] + ";3" + matrix[(y << 1) | 1][x] + "m▄");
				System.out.println("\033[0m");
			    }
			    System.out.println();
			}
		    }
	        });
	
	Engine.start();
	
	
	if ((args.length == 0) || (args[0].isEmpty()))
	{
	    Thread.sleep(500);   System.out.println("LEFT <-- ");  Blackboard.broadcastMessage(new GamePlayCommand(GamePlayCommand.Move.LEFT));
	    Thread.sleep(200);   System.out.println("RIGHT -->");  Blackboard.broadcastMessage(new GamePlayCommand(GamePlayCommand.Move.RIGHT));
	    for (int i = 0; i < 10; i++)
		{ Thread.sleep(1000);  System.out.println("RIGHT -->");  Blackboard.broadcastMessage(new GamePlayCommand(GamePlayCommand.Move.RIGHT)); }
	
	    Thread.sleep(500);   System.out.println("    DROP");  Blackboard.broadcastMessage(new GamePlayCommand(GamePlayCommand.Move.DROP));
	    
	    for (int i = 0; i < 10; i++)
		{ Thread.sleep(500);   System.out.println("LEFT <-- ");  Blackboard.broadcastMessage(new GamePlayCommand(GamePlayCommand.Move.LEFT)); }
	    
	    for (int i = 0; i < 5; i++)
		{ Thread.sleep(300);   System.out.println("  DOWN");  Blackboard.broadcastMessage(new GamePlayCommand(GamePlayCommand.Move.DOWN)); }
	    
	    Thread.sleep(7000);
	    
	    for (int i = 0; i < 4; i++)
		{ Thread.sleep(1000);  System.out.println("CLOCKWISE");  Blackboard.broadcastMessage(new GamePlayCommand(GamePlayCommand.Move.CLOCKWISE)); }
	    
	    for (int i = 0; i < 4; i++)
		{ Thread.sleep(1000);  System.out.println("ANTI-CLOCKWISE");  Blackboard.broadcastMessage(new GamePlayCommand(GamePlayCommand.Move.ANTICLOCKWISE)); }
	}
	else if (args[0].toLowerCase().contains("counter") || args[0].equalsIgnoreCase("cc") || (args[0].charAt(0) == 'a') || (args[0].charAt(0) == 'A'))
	    for (;;)
		{ Thread.sleep(500);  Blackboard.broadcastMessage(new GamePlayCommand(GamePlayCommand.Move.ANTICLOCKWISE)); }
	else
	    for (;;)
		{ Thread.sleep(500);  Blackboard.broadcastMessage(new GamePlayCommand(GamePlayCommand.Move.CLOCKWISE)); }
    }
    
}

