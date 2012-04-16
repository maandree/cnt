/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright Ⓒ 2012  Mattias Andrée, Peyman Eshtiagh,
 *                   Calle Lejdbrandt, Magnus Lundberg
 *
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;


/**
 * The lower part of the main window, the panel with the message pane and message submitter
 * 
 * @author  Magnus Lundberg
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class ChatPanel extends JPanel implements ActionListener
{
    /**
     * Desired by {@link Serializable}
     */
    private static final long serialVersionUID = 1L;
    
    
    
    /**
     * Constructor
     */
    public ChatPanel()
    {
	this.messages = new MessagePane();
	this.text = new JTextField("Type message here");
	
	this.setLayout(new GridLayout(2, 1));
	add(this.messages);
	add(this.text);
	
	this.text.addActionListener(this);
    }
    
    
    
    /**
     * The message submission field
     */
    private final JTextField text;
    
    /**
     * The message pane with all messages
     */
    private final MessagePane messages;
    
    
    
    /**
     * This method is invoked the player presses Enter/Return in {@link #text}
     *
     * @param  e  Event parameter (unused)
     */
    public void actionPerformed(final ActionEvent e)
    {
	final String msg = this.text.getText(); //Retrieves typed message
	
	messages.addText(msg, "Magnus", Color.RED);
	
	this.text.setText("Type message here"); //Reset message field
    }
    
}

