import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;


public class textField extends JTextPane{
	textField(){
	}
	/**
	 * addText l�gger till texten den f�r som parameter sist i TextPanen. Innan den l�gger den till 
	 * Spelarens namn som skrivs i f�rgen den f�r som tredje parameter
	 * @param Text
	 * @param Name
	 * @param color
	 */
	void addText(String Text, String Name, Color color){
	    StyleContext sc = StyleContext.getDefaultStyleContext();
	    AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,StyleConstants.Foreground, color);	//Skapar textens attribut
	    setCharacterAttributes(aset, false);
	    setCaretPosition(getDocument().getLength());	//S�ger att den ska skriva sist i dokumentet
	    setEditable(true);
	    replaceSelection(Name + ">");		//Skriver ut anv�ndarens namn i r�tt f�rg
	    aset = sc.addAttribute(SimpleAttributeSet.EMPTY,StyleConstants.Foreground, Color.black);	//�ndrar textf�rgen till svart
	    setCharacterAttributes(aset, false);
	    replaceSelection(Text + "\n");			//Skriver ut det anv�ndaren skrev
	    setEditable(false);
		
	}
	
}
