import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;


public class chatt extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTextPane ta;
	chatt() throws IOException{
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
	
	public void actionPerformed(ActionEvent e) {
		String temp = ((JTextField) e.getSource()).getText() + "\n";			//Hämtar texten från området där användaren fyllt i
	    StyleContext sc = StyleContext.getDefaultStyleContext();
	    AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,StyleConstants.Foreground, Color.red);	//Skapar textens attribut, red ska här bytas ut mot användarens färg
	    ta.setCharacterAttributes(aset, false);
	    ta.setCaretPosition(ta.getDocument().getLength());	//Säger att den ska skriva sist i dokumentet
	    ta.setEditable(true);
	    ta.replaceSelection("Magnus>");		//Skriver ut Magnus> i rätt färg
	    aset = sc.addAttribute(SimpleAttributeSet.EMPTY,StyleConstants.Foreground, Color.black);	//Ändrar textfärgen till svart
	    ta.setCharacterAttributes(aset, false);
	    ta.replaceSelection(temp);			//Skriver ut det användaren skrev
	    ta.setEditable(false);
		((JTextField) e.getSource()).setText("Skriv här");		//Återställer textområdet
	}
	public static void main(String[] args) throws IOException{
	    new chatt();
	}
}
