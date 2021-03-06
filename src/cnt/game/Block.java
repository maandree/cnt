/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.game;

import java.io.Serializable;


/**
 * Class representing a single block in the playing field
 * 
 * @author  Calle Lejdbrandt, <a href="mailto:callel@kth.se">callel@kth.se</a>
 */
public class Block implements Serializable
{
    /**
     * Compatibility versioning for {@link Serializable}
     */
    private static final long serialVersionUID = 1L;
    
    
    
    /**
     * Constructor
     *
     * @param  color  The color of the new block
     */
    public Block(final int color)
    {
	this.setColor(color);
    }
    
    /**
     * Constructor, without color
     */
    public Block()
    {
	this(0xFF0000C0);
    }
    
    
    
    /**
     * The color of the block
     */
    private int color;
    
    
    
    /**
     * Setter for int colors
     *
     * @param   color  An integer representing a color, the new color
     * @return         TODO: [Mattias: I think this should be determined in the interfaces, not here]
     */
    public boolean setColor(final int color)
    {
	//TODO: Set allowed colorspans
	this.color = color;
	// This makes more sense when we are using allowed colors and false is actually an option to be sent back
	return true;
    }
    
    /**
     * Setter for a string representing hexadecimal number
     *
     * @param   color  A string representing a hexadecimal number
     * @return         TODO: [Mattias: I think this should be determined in the interfaces, not here]
     */
    public boolean setColor(final String color) {
	if (this.setColor(hexToInt(color)))
	    return true;
	else
	    return false;
    }
    
    
    /**
     * Returns block color as an integer
     * 
     * @return  The color in raw integer representation
     */
    public int getColor() {
	return this.color;
    }
    
    /**
     * Returns color as a string in hexadecimal format
     * 
     * @return  The color as in hexadecimal representation
     */
    public String getHexColor() {
	return Integer.toHexString(this.color);
    }
    
    
    /**
     * Helper method to convert "hex" string to integer
     *
     * @param   strNum  String to convert to integer
     * @return          The string converted to an integer
     */
    private int hexToInt(final String strNum)
    {
	try {
	    return Integer.valueOf(strNum, 16).intValue();
	}
	catch (Exception err) {
	    throw new Error("Trying to parse String into a color failed for unknown reason.");
	}
    }
    
    
    /**
     * {@inheritDoc}
     */
    public String toString() {
	return "[" + this.getHexColor() + "]";
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean equals(final Object obj)
    {
	if ((obj == null) || (obj instanceof Block == false))
	    return false;
	
	return ((Block)obj).color == this.color;
    }
    
    /**
     * {@inheritDoc}
     */
    public int hashCode() {
	return this.color;
    }
    
}
