package teratail_java;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Ctest extends JFrame implements ItemListener {
    String[] combodata = { "Swing", "Java2D", "Java3D", "JavaMail" };
    String[] combodata2 = { "A", "B", "C", "D" };
    JComboBox combo1 = new JComboBox(combodata);
    JComboBox combo2 = new JComboBox(combodata2);
    JLabel label;

    public static void main(String[] args) {
        Ctest frame = new Ctest();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(10, 10, 300, 200);
        frame.setTitle("タイトル");
        frame.setVisible(true);
    }

    Ctest() {
        combo1.addItemListener(this);
        combo2.addItemListener(this);

        JPanel p = new JPanel();
        p.add(combo1);
        p.add(combo2);

        label = new JLabel();
        JPanel labelPanel = new JPanel();
        labelPanel.add(label);

        getContentPane().add(p, BorderLayout.CENTER);
        getContentPane().add(labelPanel, BorderLayout.PAGE_END);
    }

    public void itemStateChanged(ItemEvent e) {
        System.out.println("itemStateChanged: ItemEvent="+e);
        if (e.getItemSelectable() == combo1) {
            String data = "combo1から" + (String) combo1.getSelectedItem() + "が選ばれた";
            label.setText(data);
            System.out.println("combo1から" + (String) combo1.getSelectedItem() + "が選ばれた");
        } else if (e.getItemSelectable() == combo2) {
            String data = "combo2から" + (String) combo2.getSelectedItem() + "が選ばれた";
            label.setText(data);
            System.out.println("combo2から" + (String) combo2.getSelectedItem() + "が選ばれた");
        }
    }
}