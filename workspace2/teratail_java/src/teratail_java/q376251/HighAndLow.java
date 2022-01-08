package teratail_java.q376251;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayDeque;
import java.util.Deque;

import javax.swing.*;

class Stock {
  private Deque<Card> cardDeque = new ArrayDeque<>();
  Stock() {
    Card.setSuitIcon(0, "./src/game/highandlow/img/spade.jpg");
    Card.setSuitIcon(1, "./src/game/highandlow/img/heart.jpg");
    Card.setSuitIcon(2, "./src/game/highandlow/img/diamond.jpg");
    Card.setSuitIcon(3, "./src/game/highandlow/img/clover.jpg");

    for(int suit=0; suit<4; suit++) {
      for(int rank=0; rank<13; rank++) {
        cardDeque.add(new Card(suit, rank));
      }
    }
  }
}

class Card {
  private static ImageIcon[] SUIT_ICONS = new ImageIcon[4];
  static void setSuitIcon(int suit, String filename) {
    SUIT_ICONS[suit] = new ImageIcon(filename);
  }
  private static String[] RANKSTR = new String[] {
      "A","2","3","4","5","6","7","8","9","10","J","Q","K"
  };

  private int suit;
  private int rank;
  Card(int suit, int rank) {
    this.suit = suit;
    this.rank = rank;
  }
  int getSuit() { return suit; }
  int getRank() { return rank; }

  ImageIcon getSuitIcon() { return SUIT_ICONS[getSuit()]; }
  String getRankStr() { return RANKSTR[getRank()-1]; }
}

public class HighAndLow extends JFrame {
  public static void main(String args[]) {
    new HighAndLow().setVisible(true);
  }

  private MessagePanel messagePanel;
  private TablePanel tablePanel;
  private ButtonPanel buttonPanel;

  private Card parent, child;

  public HighAndLow() {
    super("HIGH & LOW");
    setSize(510, 480);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //setResizable(false);

    messagePanel = new MessagePanel();
    add(messagePanel, BorderLayout.NORTH);

    tablePanel = new TablePanel();
    add(tablePanel, BorderLayout.CENTER);

    buttonPanel = new ButtonPanel(e->result(true), e->result(false), e->init());
    add(buttonPanel, BorderLayout.SOUTH);

    init();
  }

  void init() {
    this.parent = new Card(0,1);
    this.child = new Card(0,1);

    messagePanel.init();
    tablePanel.init(parent);
    buttonPanel.init();
  }

  enum Judgement {
    Draw("奇遇ですね。引き分けです！"),
    Win("大正解、あなたの勝ちです！"),
    Lose("不正解、あなたの負けです！");

    final String message;
    Judgement(String message) {
      this.message = message;
    }
    String getMessage() { return message; }
  }

  private Judgement judge(boolean isHigh) {
    if(parent.getRank() == child.getRank()) return Judgement.Draw;
    if((parent.getRank() < child.getRank()) == isHigh) return Judgement.Win;
    return Judgement.Lose;
  }

  private void result(boolean isHigh) {
    messagePanel.setMessage(judge(isHigh).getMessage());
    tablePanel.openChild(child);

    buttonPanel.setAgain();
  }
}

class MessagePanel extends JPanel {
  private JLabel messageLabel;
  MessagePanel() {
    super(new BorderLayout());
    setPreferredSize(new Dimension(480, 50));
    setMinimumSize(getPreferredSize());
    setBackground(Color.ORANGE);

    messageLabel = new JLabel();
    messageLabel.setForeground(Color.BLACK);
    messageLabel.setFont(messageLabel.getFont().deriveFont(20f));
    messageLabel.setHorizontalAlignment(JLabel.CENTER);
    messageLabel.setVerticalAlignment(JLabel.CENTER);
    add(messageLabel);

    init();
  }
  void init() {
    setMessage("HIGHかLOWか当ててください。");
  }
  void setMessage(String text) {
    messageLabel.setText(text);
  }
}

class PlayerPanel extends JPanel {
  private JLabel cardLabel;

  PlayerPanel(String title) {
    super(new GridBagLayout());
    setOpaque(false);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;

    JLabel titleLabel = new JLabel(title);
    titleLabel.setHorizontalAlignment(JLabel.CENTER);
    titleLabel.setFont(titleLabel.getFont().deriveFont(14f));
    add(titleLabel, gbc);

    cardLabel = new JLabel();
    cardLabel.setPreferredSize(new Dimension(80,100));
    cardLabel.setOpaque(true);
    cardLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    cardLabel.setFont(cardLabel.getFont().deriveFont(20f));
    cardLabel.setHorizontalAlignment(JLabel.CENTER);
    add(cardLabel, gbc);

    drawPlayer(null);
  }

  void drawPlayer(Card player) {
    if(player == null) {
      cardLabel.setBackground(Color.LIGHT_GRAY);
      cardLabel.setIcon(null);
      cardLabel.setText("？");
    } else {
      cardLabel.setBackground(Color.WHITE);
      cardLabel.setIcon(player.getSuitIcon());
      cardLabel.setText(player.getRankStr());
    }
  }
}

class TablePanel extends JPanel {
  private PlayerPanel parentPanel;
  private PlayerPanel childPanel;

  TablePanel() {
    super(new GridLayout(1,2));
    setBackground(Color.CYAN);
    setMinimumSize(getPreferredSize());

    parentPanel = new PlayerPanel("私のカード");
    add(parentPanel);

    childPanel = new PlayerPanel("あなたのカード");
    add(childPanel);
  }

  void init(Card parent) {
    parentPanel.drawPlayer(parent);
    childPanel.drawPlayer(null);
  }

  void openChild(Card child) {
    childPanel.drawPlayer(child);
  }
}

class ButtonPanel extends JPanel {
  private ColoredButton highButton;
  private ColoredButton lowButton;
  private JButton againButton;

  ButtonPanel(ActionListener highListener, ActionListener lowListener, ActionListener againListener) {
    super(new GridLayout(1,3));

    highButton = new ColoredButton("HIGH", highListener);
    add(createBorderPanel(highButton));

    lowButton = new ColoredButton("LOW", lowListener);
    add(createBorderPanel(lowButton));

    againButton = new JButton("AGAIN");
    againButton.setPreferredSize(new Dimension(100, 50));
    againButton.setFont(againButton.getFont().deriveFont(20f));
    againButton.addActionListener(againListener);
    add(createBorderPanel(againButton));

    init();
  }

  private JPanel createBorderPanel(JButton button) {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(1,5,1,5));
    panel.add(button);
    return panel;
  }

  private class ColoredButton extends JButton {
    private String text;
    ColoredButton(String text, ActionListener listener) {
      super(text);
      this.text = text;

      setPreferredSize(new Dimension(100, 50));
      setFont(getFont().deriveFont(20f));

      addActionListener(e -> {
        setSelected();
        listener.actionPerformed(e);
      });
    }
    void setSelected() {
      setText("<html><font color=\"green\">"+text+"</font></html>");
    }
    void resetSelected() {
      setText(text);
    }
  }

  void init() {
    highButton.resetSelected();
    highButton.setEnabled(true);
    lowButton.resetSelected();
    lowButton.setEnabled(true);
    againButton.setEnabled(false);
  }

  void setAgain() {
    highButton.setEnabled(false);
    lowButton.setEnabled(false);
    againButton.setEnabled(true);
  }
}