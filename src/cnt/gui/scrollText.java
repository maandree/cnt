import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;



public class scrollText extends JPanel{
	textField textPane;
	JScrollPane scrollPane;
	scrollText(){
		textPane = new textField();
		scrollPane = new JScrollPane(textPane);
		scrollPane.setPreferredSize(new Dimension(250, 155));
		add(scrollPane);


	}
	public void addText(String Text, String Name, Color color){
		textPane.addText(Text, Name, color);
	}

}
