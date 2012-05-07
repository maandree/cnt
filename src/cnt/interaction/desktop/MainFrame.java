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
 * This is the main window of the program
 *
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class MainFrame extends JFrame
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
    public MainFrame()
    {
	super("cnt: Coop Network Tetris");
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //TODO only on demo
	
	this.setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT)); //TODO temporary, should depend on screen
	
	decorate();
    }
    
    
    
    /**
     * Decorates the window
     */
    private void decorate()
    {
	this.setLayout(new BorderLayout());
	
	final JPanel gamePanel   = new GamePanel();
	final JPanel playerPanel = new UserList();
	final JPanel chatPanel   = new ChatPanel();
	
	final JSplitPane hSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, SPLIT_LAYOUT_POLICY, gamePanel, playerPanel);
	final JSplitPane vSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, SPLIT_LAYOUT_POLICY, hSplit, chatPanel);
	
	this.add(vSplit, BorderLayout.CENTER);
	
	gamePanel.setPreferredSize(new Dimension(10 * DEFAULT_BLOCK_SIZE, 20 * DEFAULT_BLOCK_SIZE));
    }
    
}

