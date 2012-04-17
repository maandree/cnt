/**
 * Coop Network Tetris – A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Mattias Andrée, Peyman Eshtiagh,
 *                   Calle Lejdbrandt, Magnus Lundberg
 *
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.gui;
import cnt.control.*;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;


/**
 * Text pane for message displaying
 * 
 * @author  Magnus Lundberg
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class MessageText extends JTextPane implements Blackboard.BlackboardObserver
{
    /**
     * Constructor
     */
    public MessageText()
    {
	this.setBackground(Color.BLACK);
	this.setEditable(false);
	
	Blackboard.registerObserver(this);
	Blackboard.registerThreadingPolicy(this, Blackboard.ChatMessage.class, Blackboard.NICE_DAEMON_THREADING);
    }
    
    
    
    /**
     * {@inheritDoc}
     */
    public void messageBroadcasted(final Blackboard.BlackboardMessage message)
    {
	synchronized (this)
	{
	    if (message instanceof Blackboard.ChatMessage)
	    {
		final Blackboard.ChatMessage msg = (Blackboard.ChatMessage)message;
		addText(msg.message, msg.player, msg.colour);
	    }
	}
    }
    
    /**
     * Appends a message sent by a player
     * 
     * @param  text    The message to append
     * @param  name    The player whom sent the message
     * @param  colour  The colour of the player
     */
    private void addText(final String text, final String name, final Color colour)
    {
	StyleContext style = StyleContext.getDefaultStyleContext();
	AttributeSet attrs;
	
	attrs = style.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, colour); //Switching to player name style
	
	final int pos0 = getDocument().getLength();
	final int pos1 = pos0 + (name + ": ").length();
	final int pos2 = pos1 + (text + "\n").length();
	
	this.setEditable(true);
	
	this.setSelectionStart(pos0);	this.setSelectionEnd(pos0);
	this.replaceSelection(name + ": "); //Prints the player's name
	this.setSelectionStart(pos0);	this.setSelectionEnd(pos1);
	this.setCharacterAttributes(attrs, true);
	
	attrs = style.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.WHITE); //Switing to message style
	
	
	this.setSelectionStart(pos1);	this.setSelectionEnd(pos1);
	this.replaceSelection(text + "\n"); //Prints the player's message
	this.setSelectionStart(pos1);	this.setSelectionEnd(pos2);
	this.setCharacterAttributes(attrs, true);
	
	this.setSelectionStart(pos1);	this.setSelectionEnd(pos1);
	this.setEditable(false);
    }
    
}

