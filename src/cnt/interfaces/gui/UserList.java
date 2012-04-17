/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright Ⓒ 2012  Mattias Andrée, Peyman Eshtiagh,
 *                   Calle Lejdbrandt, Magnus Lundberg
 *
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.interfaces.gui;

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
	
	this.model = new DefaultListModel();
	this.list = new JList(model);
	
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
    }
    
    
    
    /**
     * The list component
     */
    private final JList list;
    
    /**
     * The list's model
     */
    private final DefaultListModel model;
    
}

