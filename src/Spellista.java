import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Spellista extends JPanel {

  JList list;

  DefaultListModel model;


  public Spellista() {
    setLayout(new BorderLayout());
    model = new DefaultListModel();
    list = new JList(model);
    JScrollPane pane = new JScrollPane(list);
    model.addElement("Peyman");
    model.addElement("Calle");
    model.addElement("Magnus");
    model.addElement("Mattias");


    add(pane, BorderLayout.NORTH);
  }

  public static void main(String s[]) {
    JFrame frame = new JFrame("Spellista");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(new Spellista());
    frame.setSize(200, 300);
    frame.setVisible(true);
  }
}