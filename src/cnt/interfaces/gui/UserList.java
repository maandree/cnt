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
    }
    
    
    JList list;
    
    DefaultListModel model;
    
    
    public static void main(final String... args)
    {
	JFrame frame = new JFrame("Spellista");
	
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setContentPane(new Spellista());
	frame.setSize(200, 300);
	frame.setVisible(true);
    }
}

