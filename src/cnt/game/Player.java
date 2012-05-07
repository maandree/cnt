/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.game;

import java.awt.Color;
import java.io.*;


/**
 * Player class
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 * @author  Calle Lejdbrandt, <a href="mailto:callel@kth.se">callel@kth.se</a>
 */
public class Player implements Serializable
{
    /**
     * Constructor
     * 
     * @param  name    The name of the player
     * @param  color  The color and ID of the player
     */
    public Player(final String name, final int color)
    {
	this.name = name;
	this.color = color;
    }
    
    /**
     * The name of the player
     */
    protected String name;
    
    /**
     * The color and ID of the player
     */
    protected int color;
    
    
    
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
	
	return this.name.equals(p.name) && (this.color == p.color);
    }
    
    
    /**
     * {@inheritDoc}
     */
    public int hashCode()
    {
	return this.name.hashCode() ^ this.color;
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
     * Gets the color and ID of the player
     * 
     * @return  The color and ID of the player
     */
    public int getColor()
    {
	return this.color;
    }
    
    
    /**
     * Sets the color and ID of the player
     * 
     * @param  value  The new color and ID of the player
     */
    public void setColor(final int value)
    {
	this.color = value;
    }
    
}

