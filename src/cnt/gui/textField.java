/**
 * Coop Network Tetris – A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Mattias Andrée, Peyman Eshtiagh,
 *                   Calle Lejdbrandt, Magnus Lundberg
 *
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.gui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;


/**
 * @author  Magnus Lundberg
 */
public class textField extends JTextPane
{
    //Has default constructor
    
    
    /**
     * addText lägger till texten den fÃ¥r som parameter sist i TextPanen. Innan den lägger den till 
     * Spelarens namn som skrivs i färgen den fÃ¥r som tredje parameter
     * @param Text
     * @param Name
     * @param color
     */
    void addText(String Text, String Name, Color color)
    {
	StyleContext sc = StyleContext.getDefaultStyleContext();
	AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,StyleConstants.Foreground, color);	//Skapar textens attribut
	setCharacterAttributes(aset, false);
	setCaretPosition(getDocument().getLength());	//Säger att den ska skriva sist i dokumentet
	setEditable(true);
	replaceSelection(Name + ">");		//Skriver ut användarens namn i rätt färg
	aset = sc.addAttribute(SimpleAttributeSet.EMPTY,StyleConstants.Foreground, Color.black);	//Ändrar textfärgen till svart
	setCharacterAttributes(aset, false);
	replaceSelection(Text + "\n");			//Skriver ut det användaren skrev
	setEditable(false);	
    }
	
}

