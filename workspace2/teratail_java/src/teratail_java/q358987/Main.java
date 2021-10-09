package teratail_java.q358987;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JFrame {
  public static void main(String[] args) {
    new Main().setVisible(true);
  }

  private static final int FPS = 60; //一秒間に60回
  private static final int RANGE = 100;

  private int fw = 500, fh = 500; //フレームサイズ

  Main() {
    super("");
    setSize(600, 600);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    GameFieldPanel mainc = new GameFieldPanel(RANGE, 10); //ボール10個
    mainc.setSize(fw, fh);
    add(mainc, BorderLayout.CENTER);

    mainc.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        if(e.getButton() == MouseEvent.BUTTON1) { //左ボタン
          mainc.reset();
          mainc.start(FPS);
        }
      }
    });
    mainc.addMouseMotionListener(new MouseMotionListener() {
      @Override
      public void mouseMoved(MouseEvent e) { //マウスが動いたときに呼び出される。
        int gx = e.getX();
        int gy = e.getY();
        mainc.setGravity(gx, gy);
        setTitle("X = " + gx + " : Y = " + gy);
      }
      @Override
      public void mouseDragged(MouseEvent e) {} //ignore
    });

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowOpened(WindowEvent e) {
        super.windowOpened(e);
        mainc.reset();
        mainc.start(FPS);
      }
    });
  }
}

//ゲームフィールド
class GameFieldPanel extends JPanel {
  private int fps = 0;
  private int renge; //重力目安
  private int gravityX, gravityY; //重力の位置
  private Object glock = new Object[0]; //重力位置ロック
  private Ball balls[];
  private Timer timer;

  GameFieldPanel(int renge, int count) {
    super(null);
    setBackground(Color.WHITE);

    this.renge = renge;
    balls = new Ball[count];
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    synchronized (balls) {
      for (Ball ball : balls) if(ball != null) ball.paint(g);
    }
  }

  void setGravity(int gx, int gy) {
    synchronized (glock) {
      gravityX = gx;
      gravityY = gy;
    }
  }

  void go() {
    int gx, gy;
    synchronized (glock) {
      gx = gravityX;
      gy = gravityY;
    }
    synchronized (balls) {
      for (Ball ball : balls) ball.go(gx, gy, renge);
    }
    repaint();
  }

  private static int BALL_SIZE = 10; //玉の大きさ
  private static Color BALL_COLORS[] = new Color[] {
      Color.BLACK, Color.BLUE, Color.CYAN, Color.RED, Color.GREEN,
      Color.MAGENTA, Color.YELLOW, Color.ORANGE, Color.PINK, Color.LIGHT_GRAY
  };

  void reset() {
    stop();
    int w = getWidth();
    int h = getHeight();
    Random r = new Random();
    synchronized (balls) {
      for (int i = 0; i < balls.length; i++) {
        balls[i] = new Ball(r.nextInt(w-BALL_SIZE), r.nextInt(h-BALL_SIZE), BALL_SIZE,
            BALL_COLORS[i % BALL_COLORS.length]);
      }
    }
  }

  void start(int fps) {
    if(fps <= 0) throw new IllegalArgumentException("fp sは 1 以上: fps="+fps);
    stop();
    this.fps = fps;
    timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
      public void run() {
        go();
      }
    }, 100, 1000/fps); //[ms]
  }

  void restart() {
    if(fps <= 0) throw new IllegalStateException("start が実行されていない");
    start(fps);
  }

  void stop() {
    if(timer != null) {
      timer.cancel();
      timer = null;
    }
  }
}

//ボール
class Ball {
  private static final int MEMORY_POWER = 50; //勢いの残留回数

  private int x, y, size;
  private Color color;
  private double[] xPower, yPower; //勢いの保存
  private int pi; //power インデックス

  Ball(int x, int y, int size) {
    this(x, y, size, Color.BLACK);
  }
  Ball(int x, int y, int size, Color color) {
    this.y = y;
    this.x = x;
    this.size = size;
    this.color = color;

    pi = 0;
    xPower = new double[MEMORY_POWER];
    yPower = new double[MEMORY_POWER];
  }

  public void paint(Graphics g) {
    g.setColor(color);
    g.fillOval(x, y, size, size);
  }

  void go(int centerX, int centerY, int renge) {
    int heigth = centerY - (y + size / 2);
    int width = centerX - (x + size / 2);
    int power = (int) (Math.pow(heigth, 2) + Math.pow(width, 2));
    double rad = Math.atan2(heigth, width);
    keepPower(Math.cos(rad), Math.sin(rad), renge, power);
    setPower();
  }

  private void keepPower(double xp, double yp, int renge, int power) {
    double times = 3; //初期倍率
    for (int i = renge; i < power; i += 100) {
      times -= 0.005;
      if (times <= 0) {
        times = 0.1;
        break;
      }
    }

    xPower[pi] = times * xp;
    yPower[pi] = times * yp;
    if(++pi >= MEMORY_POWER) pi = 0;
  }

  private void setPower() {
    double interimX = 0, interimY = 0;
    for (int i = 0; i < MEMORY_POWER; i++) {
      interimX += xPower[i];
      interimY += yPower[i];
    }
    x += interimX / MEMORY_POWER;
    y += interimY / MEMORY_POWER;
  }
}