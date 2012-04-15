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
public class scrollText extends JPanel
{
    textField textPane;
    JScrollPane scrollPane;
    
    scrollText()
    {
	textPane = new textField();
	scrollPane = new JScrollPane(textPane);
	scrollPane.setPreferredSize(new Dimension(250, 155));
	add(scrollPane);
    }
    
    public void addText(String Text, String Name, Color color)
    {
	textPane.addText(Text, Name, color);
    }

}

