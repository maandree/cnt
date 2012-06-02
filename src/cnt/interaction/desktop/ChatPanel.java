/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.interaction.desktop;
import cnt.messages.*;
import cnt.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 * The lower part of the main window, the panel with the message pane and message submitter
 * 
 * @author  Magnus Lundberg
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
@SuppressWarnings("serial")
public class ChatPanel extends JPanel implements ActionListener
{
    /**
     * The message displayed when the player as not entered anything.
     */
    private static final String INSTRUCTION = "Type message here";
    
    
    
    /**
     * Constructor
     */
    public ChatPanel()
    {
	this.messages = new MessagePane();
	this.text = new JTextField(INSTRUCTION)
	    {
		/**
		 * Whether the user is in the middle of writing is message
		 */
		protected boolean writing = false;
		
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void processFocusEvent(final FocusEvent e)
		{
		    if (e.getID() == FocusEvent.FOCUS_LOST)
		    {
			this.writing = this.getText().isEmpty() == false;
			if (this.writing == false)
			    this.setText(ChatPanel.INSTRUCTION);
		    }
		    else
		    {
			if (this.writing == false)
			    this.setText("");
		    }
		    
		    super.processFocusEvent(e);
		}
	    };
	
	this.setLayout(new BorderLayout());
	this.add(this.text, BorderLayout.SOUTH);
	this.add(this.messages, BorderLayout.CENTER);
	
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
	if (msg.isEmpty())
	    return;
	
	Blackboard.broadcastMessage(new UserMessage(msg));
	
	this.text.setText("");
    }
    
}

