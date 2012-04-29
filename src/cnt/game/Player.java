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
    }
    
    
    
    /**
     * Gets the name of the player
     * 
     * @return  The name of the player
     */
    public String getName()
    {
	return null;
    }
    
    /**
     * Gets the colour and ID of the player
     * 
     * @return  The colour and ID of the player
     */
    public Color getColour()
    {
	return null;
    }
    
    /**
     * Sets the colour and ID of the player
     * 
     * @param  value  The new colour and ID of the player
     */
    public void setColour(final Color value)
    {
    }
    
}

