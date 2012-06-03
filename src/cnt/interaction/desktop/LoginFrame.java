/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.interaction.desktop;
import cnt.messages.*;
import cnt.local.*;
import cnt.game.*;
import cnt.*;

import se.kth.maandree.libandree.gui.layout.DockLayout;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;


/**
 * The login frame where the user joins or creates a game
 */
@SuppressWarnings("serial")
public class LoginFrame extends JFrame implements ActionListener, DocumentListener, ItemListener
{
    /**
     * Constructor
     */
    public LoginFrame()
    {
	super("CNT: Coop Network Tetris");
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
	final int width = 400;
	final int height = 300;
	
	final Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
	final DisplayMode display = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
	
	this.pack();
	this.setSize(new Dimension(width, height));
	this.setLocation(new Point(center.x - (width >> 1), center.y - (height >> 1)));
	
	final Player[] players = Friends.getFriends();
	final Friend[] friends = new Friend[players.length + 1];
	friends[0] = new Friend(null);
	for (int i = 0, n = players.length; i < n; i++)
	    friends[i + 1] = new Friend(players[i]);
	friendList = new JComboBox<Friend>(friends);
	
	buildInterior();
	
	remoteField  .addActionListener(this);
	portField    .addActionListener(this);
	startButton  .addActionListener(this);
	diagnosButton.addActionListener(this);
	
	remoteField.getDocument().addDocumentListener(this);
	portField  .getDocument().addDocumentListener(this);
	
	friendList.addItemListener(this);
    }
    
    
    
    /**
     * Text field for the player name
     */
    private final JTextField nameField = new JTextField();
    
    /**
     * Text field for the remote player
     */
    private final JTextField remoteField = new JTextField();
    
    /**
     * Text field for the game port
     */
    private final JTextField portField = new JTextField();
    
    /**
     * List of friends
     */
    private final JComboBox<Friend> friendList;
    
    /**
     * Create/Join game button
     */
    private final JButton startButton = new JButton("Create");
    
    /**
     * Network diagnostics button
     */
    private final JButton diagnosButton = new JButton("Diagnostics");
    
    
    
    /**
     * Friend displayer
     */
    public class Friend
    {
	/**
	 * Constructor
	 * 
	 * @param  player  The player
	 */
	public Friend(final Player player)
	{
	    this.player = player;
	}
	
	

	/**
	 * The player
	 */
	public final Player player;
	
	
	
	/**
	 * {@inheritDoc}
	 */
	public String toString()
	{   return this.player == null ? "" : this.player.getName();
	}
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
	
	final JLabel nameLabel = new JLabel("Name:");
	nameLabel.setLabelFor(nameField);
	nameLabel.setDisplayedMnemonic('N');
	nameLabel.setDisplayedMnemonicIndex(0);
	nameLabel.setToolTipText("Your display name in the game.");
	this.nameField.setToolTipText(nameLabel.getToolTipText());
	
	final JLabel remoteLabel = new JLabel("Remote:");
	remoteLabel.setLabelFor(remoteField);
	remoteLabel.setDisplayedMnemonic('R');
	remoteLabel.setDisplayedMnemonicIndex(0);
	remoteLabel.setToolTipText("The IP address, DNS name or friend to connect to to play, \nleave empty to create a new game.");
	this.remoteField.setToolTipText(remoteLabel.getToolTipText());
	
	final JLabel portLabel = new JLabel("Port:"); 
	portLabel.setLabelFor(portField);
	portLabel.setDisplayedMnemonic('P');
	portLabel.setDisplayedMnemonicIndex(0);
	portLabel.setToolTipText("The port the remote client has openned to game on, \nleave empty if you create a new game and want a random port.");
	this.portField.setToolTipText(portLabel.getToolTipText());
	
	int width = 0;
	if (width < nameLabel.getPreferredSize().width)
	    width = nameLabel.getPreferredSize().width;
	if (width < remoteLabel.getPreferredSize().width)
	    width = remoteLabel.getPreferredSize().width;
	if (width < portLabel.getPreferredSize().width)
	    width = portLabel.getPreferredSize().width;
	width += 8;
	
	final JPanel nilPanel = new JPanel();
	
	nameLabel  .setPreferredSize(new Dimension(width, nameLabel  .getPreferredSize().height));
	remoteLabel.setPreferredSize(new Dimension(width, remoteLabel.getPreferredSize().height));
	portLabel  .setPreferredSize(new Dimension(width, portLabel  .getPreferredSize().height));
	nilPanel   .setPreferredSize(new Dimension(width, nilPanel   .getPreferredSize().height));
	
