/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.interaction.desktop;
import cnt.local.*;
import cnt.game.*;

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
     * @param  owner        Owner window
     * @param  localPlayer  The local player
     */
    public DNSDialogue(final Window owner, final Player localPlayer)
    {
	super(owner, "DNS names", Dialog.ModalityType.MODELESS);
	
	this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	this.setLocationByPlatform(true);
	
	this.pack();
	this.setSize(new Dimension(600, 400));
	
	this.localPlayer = localPlayer;
	this.text = new JTextArea();
	
	final StringBuilder lines = new StringBuilder();
	for (final String line : localPlayer.getDNSes())
	{   lines.append(line);
	    lines.append('\n');
	}
	
	this.text.setText(lines.toString());
	
	buildInterior();
    }
    
    
    
    /**
     * The text component
     */
    final JTextArea text;
    
    /**
     * The local player
     */
    final Player localPlayer;
    
    
    
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
    protected void processWindowEvent(final WindowEvent e)
    {
	if (e.getID() == WindowEvent.WINDOW_CLOSING)
	{
	    final String[] lines = this.text.getText().split("\n");
	    final ArrayList<String> dnses = this.localPlayer.getDNSes();
	    dnses.clear();
	    for (final String line : lines)
		if (line.isEmpty() == false)
		{
		    System.err.println("Specified DNS: " + line);
		    dnses.add(line);
		}
	    Friends.updateMe(this.localPlayer);
	}
	
	super.processWindowEvent(e);
    }
    
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {   return "(DNSDialogue)";
    }
    
}
