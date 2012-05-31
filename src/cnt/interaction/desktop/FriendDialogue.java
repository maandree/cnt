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
import java.util.HashMap;



/**
 * Friend dialogue
 *
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
@SuppressWarnings("serial")
public class FriendDialogue extends JDialog
{
    /**
     * Constructor
     * 
     * @param  owner  Owner window
     */
    public FriendDialogue(final Window owner)
    {
	super(owner, "Friends", Dialog.ModalityType.MODELESS);
	
	this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	//This.setLocationByPlatform(true);
	
	this.pack();
	this.setSize(new Dimension(400, 600));
	
	this.model = new DefaultListModel<String>();
	this.list = new JList<String>(model);
	this.popup = new JPopupMenu();
	this.menuFriend = new JMenuItem("Remove from friend list");
	
	for (final Player friend : Friends.getFriends())
	{
	    final String itemtag = (friend.getUUID() == null ? "(null)" : friend.getUUID().toString());
	    final String item = "<html><!-- " + itemtag + " -->" + friend.getName() + "</html>";
	    this.playerMap.put(friend, item);
	    this.playerReverseMap.put(item, friend);
	    this.model.addElement(item);
	}
	
	buildInterior();
    }
    
    
    
    /**
     * The list component
     */
    final JList<String> list;
    
    /**
     * The list's model
     */
    final DefaultListModel<String> model;
    
    /**
     * The popup menu
     */
    final JPopupMenu popup;
    
    /**
     * The “Remove from friend list” menu item
     */
    final JMenuItem menuFriend;
    
    /**
     * Mapping from players to their list item
     */
    final HashMap<Player, String> playerMap = new HashMap<Player, String>();
    
    /**
     * Mapping from the players' list item to the players themself
     */
    final HashMap<String, Player> playerReverseMap = new HashMap<String, Player>();
    
    /**
     * The current right clicked player
     */
    Player selected = null;
    
    
    
    /**
     * Builds and lays out all components for the frame
     */
    private void buildInterior()
    {
	this.setLayout(new BorderLayout());
	
	final JScrollPane pane = new JScrollPane(this.list);
	
	LookAndFeel.installBorder(pane, "BorderFactory.createEmptyBorder()");
	
	this.add(pane, BorderLayout.CENTER);
	
	this.popup.add(this.menuFriend);
	
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
			    
			    if (listItemIndex >= FriendDialogue.this.model.getSize())
				return;
			    
			    System.err.println("Right clicking on index " + listItemIndex);
			    
			    FriendDialogue.this.list.setSelectedIndex(listItemIndex);
			    FriendDialogue.this.selected = FriendDialogue.this.playerReverseMap.get(FriendDialogue.this.model.getElementAt(listItemIndex));
			    FriendDialogue.this.popup.show(e.getComponent(), e.getX(), e.getY());
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
			if (FriendDialogue.this.selected == null)
			    System.err.println("This is not supposted to happen: FriendDialogue.this.selected == null");
			else
			{
			    final Player player = FriendDialogue.this.selected;
			    FriendDialogue.this.playerReverseMap.remove(FriendDialogue.this.playerMap.get(player));
			    FriendDialogue.this.model.removeElement(FriendDialogue.this.playerMap.get(player));
			    FriendDialogue.this.playerMap.remove(player);
			    Friends.removeFriend(player);
			}
		    }
	        });
    }
    
}