	final JPanel   namePanel = new JPanel(new BorderLayout());
	final JPanel remotePanel = new JPanel(new BorderLayout());
	final JPanel   portPanel = new JPanel(new BorderLayout());
	final JPanel friendPanel = new JPanel(new BorderLayout());
	namePanel  .add(  nameLabel, BorderLayout.WEST);
	remotePanel.add(remoteLabel, BorderLayout.WEST);
	friendPanel.add(nilPanel,    BorderLayout.WEST);
	portPanel  .add(  portLabel, BorderLayout.WEST);
	namePanel  .add(this.  nameField, BorderLayout.CENTER);
	remotePanel.add(this.remoteField, BorderLayout.CENTER);
	friendPanel.add(this.friendList,  BorderLayout.CENTER);
	portPanel  .add(this.  portField, BorderLayout.CENTER);
	
	final JPanel spacing0 = new JPanel();  spacing0.setPreferredSize(new Dimension(8, 8));
	final JPanel spacing1 = new JPanel();  spacing1.setPreferredSize(new Dimension(8, 8));
	final JPanel spacing2 = new JPanel();  spacing1.setPreferredSize(new Dimension(8, 8));
	
	this.add(  namePanel, DockLayout.TOP);
	this.add(   spacing0, DockLayout.TOP);
	this.add(remotePanel, DockLayout.TOP);
	this.add(   spacing1, DockLayout.TOP);
	this.add(friendPanel, DockLayout.TOP);
	this.add(   spacing2, DockLayout.TOP);
	this.add(  portPanel, DockLayout.TOP);
	
	final JPanel buttonPanel = new JPanel(new BorderLayout());
	buttonPanel.setPreferredSize(startButton.getPreferredSize());
	this.startButton.setToolTipText("Start playing");
	this.diagnosButton.setToolTipText("Run network diagnostics");
	buttonPanel.add(this.startButton, BorderLayout.EAST);
	buttonPanel.add(this.diagnosButton, BorderLayout.WEST);
	this.add(buttonPanel, DockLayout.BOTTOM);
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void actionPerformed(final ActionEvent e)
    {
	final Object source = e.getSource();
	
	final boolean newGame = this.remoteField.getText().isEmpty();
	
	int port;
	try
	{   if (this.portField.getText().isEmpty())
		port = 0;
	    else
		port = Integer.parseInt(this.portField.getText());
	}
	catch (final Exception err)
	{   port = -1;
	}
	
	boolean ok = port > 1024;
	ok |= port == 0;
	ok &= port != 49151;
	ok &= port != 49152;
	ok &= port < 65535;
	ok &= (port != 0) || newGame;
	
	if (source == this.diagnosButton)
	{   (new NetworkDialogue(this)).setVisible(true);
	}
	else if (source == this.startButton)
	{   Blackboard.broadcastMessage(new JoinGame(this.nameField.getText(), this.remoteField.getText(), port));
	}
	else if (source == this.remoteField)
	{   this.startButton.setText(newGame ? "Create" : "Join");
	    this.startButton.setEnabled(ok);
	}
	else if (source == this.portField)
	{   this.startButton.setEnabled(ok);
	}
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void changedUpdate(final DocumentEvent e)
    {
	final boolean newGame = this.remoteField.getText().isEmpty();
	
	int port;
	try
        {   if (this.portField.getText().isEmpty())
		port = 0;
	    else
		port = Integer.parseInt(this.portField.getText());
	}
	catch (final Exception err)
	{   port = -1;
	}
	
	boolean ok = port > 1024;
	ok |= port == 0;
	ok &= port != 49151;
	ok &= port != 49152;
	ok &= port < 65535;
	ok &= (port != 0) || newGame;
	    
	if (e.getDocument() == this.remoteField.getDocument())
	{   this.startButton.setText(newGame ? "Create" : "Join");
	    this.startButton.setEnabled(ok);
	}
	else if (e.getDocument() == this.portField.getDocument())
	{   this.startButton.setEnabled(ok);
	}
    }
    
    /**
     * {@inheritDoc}
     */
    public void insertUpdate(final DocumentEvent e)
    {   changedUpdate(e);
    }
    
    /**
     * {@inheritDoc}
     */
    public void removeUpdate(final DocumentEvent e)
    {   changedUpdate(e);
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void itemStateChanged(final ItemEvent e)
    {
	if (e.getStateChange() == ItemEvent.SELECTED)
	{
	    final Friend friend = (Friend)(e.getItem());
	    if (friend.player == null)
		this.remoteField.setText("");
	    else
		this.remoteField.setText(friend.player.getReachable());
	}
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

