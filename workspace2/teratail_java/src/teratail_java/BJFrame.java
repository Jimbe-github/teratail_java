package teratail_java;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BJFrame extends JFrame {

    private int stack = 0;

    public static void main(String[] args) {
        BJFrame frame = new BJFrame();
        frame.setVisible(true);
    }

    BJFrame() {
        super("BlackJack");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(-8, 0, 1940, 1050);

        getContentPane().add(new MainPanel(), BorderLayout.CENTER);
    }

    private class MainPanel extends JPanel implements ActionListener {
        private CardLayout layout;

        MainPanel() {
            super(null);

            /* CardLayout準備 */
            layout = new CardLayout();//CardLayoutの作成
            setLayout(layout);

            /* Viewを追加 */
            add(new HomePanel(this), HomePanel.PANEL_NAME);

            BuyinPanel buyinPanel = new BuyinPanel(this);
            buyinPanel.setBuyinListener(new BuyinPanel.BuyinListener() {
                @Override
                public void changeBuyin(int stack) {
                    BJFrame.this.stack = stack;
                }
            });
            add(buyinPanel, BuyinPanel.PANEL_NAME);

            add(new GamePanel(this), GamePanel.PANEL_NAME);
        }

        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            if(cmd.equals("Rule")) {
                System.out.println("a");
                new Rule();
            } else {
                layout.show(this, cmd);
            }
        }
    }

    /** 背景画像を設定できるパネル */
    private static class BackImagePanel extends JPanel {
        private Image background;
        public BackImagePanel(LayoutManager layout) {
            super(layout);
        }
        public void setBackground(String filename) throws IOException {
            if(filename != null) {
                background = ImageIO.read(new File(filename));
            }
        }
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if(background != null) {
                g.drawImage(background, 0, 0, null);
            }
        }
    }

    /** ホームパネル */
    private static class HomePanel extends BackImagePanel {
        public static String PANEL_NAME = "HomeView";

        HomePanel(ActionListener listener){
            super(null);
            try {
                setBackground("casino.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
            setPreferredSize(new Dimension(1980, 1050));

            JButton startButton = new JButton("START");//ホーム画面→BuyIn
            startButton.setBackground(Color.white);
            startButton.setActionCommand(BuyinPanel.PANEL_NAME);
            startButton.addActionListener(listener);
            startButton.setBounds(840, 680, 300, 100);
            add(startButton);

            JButton ruleButton = new JButton("RULE");//ホーム画面でルール確認
            ruleButton.setBackground(Color.white);
            ruleButton.setActionCommand("Rule");
            ruleButton.addActionListener(listener);
            ruleButton.setBounds(840, 800, 300, 100);
            add(ruleButton);
        }
    }

    /** 掛け金パネル */
    private static class BuyinPanel extends JPanel {
        public static String PANEL_NAME = "BuyinView";

        public interface BuyinListener {
            void changeBuyin(int stack);
        }
        private BuyinListener buyinListener;

        private static String[] buyin = { "$10", "$50", "$1000", "$5000", "$10000" };
        private int stack = 0;

        BuyinPanel(ActionListener listener) {
            super(null);
            setBackground(Color.BLACK);

            JButton backButton = new JButton("Back");//BuyIn→ホーム
            backButton.setActionCommand(HomePanel.PANEL_NAME);
            backButton.addActionListener(listener);
            backButton.setBounds(840, 900, 300, 100);
            add(backButton);

            JButton homeButton = new JButton("Let's Play!");//BuyIn→ゲーム
            homeButton.setActionCommand(GamePanel.PANEL_NAME);
            homeButton.addActionListener(listener);
            homeButton.setBounds(840, 780, 300, 100);
            add(homeButton);

            JComboBox<String> buyinCombo = new JComboBox<String>(buyin);
            buyinCombo.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String str = ((String)buyinCombo.getSelectedItem()).replace("$", "");
                    int v = Integer.parseInt(str);
                    if(buyinListener != null && v != stack) {
                        stack = v;
                        buyinListener.changeBuyin(stack);
                    }
                }
            });
            buyinCombo.setBounds(840, 550, 300, 100);
            add(buyinCombo);
        }
        void setBuyinListener(BuyinListener l) {
            buyinListener = l;
        }
    }

    /** ゲームパネル */
    private static class GamePanel extends BackImagePanel {
        public static String PANEL_NAME = "GameView";

        GamePanel(ActionListener listener) {
            super(null);
            try {
                setBackground("Game.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
            setPreferredSize(new Dimension(1980, 1050));

            JButton homeButton = new JButton("H");//ゲーム画面→ホーム
            homeButton.setBackground(Color.white);
            homeButton.setActionCommand(HomePanel.PANEL_NAME);
            homeButton.addActionListener(listener);
            homeButton.setBounds(12, 18, 50, 50);
            add(homeButton);

            JButton buyinButton = new JButton("$");//ゲーム画面→BuyIn
            buyinButton.setBackground(Color.white);
            buyinButton.setActionCommand(BuyinPanel.PANEL_NAME);
            buyinButton.addActionListener(listener);
            buyinButton.setBounds(11, 63, 50, 50);
            add(buyinButton);

            JButton ruleButton = new JButton("R");//ゲーム画面でルール確認
            ruleButton.setBackground(Color.white);
            ruleButton.setActionCommand("Rule");
            ruleButton.addActionListener(listener);
            ruleButton.setBounds(11, 113, 50, 50);
            add(ruleButton);
        }
    }
}