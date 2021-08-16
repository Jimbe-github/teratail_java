package teratail_java.q353832;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;

//メインフレーム
public class ShootingGame extends JFrame {

  public static void main(String[] args) {
    new ShootingGame().setVisible(true);
  }
  ShootingGame() {
    super("ShootingGame");
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    add(new ShootingPanel());

    pack();
    setResizable(false);
    setLocationRelativeTo(null);
  }
}

//キャラ共通
abstract class Character {
  protected int x, y, w, h;
  abstract public void paint(Graphics g);
  public Rectangle getRectangle() { return new Rectangle(x, y, w, h); }
}

//自機
class MyChar extends Character {
  MyChar() {
    x = 100; y = 250; //左下座標
    w = 30; h = 20;
  }
  public void move(int keycode) {
    switch (keycode) {
    case KeyEvent.VK_UP:
      y -= 10;
      break;
    case KeyEvent.VK_DOWN:
      y += 10;
      break;
    case KeyEvent.VK_LEFT:
      x -= 10;
      break;
    case KeyEvent.VK_RIGHT:
      x += 10;
      break;
    }
  }
  public void invert(int keycode) {
    switch (keycode) {
    case KeyEvent.VK_UP:
      y += 10;
      break;
    case KeyEvent.VK_DOWN:
      y -= 10;
      break;
    case KeyEvent.VK_LEFT:
      x += 10;
      break;
    case KeyEvent.VK_RIGHT:
      x -= 10;
      break;
    }
  }
  public void paint(Graphics g) {
    g.setColor(Color.BLUE);
    g.fillPolygon(
        new int[]{x, x, x + 10, x + 10, x + 20, x + 20, x + 30, x + 30},
        new int[]{y, y - 10, y - 10, y - 20, y - 20, y - 10, y - 10, y}, 8);
  }
  public Rectangle getRectangle() { return new Rectangle(x, y-h, w, h); } //左上からの矩形
}

//敵機
class EnemyChar extends Character {
  EnemyChar() {
    x = 100; y = 100; //左上座標
    w = 30; h = 20;
  }
  public void paint(Graphics g) {
    g.setColor(Color.RED);
    g.fillPolygon(
        new int[]{x, x, x + 10, x + 10, x + 20, x + 20, x + 30, x + 30},
        new int[]{y, y + 10, y + 10, y + 20, y + 20, y + 10, y + 10, y}, 8);
  }
}

//メインパネル
class ShootingPanel extends JPanel implements FocusListener, Runnable {

  private Rectangle panelframe = new Rectangle(0, 0, 400, 300);
  private MyChar myc = new MyChar();
  private EnemyChar enc = new EnemyChar();
  private int inkey;

  public ShootingPanel() {
    super(null);
    setPreferredSize(new Dimension(400, 300));
    setBackground(Color.WHITE);

    setFocusable(true);
    addFocusListener(this);

    addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) { } //ignore

      @Override
      public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        switch(code) {
        case KeyEvent.VK_UP:
        case KeyEvent.VK_DOWN:
        case KeyEvent.VK_LEFT:
        case KeyEvent.VK_RIGHT:
          inkey = code;
        }
      }

      @Override
      public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if(inkey == code) inkey = 0;
      }
    });
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    enc.paint(g);
    myc.paint(g);
  }

  @Override
  public void run() {
    int code = inkey;
    myc.move(code); //動かす
    Rectangle myr = myc.getRectangle();
    if(!panelframe.contains(myr) || //画面脱出判定
      enc.getRectangle().intersects(myr)) { //敵衝突判定
      myc.invert(code); //戻す
    }
    repaint(); //再描画依頼
  }

  private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
  private ScheduledFuture<?> future;
  @Override
  public void focusGained(FocusEvent e) {
    future = scheduler.scheduleAtFixedRate(this, 0, 25, TimeUnit.MILLISECONDS);
  }

  @Override
  public void focusLost(FocusEvent e) {
    if(future != null) {
      future.cancel(false);
      future = null;
    }
  }
}