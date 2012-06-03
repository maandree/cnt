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
 * The login frame where the user joins or creates a game
 */
@SuppressWarnings("serial")
public class LoginFrame extends JFrame
{
    /**
     * Constructor
     */
    public LoginFrame()
    {
	super("CNT: Coop Network Tetris");
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
	this.pack();
	this.setSize(new Dimension(400, 300));
        this.setLocationByPlatform(true);
	
	buildInterior();
    }
    
    
    
    /**
     * Builds and lays out all components for the frame
     */
    private void buildInterior()
    {
        this.setLayout(new DockLayout());
	
	final JPanel marginTop    = new JPanel();
	final JPanel marginBottom = new JPanel();
	final JPanel marginLeft   = new JPanel();
	final JPanel marginRight  = new JPanel();
	marginTop   .setPreferredSize(new Dimension(8, 8));
	marginBottom.setPreferredSize(new Dimension(8, 8));
	marginLeft  .setPreferredSize(new Dimension(8, 8));
	marginRight .setPreferredSize(new Dimension(8, 8));
	this.add(marginTop,    DockLayout.TOP);
	this.add(marginBottom, DockLayout.BOTTOM);
	this.add(marginLeft,   DockLayout.LEFT);
	this.add(marginRight,  DockLayout.RIGHT);
	
	final JTextField nameField = new JTextField();
	final JLabel nameLabel = new JLabel("Name:");
	nameLabel.setLabelFor(nameField);
	nameLabel.setDisplayedMnemonic('N');
	nameLabel.setDisplayedMnemonicIndex(0);
	
	final JTextField remoteField = new JTextField();
	final JLabel remoteLabel = new JLabel("Remote:");
	remoteLabel.setLabelFor(remoteField);
	remoteLabel.setDisplayedMnemonic('R');
	remoteLabel.setDisplayedMnemonicIndex(0);
	
	final JTextField portField = new JTextField();
	final JLabel portLabel = new JLabel("Port:");
	portLabel.setLabelFor(portField);
	portLabel.setDisplayedMnemonic('P');
	portLabel.setDisplayedMnemonicIndex(0);
	
	int width = 0;
	if (width < nameLabel.getPreferredSize().width)
	    width = nameLabel.getPreferredSize().width;
	if (width < remoteLabel.getPreferredSize().width)
	    width = remoteLabel.getPreferredSize().width;
	if (width < portLabel.getPreferredSize().width)
	    width = portLabel.getPreferredSize().width;
	width += 8;
	
	nameLabel  .setPreferredSize(new Dimension(width, nameLabel  .getPreferredSize().height));
	remoteLabel.setPreferredSize(new Dimension(width, remoteLabel.getPreferredSize().height));
	portLabel  .setPreferredSize(new Dimension(width, portLabel  .getPreferredSize().height));
	
	final JPanel   namePanel = new JPanel(new BorderLayout());
	final JPanel remotePanel = new JPanel(new BorderLayout());
	final JPanel   portPanel = new JPanel(new BorderLayout());
	namePanel  .add(  nameLabel, BorderLayout.WEST);
	remotePanel.add(remoteLabel, BorderLayout.WEST);
	portPanel  .add(  portLabel, BorderLayout.WEST);
	namePanel  .add(  nameField, BorderLayout.CENTER);
	remotePanel.add(remoteField, BorderLayout.CENTER);
	portPanel  .add(  portField, BorderLayout.CENTER);
	
	final JPanel spacing0 = new JPanel();  spacing0.setPreferredSize(new Dimension(8, 8));
	final JPanel spacing1 = new JPanel();  spacing1.setPreferredSize(new Dimension(8, 8));
	
	this.add(  namePanel, DockLayout.TOP);
	this.add(   spacing0, DockLayout.TOP);
	this.add(remotePanel, DockLayout.TOP);
	this.add(   spacing1, DockLayout.TOP);
	this.add(  portPanel, DockLayout.TOP);
    }
    
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {   return "(LoginFrame)";
    }
    
    
    
    public static void main(final String... args)
    {
	(new LoginFrame()).setVisible(true);
    }
    
}

