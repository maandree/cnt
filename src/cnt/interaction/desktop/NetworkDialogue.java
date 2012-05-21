/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.interaction.desktop;

import javax.swing.*;
import java.awt.*;


/**
 * Network diagnosics dialogue
 *
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
@SuppressWarnings("serial")
public class NetworkDialogue extends JDialog
{
    /**
     * Constructor
     * 
     * @param  owner  Owner window
     */
    public NetworkDialogue(final Window owner)
    {
	super(owner, "Network diagnositics", Dialog.ModalityType.MODELESS);
	
	this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	this.setLocationByPlatform(true);
	
	this.pack();
	this.setSize(new Dimension(400, 300));
	
	buildInterior();
    }
    
    
    
    /**
     * Builds and lays out all components for the frame
     */
    private void buildInterior()
    {
    }
    
}

