/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright Ⓒ 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.game;

import java.awt.Color;
import java.io.*;


/**
 * Player class
 * 
 * @author  Mattias Andrée, <a href="maandree@kth.se">maandree@kth.se</a>
 */
public class Player implements Serializable
{
    /**
     * Constructor
     * 
     * @param  name    The name of the player
     * @param  colour  The colour and ID of the player
     */
    public Player(final String name, final Color colour)
    {
	this.name = name;
	this.colour = colour;
    }
    
    
    
    /**
     * The name of the player
     */
    protected String name;
    
    /**
     * The colour and ID of the player
     */
    protected Color colour;
    
    
    
    /**
     * {@inheritDoc}
     */
    public boolean equals(final Object object)
    {
	if (object == this)  return true;
	if (object == null)  return false;
	if (object instanceof Player == false)
	    return false;
	
	final Player p = (Player)object;
	
	return this.name.equals(p.name) && this.colour.equals(p.colour);
    }
    
    
    /**
     * {@inheritDoc}
     */
    public int hashCode()
    {
	return this.name.hashCode() ^ this.colour.hashCode();
    }
    
    
    /**
     * Gets the name of the player
     * 
     * @return  The name of the player
     */
    public String getName()
    {
	return this.name;
    }
    
    
    /**
     * Gets the colour and ID of the player
     * 
     * @return  The colour and ID of the player
     */
    public Color getColour()
    {
	return this.colour;
    }
    
    
    /**
     * Sets the colour and ID of the player
     * 
     * @param  value  The new colour and ID of the player
     */
    public void setColour(final Color value)
    {
	this.colour = value;
    }
    
}

