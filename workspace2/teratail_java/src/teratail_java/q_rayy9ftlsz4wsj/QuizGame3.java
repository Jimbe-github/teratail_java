package teratail_java.q_rayy9ftlsz4wsj;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

import javax.swing.*;

public class QuizGame3 extends JFrame {
  Image img = getToolkit().getImage("C:\\Users\\Jimbe\\Desktop\\艦これ画像\\艦これ 201511秋イベントクリア.png");

  public static void main(String[] args){
    new QuizGame3("NOBUクイズ", 500, 500);
  }

  //public void paint(Graphics g) { //画像描画メソッド
  //  g.drawImage(img, 10, 30,500,500, this);
  //  super.paint(g);
  //}

  static private class Quiz {
    final String question; //問題文
    final String[] answers; //選択肢
    final String correct; //正解
    Quiz(String question, String[] answers) {
      this(question, answers, 0);
    }
    Quiz(String question, String[] answers, int correctNo) {
      this.question = question;
      this.answers = shuffle(answers);
      this.correct = answers[correctNo];
    }
    private static String[] shuffle(String[] array) {
      List<String> a = Arrays.asList(array);
      Collections.shuffle(a);
      return a.toArray(new String[array.length]);
    }
  }

  static private Quiz[] quizArray = {
      new Quiz("ドラえもんに登場するスネ夫の苗字は？",
          new String[]{"骨川", "馬川"}),
      new Quiz("ドラえもんの好物はどら焼き、では妹のドラミちゃんの好物は？",
          new String[]{"メロンパン", "チョコレートパン"}),
      new Quiz("硬式テニスで０点の事を２文字で何と言うでしょう？",
          new String[]{"ラブ", "レイブ"}),
      new Quiz("パソコンのコンはコンピュータの略。ではパソの略は？",
          new String[]{"パーソナル", "ターミナル"}),
      new Quiz("ばいきんまんが愛用しているロボットの名前は？",
          new String[]{"だだんだん", "どどんどん"}),
  };

  private Deque<Quiz> quizStack = new ArrayDeque<Quiz>();
  private Quiz quiz; //出題中のクイズ

  static private final int ANSWERS_SIZE = 2;
  private JLabel questionLabel;
  private JButton[] answerButtons = new JButton[ANSWERS_SIZE];
  private SouthPanel southPanel;

  public QuizGame3(String title, int width, int height) {
    super(title);
    //setVisible(true); //setVisible は最後に行うこと
    setSize(width, height);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);

    JPanel background = new JPanel(new BorderLayout()) {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img,  0,  0,500,500,this);
      }
    };
    add(background);

    JPanel headerPanel = new JPanel();
    headerPanel.setBackground(Color.BLACK);
    headerPanel.setPreferredSize(new Dimension(500,50));
    background.add(headerPanel, BorderLayout.NORTH);

    questionLabel = new JLabel("");
    questionLabel.setHorizontalAlignment(JLabel.CENTER);
    background.add(questionLabel, BorderLayout.CENTER);//中心に問題を表示

    JPanel answerPanel = new JPanel();
    for(int i=0; i<answerButtons.length; i++) {
      answerButtons[i] = new JButton();
      answerButtons[i].addActionListener(e -> {
        decide(((JButton)e.getSource()).getText());
      });
      answerPanel.add(answerButtons[i]);
    }

    southPanel = new SouthPanel(answerPanel, e->drawQuiz(), e->end());
    background.add(southPanel, BorderLayout.SOUTH);

    //クイズをシャッフルして山にする
    List<Quiz> quizList = Arrays.asList(quizArray);
    Collections.shuffle(quizList);
    quizStack.addAll(quizList);

    drawQuiz();

    setVisible(true);
  }

  //下部の表示を進行状況に応じて切り替える
  static private class SouthPanel extends JPanel {
    enum Card {
      ANSWER, //回答
      NEXT,   //次の問題へ
      END     //全問終了
    };

    private CardLayout southCard;

    SouthPanel(JComponent answer, ActionListener nextActionListener, ActionListener endActionListener) {
      super(null);
      southCard = new CardLayout();
      setLayout(southCard);

      add(answer, Card.ANSWER.name());

      JButton nextButton = new JButton("次へ");
      nextButton.addActionListener(nextActionListener);
      add(nextButton, Card.NEXT.name());

      JButton endButton = new JButton("終了");
      endButton.addActionListener(endActionListener);
      add(endButton, Card.END.name());
    }

    void show(Card card) {
      southCard.show(this, card.name());
    }
  }

  private void end() {
    dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
  }

  //山からクイズを引き, 表示する.
  private void drawQuiz() {
    quiz = quizStack.pollFirst();

    southPanel.show(SouthPanel.Card.ANSWER);

    questionLabel.setText(quiz.question);
    questionLabel.setForeground(Color.red);
    for(int i=0; i<answerButtons.length; i++) {
      answerButtons[i].setText(quiz.answers[i]);
    }
  }

  //判定
  private void decide(String answer) {
    if(answer.equals(quiz.correct)){
      questionLabel.setText("正解");
      questionLabel.setForeground(Color.PINK);
    }else{
      questionLabel.setText("不正解");
      questionLabel.setForeground(Color.DARK_GRAY);
    }

    southPanel.show(quizStack.isEmpty() ? SouthPanel.Card.END : SouthPanel.Card.NEXT);
  }
}