/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.interaction.desktop;
import cnt.messages.*;
import cnt.network.Toolkit;
import cnt.game.*;
import cnt.*;

import se.kth.maandree.jmenumaker.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.IOException;
import java.io.IOError;
import java.lang.ref.*;


/**
 * This is the main window of the program
 *
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
@SuppressWarnings("serial")
public class GameFrame extends JFrame implements UpdateListener, Blackboard.BlackboardObserver
{
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
	
	int height = 300;
	int width = 300;
	final Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
	final DisplayMode display = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
	
	int _height = (display.getHeight() - 100 - height) / 20;
	int _width = (display.getWidth() - 100 - width) / 10;
	int block = _height < _width ? _height : _width;
	if (block > 32)
	    block = 32;
	
	height += 20 * block;
	width += 10 * block;
	
	this.setSize(new Dimension(width, height));
	this.setLocation(new Point(center.x - (width >> 1), center.y - (height >> 1)));
	
	Blackboard.registerObserver(this);
	
	buildMenus();
	buildInterior();
    }
    
    
    
    /**
     * Menu items
     */
    private HashMap<String, WeakReference<Component>> menuItems;
    
    /**
     * The local player
     */
    private Player localPlayer = null;
    
    
    
    /**
     * Builds and lays out all menu items for the frame
     */
    private void buildMenus()
    {
	try
	{   menuItems = JMenuMaker.makeMenu(this, "GameFrame.jmml", this, null);
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
	
	String xip = "?", lip = "?";
	try
	{   xip = Toolkit.getPublicIP();
	    xip = xip == null ? "?" : xip.isEmpty() ? "?" : xip;
	}
	catch (final Throwable err)
	    { /* Do nothing */ }
	try
	{   lip = Toolkit.getLocalIP();
	    lip = lip == null ? "?" : lip.isEmpty() ? "?" : lip;
	}
	catch (final Throwable err)
	    { /* Do nothing */ }
	
	final JLabel xipLabel, lipLabel;
	status.add(xipLabel = new JLabel("WAN: " + xip), "LEFT");
	status.add(lipLabel = new JLabel("LAN: " + lip), "LEFT");
	status.add(new ScoreLabel(), "RIGHT");
	status.add(new JPanel(), "FILL");
	status.setPreferredSize(new Dimension(0, 20));
	
	xipLabel.setFont(xipLabel.getFont().deriveFont(Font.PLAIN));
	lipLabel.setFont(xipLabel.getFont());
	
	final JPanel gamePanel   = new GamePanel();
	final JPanel playerPanel = new UserList();
	final JPanel chatPanel   = new ChatPanel();
	
	final JSplitPane hSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, SPLIT_LAYOUT_POLICY, gamePanel, playerPanel);
	final JSplitPane vSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, SPLIT_LAYOUT_POLICY, hSplit, chatPanel);
	
	this.add(status, BorderLayout.SOUTH);
	this.add(vSplit, BorderLayout.CENTER);
	
	gamePanel.setPreferredSize(new Dimension(10 * 32, 20 * 32));
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void messageBroadcasted(final Blackboard.BlackboardMessage message)
    {   synchronized (this)
	{
	    if (message instanceof LocalPlayer)
	    {
		this.localPlayer = ((LocalPlayer)message).player;
	    } 
	    else if (message instanceof PlayerPause)
	    {
		if (this.localPlayer == ((PlayerPause)message).player)
		    ((JCheckBoxMenuItem)(menuItems.get("pause").get())).setState(((PlayerPause)message).paused);
	    } 
	    else if (message instanceof EmergencyPause)
	    {
		final WeakReference<Component> ref = menuItems.get("empause");
		if (ref != null)
		    ((JCheckBoxMenuItem)(ref.get())).setState(((EmergencyPause)message).paused);
	    }
	}
    }
    
    
    /**
     * {@inheritDoc}
     */
    protected void processKeyEvent(final KeyEvent e)
    {
	System.out.println(e);
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void itemClicked(final String id)
    {   switch (id)
	{
	    case "friends":
		(new FriendDialogue(this)).setVisible(true);
	        break;
		
	    case "dnses":
		(new DNSDialogue(this, this.localPlayer)).setVisible(true);
	        break;
		
	    case "netdiag":
		(new NetworkDialogue(this)).setVisible(true);
	        break;
		
	    default:
		System.err.println("Unrecognised menu ID for MainFrame: " + id);
		break;
	}
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void valueUpdated(final String id, final boolean value)
    {   switch (id)
	{
	    case "pause":
		if (localPlayer == null)
		    ((JCheckBoxMenuItem)(menuItems.get(id).get())).setState(false);
		else
		    Blackboard.broadcastMessage(new PlayerPause(localPlayer, value));
	        break;
		
	    case "empause":
		Blackboard.broadcastMessage(new EmergencyPause(value));
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
    
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {   return "(GameFrame)";
    }
    
}

