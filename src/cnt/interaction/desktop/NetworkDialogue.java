/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.interaction.desktop;
import cnt.network.Diagnostics;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;


/**
 * Network diagnosics dialogue
 *
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
@SuppressWarnings("serial")
public class NetworkDialogue extends JDialog
{
    /**
     * Constructor
     * 
     * @param  owner  Owner window
     */
    public NetworkDialogue(final Window owner)
    {
	super(owner, "Network diagnositics", Dialog.ModalityType.MODELESS);
	
	this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	this.setLocationByPlatform(true);
	
	this.pack();
	this.setSize(new Dimension(400, 300));
	
	buildInterior();
    }
    
    
    
    /**
     * Builds and lays out all components for the frame
     */
    private void buildInterior()
    {
	this.setLayout(new BorderLayout());
	
	String data = Diagnostics.run();
	
	if (data.endsWith("\n"))
	    data = data.substring(0, data.length());
	String lines[] = data.split("\n");
	final DefaultMutableTreeNode[] stack = new DefaultMutableTreeNode[lines.length];
	int ptr = 0;
	stack[ptr] = new DefaultMutableTreeNode("Diagnostics", true);
	int indent = -1;
	for (final String line : lines)
	{
	    int ind = 0;
	    while (line.charAt(ind) == ' ')
		ind++;
	    
	    final DefaultMutableTreeNode node = new DefaultMutableTreeNode(line.substring(ind), true);
	    if (ind > indent)
	    {
		stack[ptr].add(node);
		stack[++ptr] = node;
	    }
	    else if (ind == indent)
	    {
		stack[--ptr].add(node);
		stack[++ptr] = node;
	    }
	    else
	    {
		ptr -= indent - ind;
		indent -= indent - ind;
		stack[--ptr].add(node);
		stack[++ptr] = node;
	    }
	    indent = ind;
	}
	
	
	final JTree tree = new JTree();
	((DefaultTreeModel)(tree.getModel())).setRoot(stack[0]);
	final JScrollPane scrolls = new JScrollPane(tree);
	this.add(scrolls, BorderLayout.CENTER);
    }
    
}

