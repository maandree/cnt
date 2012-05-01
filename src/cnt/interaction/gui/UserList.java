/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.interaction.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 * Player list panel
 * 
 * @author  Peyman Eshtiagh
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class UserList extends JPanel
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
	this.model.addElement("<html><span style=\"color: rgb(0, 255, 0);\">Peyman</span></html>");
	this.model.addElement("<html><span style=\"color: rgb(255, 255, 0);\">Calle</span></html>");
	this.model.addElement("<html><span style=\"color: rgb(255, 0, 0);\">Magnus</span></html>");
	this.model.addElement("<html><span style=\"color: rgb(0, 0, 255);\">Mattias</span></html>");
	
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
    
}

