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
	scrollText ta;
	chatt() throws IOException{
		ta = new scrollText();
		JTextField  da = new JTextField("Skriv här");
	    add(ta);
	    add(da);
	    this.setLayout(new GridLayout(2,1));
		setVisible(true);
		da.addActionListener(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
	}
	
	public void actionPerformed(ActionEvent e) {
		String temp = ((JTextField) e.getSource()).getText();			//Hämtar texten från området där användaren fyllt i
		ta.addText(temp, "Magnus", Color.red);
		((JTextField) e.getSource()).setText("Skriv här");		//Återställer textområdet
	}
	public static void main(String[] args) throws IOException{
	    new chatt();
	}
}
