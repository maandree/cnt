package cnt.interfaces.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class UserList extends JPanel
{
    public UserList()
    {
	setLayout(new BorderLayout());
	model = new DefaultListModel();
	list = new JList(model);
	
	JScrollPane pane = new JScrollPane(list);
	model.addElement("Peyman");
	model.addElement("Calle");
	model.addElement("Magnus");
	model.addElement("Mattias");
	
	add(pane, BorderLayout.NORTH);
	
	this.setBackground(Color.BLACK);
	pane.setBackground(Color.BLACK);
	list.setBackground(Color.BLACK);
    }
    
    
    
    JList list;
    
    DefaultListModel model;
    
}

