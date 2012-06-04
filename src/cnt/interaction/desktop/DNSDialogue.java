/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.interaction.desktop;
import cnt.game.*;
import cnt.local.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;



/**
 * DNS name dialogue
 *
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
@SuppressWarnings("serial")
public class DNSDialogue extends JDialog
{
    /**
     * Constructor
     * 
     * @param  owner  Owner window
     */
    public DNSDialogue(final Window owner)
    {
	super(owner, "DNS names", Dialog.ModalityType.MODELESS);
	
	this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	this.setLocationByPlatform(true);
	
	this.pack();
	this.setSize(new Dimension(600, 400));
	
	this.text = new JTextArea();
	
	buildInterior();
    }
    
    
    
    /**
     * The text component
     */
    final JTextArea text;
    
    
    
    /**
     * Builds and lays out all components for the frame
     */
    private void buildInterior()
    {
	this.setLayout(new BorderLayout());
	final JScrollPane pane = new JScrollPane(this.text);
	this.add(pane, BorderLayout.CENTER);
    }
    
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {   return "(DNSDialogue)";
    }
    
}
