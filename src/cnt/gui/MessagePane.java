/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright Ⓒ 2012  Mattias Andrée, Peyman Eshtiagh,
 *                   Calle Lejdbrandt, Magnus Lundberg
 *
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.gui;

import java.awt.*;
import javax.swing.*;


/**
 * The message viewer pane in the main frame
 * 
 * @author  Magnus Lundberg
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class MessagePane extends JPanel
{
    /**
     * Constructor
     */
    public MessagePane()
    {
	this.messageText = new MessageText();
	this.scrollPane = new JScrollPane(messageText);
	
	this.setLayout(new BorderLayout());
	
	this.add(scrollPane, BorderLayout.CENTER);
    }
    
    
    
    /**
     * Message area
     */
    private final MessageText messageText;
    
    /**
     * Scroll pane
     */
    private final JScrollPane scrollPane;
    
    
    
    /**
     * Appends a text to the message area
     * 
     * @param  text    The message to append
     * @param  name    The player whom sent the message
     * @param  colour  The colour of the player
     */
    public void addText(final String text, final String name, final Color colour)
    {
	messageText.addText(text, name, colour);
    }

}

