/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright Ⓒ 2012  Mattias Andrée, Peyman Eshtiagh,
 *                   Calle Lejdbrandt, Magnus Lundberg
 *
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.interfaces.gui;
import cnt.control.*;

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
public class ChatPanel extends JPanel implements ActionListener, MouseListener
{
    /**
     * Desired by {@link Serializable}
     */
    private static final long serialVersionUID = 1L;
    
    
    
    /**
     * The message displayed when the player as not entered anything.<br/>
     * Identity check is not possible becuase {@link JTextField#getText()} clones the text on call.
     */
    private static final String INSTRUCTION = "Type message here\u200c";
    //U+200C is an invisible character used to recognised whether or not the message is inserted by the program
    
    
    
    /**
     * Constructor
     */
    public ChatPanel()
    {
	this.messages = new MessagePane();
	this.text = new JTextField(INSTRUCTION);
	
	this.setLayout(new BorderLayout());
	this.add(this.text, BorderLayout.SOUTH);
	this.add(this.messages, BorderLayout.CENTER);
	
	this.text.addActionListener(this);
	this.text.addMouseListener(this);
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
	
	Blackboard.broadcastMessage(new Blackboard.ChatMessage("Magnus", Color.RED, msg));
	
	this.text.setText(INSTRUCTION); //Reset message field
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void mousePressed(final MouseEvent e)
    {
	if (e.getButton() != 1) //left button
	    return;
	
	//Emptying the field when pressing the field
	
	if (this.text.getText().equals(INSTRUCTION))
	    this.text.setText("");
    }
    
    
    
    /**
     * {@inheritDoc}
     */
    public void mouseExited(final MouseEvent e) { /* Nothing to do */ }
    
    /**
     * {@inheritDoc}
     */
    public void mouseEntered(final MouseEvent e) { /* Nothing to do */ }
    
    /**
     * {@inheritDoc}
     */
    public void mouseReleased(final MouseEvent e) { /* Nothing to do */ }
    
    /**
     * {@inheritDoc}
     */
    public void mouseClicked(final MouseEvent e) { /* Nothing to do */ }
    
}

