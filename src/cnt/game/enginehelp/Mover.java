/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.game.enginehelp;
import cnt.game.*;



/**
 * Game engine helper class: shape mover and rotater
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class Mover
{
    /**
     * Constructor
     * 
     * @param  data  The game engine
     */
    public Mover(final Engine engine)
    {
	this.data = (this.engine = engine).data;
    }
    
    
    
    /**
     * The game engine
     */
    private final Engine engine;
    
    /**
     * All data held by the engine
     */
    private final EngineData data;
    
    
    
    /**
     * Makes the falling block drop on step and apply the, if any, registrered modification
     * 
     * @return  Whether the fall was not interrupted
     * 
     * @throws  InterruptedException  Can only indicate the the player is leaving
     */
    public boolean fall() throws InterruptedException
    {
	this.data.patcher.dispatch();
	
	if (this.data.fallingShape == null)
	{
	    System.err.println("What's happening, why do we not have a falling shape?");
	    return true;
	}
	
	this.data.patcher.patchAway(this.data.fallingShape);
	this.data.fallingShape.restore(this.data.moveInitialMomento = this.data.moveAppliedMomento);
	this.data.fallingShape.setY(this.data.fallingShape.getY() + 1);
	
	if (this.data.board.canPut(this.data.fallingShape, false) == false)
	{
	    this.data.fallingShape.restore(this.data.moveInitialMomento);
	    this.engine.reaction();
	    this.data.fallingShape = null;
	    return false;
	}
	
	this.data.moveInitialMomento = this.data.moveAppliedMomento = this.data.fallingShape.store();
	
	this.data.patcher.patchIn(this.data.fallingShape);
	return true;
    }
    
    
    /**
     * Drops the falling block to the bottom
     * 
     * @throws  InterruptedException  Can only indicate the the player is leaving
     */
    public void drop() throws InterruptedException
    {
	if (this.data.fallingShape == null)
	{
	    System.err.println("What's happening, why do we not have a falling shape?");
	    return;
	}
	
	this.data.patcher.dispatch();
	this.data.patcher.patchAway(this.data.fallingShape);
	this.data.fallingShape.restore(this.data.moveInitialMomento = this.data.moveAppliedMomento);
	
	for (;;)
	{
	    this.data.fallingShape.setY(this.data.fallingShape.getY() + 1);
	    
	    if (this.data.board.canPut(this.data.fallingShape, false) == false)
	    {
		this.data.fallingShape.setY(this.data.fallingShape.getY() - 1);
		this.engine.reaction();
		this.data.fallingShape = null;
		return;
	    }
	}
    }
    
    
    /**
     * Applies any requested move, but does not make the shape fall
     * 
     * @throws  InterruptedException  Can only indicate the the player is leaving
     */
    public void move() throws InterruptedException
    {
	this.data.patcher.dispatch();
	
	if (this.data.fallingShape == null)
	{
	    System.err.println("What's happening, why do we not have a falling shape?");
	    return;
	}
	
	this.data.patcher.patchAway(this.data.fallingShape);
	this.data.fallingShape.restore(this.data.moveInitialMomento = this.data.moveAppliedMomento);
	this.data.patcher.patchIn(this.data.fallingShape);
    }
    
    
    /**
     * Registrers a rotation, if possible, to the falling block
     * 
     * @param  clockwise  Whether to rotate clockwise
     */
    public void rotate(final boolean clockwise)
    {
	if (this.data.fallingShape == null)
	{
	    System.err.println("What's happening, why do we not have a falling shape?");
	    return;
	}
	
	this.data.fallingShape.rotate(clockwise);
	
	if (this.data.board.canPut(this.data.fallingShape, false))
	    this.data.moveAppliedMomento = this.data.fallingShape.store();
	else
	    this.data.moveAppliedMomento = this.data.moveInitialMomento;
	
	this.data.fallingShape.restore(this.data.moveInitialMomento);
    }
    
    
    /**
     * Registrers a horizontal movement, if possible, to the falling block
     * 
     * @param  incrX  The value with which to increase the left position
     */
    public void move(final int incrX)
    {
	if (this.data.fallingShape == null)
	{
	    System.err.println("What's happening, why do we not have a falling shape?");
	    return;
	}
	
	this.data.fallingShape.setX(this.data.fallingShape.getX() + incrX);
	
	if (this.data.board.canPut(this.data.fallingShape, false))
	    this.data.moveAppliedMomento = this.data.fallingShape.store();
	else
	    this.data.moveAppliedMomento = this.data.moveInitialMomento;
	
	this.data.fallingShape.restore(this.data.moveInitialMomento);
    }
    
}

