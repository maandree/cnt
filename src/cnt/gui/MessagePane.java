/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright Ⓒ 2012  Mattias Andrée, Peyman Eshtiagh,
 *                   Calle Lejdbrandt, Magnus Lundberg
 *
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.gui;

import java.awt.*;
import javax.swing.*;


/**
 * @author  Magnus Lundberg
 */
public class MessagePane extends JPanel
{
    MessagePane()
    {
	messageText = new MessageText();
	scrollPane = new JScrollPane(messageText);
	scrollPane.setPreferredSize(new Dimension(250, 155));
	add(scrollPane);
    }
    
    
    
    MessageText messageText;
    JScrollPane scrollPane;
    
    
    
    public void addText(String Text, String Name, Color color)
    {
	messageText.addText(Text, Name, color);
    }

}

