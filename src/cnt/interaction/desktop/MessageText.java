/**
 * Coop Network Tetris – A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.interaction.desktop;
import cnt.interaction.*;
import cnt.messages.*;
import cnt.*;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;


/**
 * Text pane for message displaying
 * 
 * @author  Magnus Lundberg
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
@SuppressWarnings("serial")
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
	Blackboard.registerThreadingPolicy(this, Blackboard.NICE_DAEMON_THREADING, ChatMessage.class, SystemMessage.class);
    }
    
    
    
    /**
     * {@inheritDoc}
     */
    public void messageBroadcasted(final Blackboard.BlackboardMessage message)
    {
	synchronized (this)
	{
	    if (message instanceof ChatMessage)
	    {
		final ChatMessage msg = (ChatMessage)message;
		addUserText(msg.message, msg.player.getName(), ColourMapper.getColour(msg.player.getID()));
	    }
	    else if (message instanceof SystemMessage)
	    {
		final SystemMessage msg = (SystemMessage)message;
		addSystemText(msg.message, msg.player == null ? null : ColourMapper.getColour(msg.player.getID()));
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
    private void addUserText(final String text, final String name, final Color colour)
    {
	StyleContext style = StyleContext.getDefaultStyleContext();
	AttributeSet attrs;
	
	
	attrs = style.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, colour); //Switching to player name style
	attrs = style.addAttribute(attrs, StyleConstants.Bold, Boolean.TRUE);
	
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
	
	this.setSelectionStart(pos2);	this.setSelectionEnd(pos2);
	this.setEditable(false);
    }
    
    
    /**
     * Appends a message sent by the system
     * 
     * @param  text    The message to append
     * @param  colour  The colour of the player, <code>null</code> if none
     */
    private void addSystemText(final String text, final Color colour)
    {
	StyleContext style = StyleContext.getDefaultStyleContext();
	AttributeSet attrs;
	
	final int pos0 = getDocument().getLength();
	final int pos1 = pos0 + (text + "\n").length();
	
	this.setEditable(true);
	
	attrs = style.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, colour == null ? Color.GRAY : colour);
	attrs = style.addAttribute(attrs, StyleConstants.Italic, Boolean.TRUE);
	
	
	this.setSelectionStart(pos0);	this.setSelectionEnd(pos0);
	this.replaceSelection(text + "\n"); //Prints the system's message
	this.setSelectionStart(pos0);	this.setSelectionEnd(pos1);
	this.setCharacterAttributes(attrs, true);
	
	this.setSelectionStart(pos1);	this.setSelectionEnd(pos1);
	this.setEditable(false);
    }
    
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {   return "(MessageText)";
    }
    
}

