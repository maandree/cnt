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
     * Compatibility versioning for {@link Serializable}
     */
    private static final long serialVersionUID = 1L;
    
    
    
    /**
     * Constructor
     * 
     * @param  name   The name of the player
     * @param  color  The ID of the player
     */
    public Player(final String name, final int id)
    {
	this.name = name;
	this.id = id;
    }
    
    
    
    /**
     * The name of the player
     */
    protected String name;
    
    /**
     * The ID of the player
     */
    protected int id;
    
    
    
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
	
	return this.name.equals(p.name) && (this.id == p.id);
    }
    
    
    /**
     * {@inheritDoc}
     */
    public int hashCode() {
	return this.name.hashCode() ^ this.id;
    }
    
    
    /**
     * Gets the name of the player
     * 
     * @return  The name of the player
     */
    public String getName() {
	return this.name;
    }
    
    
    /**
     * Gets the ID of the player
     * 
     * @return  The ID of the player
     */
    public int getID() {
	return this.id;
    }
    
    
    /**
     * Sets the ID of the player
     * 
     * @param  value  The new ID of the player
     */
    public void setID(final int value) {
	this.id = value;
    }
    
    /**
     * {@inheritDoc}
     */
    public String toString() {
	return this.name + " (" + this.id + ")";
    }
    
}

