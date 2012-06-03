/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.game;
import cnt.game.enginehelp.*;

import java.util.Arrays;


/**
 * Classical (traditional, original) reaction engine
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class ClassicalReactor implements Reactor
{
    /**
     * Constructor
     * 
     * @param  data  All data held by the engine
     */
    public ClassicalReactor(final EngineData data)
    {
	this.data = data;
    }
    
    
    
    /**
     * All data held by the engine
     */
    private final EngineData data;
    
    
    
    /**
     * {@inheritDoc}
     */
    public void reaction()
    {
	for (;;)
	{
	    final int[] full = this.data.board.getFullRows();
	    if (full.length == 0)
		break;
	    Arrays.sort(full);
	    
	    final boolean[][] fullLine = new boolean[1][Board.WIDTH];
	    for (int x = 0; x < Board.WIDTH; x++)
		fullLine[0][x] = true;
	    
	    final Block[][] matrix = this.data.board.getMatrix();
	    
	    int sub = 0;
	    int row = full[full.length - 1];
	    
	    for (int y = 0; y <= row; y++)
	    {
		this.data.patcher.patchAway(fullLine, 0, y);
		this.data.board.delete(fullLine, 0, y);
	    }
	    for (int y = sub; y < row; y++)
	    {
		this.data.patcher.patchIn(new Block[][] {matrix[y]}, 0, y + 1);
		this.data.board.put(new Block[][] {matrix[y]}, 0, y + 1);
	    }
	    this.data.patcher.dispatch();
	    
	    this.data.score += 10;
	}
    }

}

