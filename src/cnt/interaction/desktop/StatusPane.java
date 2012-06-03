/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.interaction.desktop;

import se.kth.maandree.libandree.gui.layout.DockLayout;

import javax.swing.*;
import java.awt.*;


/**
 * Status pane for frames
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
@SuppressWarnings("serial")
public class StatusPane extends JPanel
{
    /**
     * Constructor
     */
    public StatusPane()
    {
	this.setLayout(new DockLayout());
    }
    
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void add(final Component comp, final Object contraints)
    {
	final JPanel panel = new JPanel();
	panel.setBorder(BorderFactory.createLoweredBevelBorder());
	panel.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 0));
	
	panel.add(comp);
	super.add(panel, contraints);
    }
    
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {   return "(StatusPane)";
    }
    
}
