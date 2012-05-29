/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.interaction.desktop;
import cnt.game.*;
import cnt.messages.*;
import cnt.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;


/**
 * Player list panel
 * 
 * @author  Peyman Eshtiagh
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
@SuppressWarnings("serial")
public class UserList extends JPanel implements Blackboard.BlackboardObserver
{
    /**
     * Constructor
     */
    public UserList()
    {
	this.setLayout(new BorderLayout());
	
	this.model = new DefaultListModel<String>();
	this.list = new JList<String>(model);
	
	final JScrollPane pane = new JScrollPane(this.list);
	
	Blackboard.registerObserver(this);
	Blackboard.registerThreadingPolicy(this, Blackboard.DAEMON_THREADING, PlayerDropped.class, PlayerJoined.class);
	
	LookAndFeel.installBorder(pane, "BorderFactory.createEmptyBorder()");
	
	this.add(pane, BorderLayout.CENTER);
	
	this.setBackground(Color.BLACK);
	pane.setBackground(Color.BLACK);
	this.list.setBackground(Color.BLACK);
	this.list.setSelectionBackground(new Color(16, 16, 100));
	
	this.popup = new JPopupMenu();
	this.popup.add(this.menuFriend = new JMenuItem("Add to friend list"));
	
	this.list.setPrototypeCellValue("X");
	final int cellHeight = this.list.getFixedCellHeight();
	
	this.list.addMouseListener(new MouseAdapter()
	        {
		    /**
		     * {@inheritDoc}
		     */
		    public void mouseReleased(final MouseEvent e)
		    {
			// e.isPopupTrigger() returns false
			if (e.getButton() == 3 /*right*/)
			{
			    final int listItemIndex = e.getY() / cellHeight;
			    
			    if (listItemIndex >= UserList.this.model.getSize())
				return;
			    
			    System.err.println("Right clicking on index " + listItemIndex);
			
			    UserList.this.list.setSelectedIndex(listItemIndex);
			    UserList.this.popup.show(e.getComponent(), e.getX(), e.getY());
			}
		    }
	        });
	
	this.menuFriend.addActionListener(new ActionListener()
	        {
		    /**
		     * {@inheritDoc}
		     */
		    public void actionPerformed(final ActionEvent e)
		    {
			System.err.println("[Add to friend list] click");
		    }
	        });
    }
    
    
    
    /**
     * The list component
     */
    private final JList<String> list;
    
    /**
     * The list's model
     */
    private final DefaultListModel<String> model;
    
    /**
     * The popup menu
     */
    private final JPopupMenu popup;
    
    /**
     * The “Add to friend list” menu item
     */
    private final JMenuItem menuFriend;
    
    /**
     * Mapping form players to their list item
     */
    private final HashMap<Player, String> playerMap = new HashMap<Player, String>();
    
    
    
    /**
     * {@inheritDoc}
     */
    public void messageBroadcasted(final Blackboard.BlackboardMessage message)
    {
	synchronized (this)
	{   
	    if (message instanceof PlayerJoined)
	    {
		final Player player = ((PlayerJoined)message).player;
		if (this.playerMap.containsKey(player))
		    return;
		int _colour = player.getID();
		String colour = Integer.toString((_colour >> 16) & 255) + ", ";
		      colour += Integer.toString((_colour >>  8) & 255) + ", ";
		      colour += Integer.toString((_colour >>  0) & 255);
		final String item = "<html><span style=\"color: rgb(" + colour + ");\">" + player.getName() + "</span></html>";
		this.playerMap.put(player, item);
		this.model.addElement(item);
	    }
	    else if (message instanceof PlayerDropped)
	    {
		final Player player = ((PlayerDropped)message).player;
		if (this.playerMap.containsKey(player) == false)
		    return;
		this.playerMap.remove(player);
		this.model.removeElement(this.playerMap.get(player));
	    }
	}
    }
    
}

