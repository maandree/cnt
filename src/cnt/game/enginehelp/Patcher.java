/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.game.enginehelp;
import cnt.game.*;
import cnt.messages.*;
import cnt.*;

import java.util.*;


/**
 * Game engine helper class: matrix patcher
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class Patcher
{
    //Has default constructor
    
    
    /**
     * All queued matrix patches for {@link Blackboard}
     */
    private final ArrayList<MatrixPatch> patches = new ArrayList<MatrixPatch>();
    
    
    
    /**
     * Broadcasts a matrix patch that removes a shape
     * 
     * @param  shape  The shape to remove
     */
    public void patchAway(final Shape shape)
    {
	final int offX = shape.getX();
	final int offY = shape.getY();
	final boolean[][] blocks = shape.getBooleanMatrix();
	patchAway(blocks, offX, offY);
    }
    
    
    /**
     * Broadcasts a matrix patch that removes a set of blocks
     * 
     * @param  blocks  The block pattern
     * @param  offX    Offset on the x-axis
     * @param  offY    Offset on the y-axis
     */
    public void patchAway(final boolean[][] blocks, final int offX, final int offY)
    {
	final MatrixPatch patch = new MatrixPatch(blocks, null, offY, offX);
	patches.add(patch);
    }
    
    
    /**
     * Broadcasts a matrix patch that adds a shape
     * 
     * @param  shape  The shape to add
     */
    public void patchIn(final Shape shape)
    {
	final int offX = shape.getX();
	final int offY = shape.getY();
	final Block[][] blocks = shape.getBlockMatrix();
	patchIn(blocks, offX, offY);
    }
    
    
    /**
     * Broadcasts a matrix patch that adds a set of blocks
     * 
     * @param  blocks  The block pattern
     * @param  offX    Offset on the x-axis
     * @param  offY    Offset on the y-axis
     */
    public void patchIn(final Block[][] blocks, final int offX, final int offY)
    {
	final MatrixPatch patch = new MatrixPatch(null, blocks, offY, offX);
	patches.add(patch);
    }
    
    
    /**
     * Broadcasts all updates
     */
    public void dispatch()
    {
	if (patches.isEmpty() == false)
	{
	    int x1 = 0, y1 = 0, x2 = 0, y2 = 0, x3 = 0, y3 = 0;
	    boolean del = false, add = false;
	    
	    for (final MatrixPatch patch : patches)
	    {
		if (x1 > patch.offX)  x1 = patch.offX;
		if (y1 > patch.offY)  y1 = patch.offY;
		
		if (patch.erase != null)
		    if (del)
		    {
			if (x2 < patch.offX + patch.erase[0].length)  x2 = patch.offX + patch.erase[0].length;
			if (y2 < patch.offY + patch.erase.length)     y2 = patch.offY + patch.erase.length;
		    }
		    else
		    {
			del = true;
			x2 = patch.offX + patch.erase[0].length;
			y2 = patch.offY + patch.erase.length;
		    }
		
	        if (patch.blocks != null)
		    if (add)
		    {
			if (x3 < patch.offX + patch.blocks[0].length)  x3 = patch.offX + patch.blocks[0].length;
			if (y3 < patch.offY + patch.blocks.length)     y3 = patch.offY + patch.blocks.length;
		    }
		    else
		    {
			add = true;
			x3 = patch.offX + patch.blocks[0].length;
			y3 = patch.offY + patch.blocks.length;
		    }
	    }
	    
	    if (del || add)
	    {
		final boolean[][] erase  = del ? new boolean[y2 - y1][x2 - x1] : null;
		final Block  [][] blocks = add ? new Block  [y3 - y1][x3 - x1] : null;
		
		for (final MatrixPatch patch : patches)
		{
		    if (patch.erase != null)
			for (int y = 0; y < patch.erase.length; y++)
			    for (int x = 0; x < patch.erase[y].length; x++)
				erase[y + patch.offY - y1][x + patch.offX - x1] = patch.erase[y][x];
		    
		    if (patch.blocks != null)
			for (int y = 0; y < patch.blocks.length; y++)
			    for (int x = 0; x < patch.blocks[y].length; x++)
				blocks[y + patch.offY - y1][x + patch.offX - x1] = patch.blocks[y][x];
		}
		
		patches.clear();
		Blackboard.broadcastMessage(new MatrixPatch(erase, blocks, y1, x1));
	    }
	    else
		System.err.println("Shouldn't the matrix patches actually contain something?");
	}
    }
    
}

