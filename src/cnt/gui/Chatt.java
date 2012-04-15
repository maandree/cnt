package cnt.gui;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;


public class Chatt extends JFrame implements ActionListener
{
    /**
     * Desired by  {@link Serializable}
     */
    private static final long serialVersionUID = 1L;
    
    
    
    JTextPane ta;
    public chatt() throws IOException
    {
	ta = new JTextPane();
	JTextField  da = new JTextField("Skriv här");
	ta.setEditable(false);
	add(ta);
	add(da);
	this.setLayout(new GridLayout(2,1));
	setVisible(true);
	da.addActionListener(this);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setSize(200,300);
    }
    
    public void actionPerformed(ActionEvent e)
    {
	String temp = ((JTextField) e.getSource()).getText() + "\n"; //Hämtar texten från området där användaren fyllt i
	StyleContext sc = StyleContext.getDefaultStyleContext();
	AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.RED); //Skapar textens attribut, red ska här bytas ut mot användarens färg
	
	ta.setCharacterAttributes(aset, false);
	ta.setCaretPosition(ta.getDocument().getLength()); //Säger att den ska skriva sist i dokumentet
	ta.setEditable(true);
	
	ta.replaceSelection("Magnus>");	//Skriver ut Magnus> i rätt färg
	aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.BLACK); //Ändrar textfärgen till svart
	ta.setCharacterAttributes(aset, false);
	ta.replaceSelection(temp); //Skriver ut det användaren skrev
	
	ta.setEditable(false);
	
	((JTextField) e.getSource()).setText("Skriv här"); //Återställer textområdet
    }
    
    
    public static void main(String[] args) throws IOException
    {
	new chatt();
    }
    
}

