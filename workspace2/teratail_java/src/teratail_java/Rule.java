package teratail_java;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Rule extends JFrame implements ActionListener {
    JPanel panel;
    CardLayout layout;
    private JLabel r1l = new JLabel("");
    private JLabel r2l = new JLabel("");
    private JLabel r3l = new JLabel("");
    private JLabel r4l = new JLabel("");

    public Rule() {
        this.setSize(300, 700);
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(355, 100, 300, 580);
        this.setTitle("BJ's Rule");

        JPanel rcard1 = new JPanel();
        rcard1.setBackground(new Color(215, 245, 215));
        rcard1.setLayout(null);
        this.r1l.setIcon(new ImageIcon("rule1.png"));
        this.r1l.setBounds(0, 0, 300, 500);
        rcard1.add(this.r1l);

        JPanel rcard2 = new JPanel();
        rcard2.setBackground(new Color(215, 245, 215));
        rcard2.setLayout(null);
        this.r2l.setIcon(new ImageIcon("rule2.png"));
        this.r2l.setBounds(0, 0, 300, 500);
        rcard2.add(this.r2l);

        JPanel rcard3 = new JPanel();
        rcard3.setBackground(new Color(215, 245, 215));
        rcard3.setLayout(null);
        this.r3l.setIcon(new ImageIcon("rule3.png"));
        this.r3l.setBounds(0, 0, 300, 500);
        rcard3.add(this.r3l);

        JPanel rcard4 = new JPanel();
        rcard4.setBackground(new Color(215, 245, 215));
        rcard4.setLayout(null);
        this.r4l.setIcon(new ImageIcon("rule4.png"));
        this.r4l.setBounds(0, 0, 300, 500);
        rcard4.add(this.r4l);

        /* CardLayout準備 */
        this.panel = new JPanel();
        this.layout = new CardLayout();//CardLayoutの作成
        this.panel.setLayout(this.layout);
        //panelにViewを追加
        this.panel.add(rcard1, "r1View");
        this.panel.add(rcard2, "r2View");
        this.panel.add(rcard3, "r3View");
        this.panel.add(rcard4, "r4View");

        /* カード移動用ボタン */
        JButton rb1 = new JButton("1");
        rb1.addActionListener(this);
        rb1.setActionCommand("r1View");

        JButton rb2 = new JButton("2");
        rb2.addActionListener(this);
        rb2.setActionCommand("r2View");

        JButton rb3 = new JButton("3");
        rb3.addActionListener(this);
        rb3.setActionCommand("r3View");

        JButton rb4 = new JButton("4");
        rb4.addActionListener(this);
        rb4.setActionCommand("r4View");

        JPanel btnPanel = new JPanel();
        btnPanel.add(rb1);
        btnPanel.add(rb2);
        btnPanel.add(rb3);
        btnPanel.add(rb4);

        getContentPane().add(this.panel, BorderLayout.CENTER);
        getContentPane().add(btnPanel, BorderLayout.PAGE_END);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        layout.show(this.panel, cmd);
    }

}