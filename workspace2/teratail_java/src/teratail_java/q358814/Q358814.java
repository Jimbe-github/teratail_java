package teratail_java.q358814;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Q358814 extends JFrame {
  public static void main(String[] args) {
    new Q358814().setVisible(true);
  }

  public Q358814() {
    super("画面遷移テスト");
    setSize(450, 300);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    //コンテンツ
    QuestionPanel p1 = new QuestionPanel();
    AnswerPanel p2 = new AnswerPanel();
    ResultPanel p3 = new ResultPanel();

    ContentsPanel cardPanel = new ContentsPanel(p1, p2, p3);

    Container contentPane = getContentPane();
    contentPane.add(cardPanel, BorderLayout.CENTER);
    contentPane.add(cardPanel.getNavigationPanel(), BorderLayout.SOUTH);

    p2.setCallback(new AnswerPanel.Callback() {
      @Override
      public void countOver() {
        cardPanel.getNavigationPanel().setToResultButtonEnabled(true);
      }
    });
  }
}

//一枚目(panel01)
class QuestionPanel extends JPanel {
  QuestionPanel() {
    super();
    JLabel questionLabel = new JLabel("問題？");
    add(questionLabel);
  }
}

//二枚目(panel02)
class AnswerPanel extends JPanel {
  interface Callback {
    void countOver(); //"ふぁいなるあんさー"ボタンを10回押すと呼ばれる.
  }
  private Callback callback = null;
  private int i = 0;
  AnswerPanel() {
    super();

    JTextField countText = new JTextField(10); //i確認用
    countText.setText(""+i);
    add(countText);

    JButton finalAnswer = new JButton("ふぁいなるあんさー？"); //ｉを増やすイベントを持ったボタン
    finalAnswer.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        //Code.XXX(); //音を鳴らす
        i++;
        countText.setText(""+i);
        if(i >= 10 && callback != null) {
          callback.countOver();
        }
      }
    });
    add(finalAnswer);
  }
  void setCallback(Callback callback) {
    this.callback = callback;
  }
}

//三枚目(panel03)
class ResultPanel extends JPanel {
  ResultPanel() {
    super();
    JLabel resultLabel = new JLabel("結果？");
    add(resultLabel);
  }
}

//メインパネル
class ContentsPanel extends JPanel {
  private enum Card {
    PANEL01, PANEL02,PANEL03;
  }

  private CardLayout cardLayout = new CardLayout();
  private NavigationPanel navigationPanel = null;

  ContentsPanel(JPanel question, JPanel answer, JPanel result) {
    super(null);
    setLayout(cardLayout);

    add(question, Card.PANEL01.name());
    add(answer, Card.PANEL02.name());
    add(result, Card.PANEL03.name());
  }

  private void showCard(Card card) {
    cardLayout.show(this, card.name());
  }

  NavigationPanel getNavigationPanel() {
    if(navigationPanel == null) navigationPanel = new NavigationPanel();
    return navigationPanel;
  }

  //ナビゲーション
  class NavigationPanel extends JPanel {
    private JButton toResultButton;
    private NavigationPanel() {
      super();

      JButton toAnswerButton  = new JButton("回答に進む"); //btn_1to2
      toAnswerButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          toAnswerButton.setEnabled(false);
          showCard(Card.PANEL02);
        }
      });

      toResultButton = new JButton("結果に進む"); //btn_2to3
      toResultButton.setEnabled(false);
      toResultButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          toResultButton.setEnabled(false);
          showCard(Card.PANEL03);
        }
      });

      add(toAnswerButton);
      add(toResultButton);
    }
    void setToResultButtonEnabled(boolean b) {
      toResultButton.setEnabled(b);
    }
  }
}
