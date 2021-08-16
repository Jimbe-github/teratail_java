package teratail_java.q354099;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

/**
 * パネルクラス
 */
public class NEPanel extends JPanel {
  static final int CARDS_MAX = 4;

  private List<Trump> cardList = new ArrayList<Trump>(CARDS_MAX);
  private TrumpLabel labels[] = new TrumpLabel[CARDS_MAX];

  private MouseListener mouselistener = new NEMouseAdapter();

  class TrumpLabel extends JLabel {
    private Trump trump;

    void setTrump(Trump trump) {
      this.trump = trump;
      setIcon(trump.getIcon());
    }
    void open() {
      trump.open();
      setIcon(trump.getIcon());
    }
    void close() {
      trump.close();
      setIcon(trump.getIcon());
    }
    int getNumber() { return trump.getNumber(); }
    boolean isClose() { return trump.getState() == Trump.State.CLOSE; }
  }

  /**
   * コンストラクタ
   */
  public NEPanel() {

    // スーパークラスを呼び出す
    super(new GridLayout(1, 4)); // レイアウト設定

    //カード表示ラベル
    for(int i=0; i<labels.length; i++) {
      labels[i] = new TrumpLabel();
      add(labels[i]);
    }
    setMouseListener();

    //カード準備
    for(int i=0; i<CARDS_MAX; i++) {
      cardList.add(new Trump(Trump.Suit.Spade, i/2+1));
    }

    setCards();
  }

  void setCards() {
    //シャッフル
    Collections.shuffle(cardList);

    //ラベルにセット
    for (int i=0; i<CARDS_MAX; i++) {
      cardList.get(i).close(); //裏
      labels[i].setTrump(cardList.get(i));
      labels[i].setVisible(true);
    }
  }

  private boolean allOpen() {
    boolean allOpen = true;
    for(int i=0; i<labels.length && allOpen; i++) {
      if(labels[i].isClose()) allOpen = false;
    }
    return allOpen;
  }
  private void setMouseListener() {
    for (int i=0; i<labels.length; i++) {
      labels[i].addMouseListener(mouselistener);
    }
  }
  private void removeMouseListener() {
    for (int i=0; i<labels.length; i++) {
      labels[i].removeMouseListener(mouselistener);
    }
  }

  /**
   * マウスアダプタ
   */
  private class NEMouseAdapter extends MouseAdapter {
    private TrumpLabel labelOne; //表にした1枚目
    /**
     * マウスクリックされた時に呼ばれます
     */
    public void mouseClicked(MouseEvent me) {
      TrumpLabel labelTwo = (TrumpLabel)me.getComponent();

      if (labelOne == labelTwo) { //1枚目と同じ？
        return;
      }

      labelTwo.open();
      repaint();

      if(labelOne == null) { //本当に1枚目？
        labelOne = labelTwo;
        return;
      }

      removeMouseListener();
      if (labelTwo.getNumber() == labelOne.getNumber()) { //2枚目と1枚目が同じカード
        if (allOpen()) { //ゲームクリア？
          new RestartWorker().execute();
        } else {
          new InvisibleWorker(labelOne, labelTwo).execute();
        }
      } else { //同じでは無かった
        new RedoWorker(labelOne, labelTwo).execute();
      }
      labelOne = null;
    }
  }

  abstract class DelayWorker extends SwingWorker<Object,Object> {
    private long delay;
    DelayWorker(long delay) {
      this.delay = delay;
    }
    @Override
    public Object doInBackground() {
      try {
        Thread.sleep(delay);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return null;
    }
  }

  //1秒後に One,Two それぞれを非表示状態にし再表示する
  class InvisibleWorker extends DelayWorker {
    private JLabel one, two;
    InvisibleWorker(JLabel one, JLabel two) {
      super(1000);
      this.one = one;
      this.two = two;
    }
    @Override
    protected void done() {
      one.setVisible(false);
      two.setVisible(false);
      repaint();
      setMouseListener();
    }
  }
  //1秒後にリスタート状態にする
  class RestartWorker extends DelayWorker {
    RestartWorker() {
      super(1000);
    }
    @Override
    protected void done() {
      setCards();
      repaint();
      setMouseListener();
    }
  }

  //1秒後に One,Two それぞれを close 状態にし再表示する
  class RedoWorker extends DelayWorker {
    private TrumpLabel one, two;
    RedoWorker(TrumpLabel one, TrumpLabel two) {
      super(1000);
      this.one = one;
      this.two = two;
    }
    @Override
    protected void done() {
      one.close();
      two.close();
      repaint();
      setMouseListener();
    }
  }
}