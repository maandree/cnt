/**
 * Coop Network Tetris – A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Mattias Andrée, Peyman Eshtiagh,
 *                   Calle Lejdbrandt, Magnus Lundberg
 *
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.gui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;


/**
 * Text pane for message displaying
 * 
 * @author  Magnus Lundberg
 */
public class MessageText extends JTextPane
{
    /**
     * Constructor
     */
    public MessageText()
    {
	this.setBackground(Color.BLACK);
	this.setEditable(false);
    }
    
    
    
    /**
     * Appends a message sent by a player
     * 
     * @param  text    The message to append
     * @param  name    The player whom sent the message
     * @param  colour  The colour of the player
     */
    public void addText(final String text, final String name, final Color colour)
    {
	StyleContext style = StyleContext.getDefaultStyleContext();
	AttributeSet attrs;
	
	attrs = style.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, colour); //Switching to player name style
	this.setCharacterAttributes(attrs, false);
	this.setCaretPosition(getDocument().getLength()); //Moving caret to end so the appended text is ended to the end
	this.setEditable(true);
	
	this.replaceSelection(name + ": "); //Prints the player's name
	
	attrs = style.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.WHITE); //Switing to message style
	this.setCharacterAttributes(attrs, false);
	
	this.replaceSelection(text + "\n"); //Prints the player's message
	
	this.setEditable(false);
    }
    
}

