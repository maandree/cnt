/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright Ⓒ 2012  Mattias Andrée, Peyman Eshtiagh,
 *                   Calle Lejdbrandt, Magnus Lundberg
 *
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.interaction.gui;

import java.awt.*;
import javax.swing.*;


/**
 * The message viewer pane in the main frame
 * 
 * @author  Magnus Lundberg
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class MessagePane extends JPanel
{
    /**
     * Constructor
     */
    public MessagePane()
    {
	this.setLayout(new BorderLayout());
	this.add(new JScrollPane(new MessageText()), BorderLayout.CENTER);
    }

}

