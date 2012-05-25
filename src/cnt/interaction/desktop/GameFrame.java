/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.interaction.desktop;

import se.kth.maandree.jmenumaker.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.IOError;


/**
 * This is the main window of the program
 *
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
@SuppressWarnings("serial")
public class GameFrame extends JFrame implements UpdateListener
{
    /**
     * The default total frame width
     */
    private static final int DEFAULT_WIDTH = 640;
    
    /**
     * The default total frame height
     */
    private static final int DEFAULT_HEIGHT = 900;
    
    /**
     * The default block width an height
     */
    private static final int DEFAULT_BLOCK_SIZE = 32;
    
    /**
     * Whether or not the views of the split panes are continuously
     * redisplayed while resizing.
     */
    private static final boolean SPLIT_LAYOUT_POLICY = true;
    
    
    
    /**
     * Constructor
     */
    public GameFrame()
    {
	super("cnt: Coop Network Tetris");
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //TODO only on demo
	
	this.setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT)); //TODO temporary, should depend on screen
	this.setLocationByPlatform(true);
	
	buildMenus();
	buildInterior();
    }
    
    
    
    /**
     * Builds and lays out all menu items for the frame
     */
    private void buildMenus()
    {
	try
	{   JMenuMaker.makeMenu(this, "GameFrame.jmml", this, null);
	}
	catch (final IOException err)
	{   throw new IOError(err);
	}
    }
    
    
    /**
     * Builds and lays out all components for the frame
     */
    private void buildInterior()
    {
	this.setLayout(new BorderLayout());
	
	final StatusPane status = new StatusPane();
	status.add(new ScoreLabel(), "RIGHT");
	status.add(new JPanel(), "FILL");
	status.setPreferredSize(new Dimension(0, 20));
	
	final JPanel gamePanel   = new GamePanel();
	final JPanel playerPanel = new UserList();
	final JPanel chatPanel   = new ChatPanel();
	
	final JSplitPane hSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, SPLIT_LAYOUT_POLICY, gamePanel, playerPanel);
	final JSplitPane vSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, SPLIT_LAYOUT_POLICY, hSplit, chatPanel);
	
	
	this.add(status, BorderLayout.SOUTH);
	this.add(vSplit, BorderLayout.CENTER);
	
	gamePanel.setPreferredSize(new Dimension(10 * DEFAULT_BLOCK_SIZE, 20 * DEFAULT_BLOCK_SIZE));
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void itemClicked(final String id)
    {
	switch (id)
	{
	    case "netdiag":
		(new NetworkDialogue(this)).setVisible(true);
	        break;
		
	    default:
		System.err.println("Unrecognised menu ID for MainFrame: " + id);
		break;
	}
    }
    
    public void valueUpdated(final String id, final String value)  { /*Not used*/ }
    public void valueUpdated(final String id, final double value)  { /*Not used*/ }
    public void valueUpdated(final String id, final long value)    { /*Not used*/ }
    public void valueUpdated(final String id, final int value)     { /*Not used*/ }
    public void valueUpdated(final String id, final boolean value) { /*Not used*/ }
    
}

