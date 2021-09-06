package teratail_java.q357803;

import java.awt.*;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class C2 {
  public static void main(String[] args) {
    GameWindow gw = new GameWindow("同好会で作ったやつ", 550, 600);
    gw.add(new DrawCanvas());//描画領域の追加
    gw.setVisible(true);
  }

  //ウィンドウクラス
  static class GameWindow extends JFrame {
    public GameWindow(String title, int width, int height) {
      super(title);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setSize(width, height);
      setLocationRelativeTo(null);
      setResizable(false);
    }
  }

  //描画する紙を表すクラス
  static class DrawCanvas extends JPanel {
    public void paintComponent(Graphics g) {
      g.setColor(Color.yellow);
      g.fillRect(500, 100, 50, 50);

      g.setColor(new Color(0, 170, 0));
      g.fillRect(50, 100, 450, 450);

      g.setColor(Color.black);
      for(int y=0; y<9; y++) {
        for(int x=0; x<9; x++) {
          g.drawRect(x*50+50, y*50+100, 50, 50); //矩形
        }
      }

      drawNumberAndDotLine(g); //番号・点線

      //スタート、ゴール
      g.drawString("ゴール", 500, 115);
      g.drawString("スタート", 252, 320);
      //イヴェント
      Random random = new Random();
      Set<Point> used = new HashSet<>(); //イベントを置けない(既に置いてある=使用済み)位置の集合
      used.add(new Point(4,4)); //スタート位置

      drawForwardEvent(g, random, used);
      drawRotateEvent(g, random, used, "⤵90°");
      drawForwardEvent(g, random, used);
      drawRotateEvent(g, random, used, " ⤵ 180°");
      drawForwardEvent(g, random, used);
      drawToLeftEvent(g, random, used);
      drawForwardEvent(g, random, used);
      drawForwardEvent(g, random, used);
      drawForwardEvent(g, random, used);
    }

    //番号・点線
    private void drawNumberAndDotLine(Graphics g) {
      Graphics2D g2 = (Graphics2D)g.create();
      g2.setStroke(new BasicStroke(0, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, new float[]{5}, 0));

      Set<Point> used = new HashSet<>();

      Turtle turtle = new Turtle(9/2, 9/2, Turtle.Direction.EAST, new Turtle.Confirm() {
        @Override
        public boolean isSafe(int x, int y) {
          if(used.contains(new Point(x,y))) return false; //既に通った
          return true;
        }
      });

      Point prev = null;
      for(int number=1; number<=9*9; turtle.forward(), number++) {
        Point point = new Point(turtle.getX(), turtle.getY());
        used.add(point);
        g2.drawString(""+number, point.x*50+35+50, point.y*50+45+100); //番号
        if(prev != null) g2.drawLine(prev.x*50+50+25, prev.y*50+100+25, point.x*50+50+25, point.y*50+100+25);
        prev = point;
      }
      g2.drawLine(prev.x*50+50+25, prev.y*50+100+25, prev.x*50+50+50, prev.y*50+100+25); //最後だけ特別(ゴールへの接続)

      g2.dispose();
    }

    private void drawForwardEvent(Graphics g, Random random, Set<Point> used) {
      Point point = new Point(random.nextInt(9), random.nextInt(9));
      if (!used.contains(point)) {
        used.add(point);
        int p = random.nextInt(5) + 2;
        g.drawString("+" + p + "マス", 50 * point.x + 5, 50 * point.y + 15);
      }
    }

    private void drawRotateEvent(Graphics g, Random random, Set<Point> used, String text) {
      Point point = new Point(random.nextInt(8), random.nextInt(9));
      if (!used.contains(point)) {
        used.add(point);
        g.drawString("盤面", 50 * point.x + 5, 50 * point.y + 15);
        g.drawString(text, 50 * point.x + 5, 50 * point.y + 28);
      }
    }

    private void drawToLeftEvent(Graphics g, Random random, Set<Point> used) {
      Point qr = new Point(random.nextInt(9), random.nextInt(9));
      if (!used.contains(qr)) {
        used.add(qr);
        g.drawString("2マス", 50 * qr.x + 5, 50 * qr.y + 15);
        g.drawString("左へ", 50 * qr.x + 5, 50 * qr.y + 25);
      }
    }
  }

  //右好きの亀さん
  private static class Turtle {
    interface Confirm {
      //安全確認. x,y にマイナスや大きな値が入っても例外を出さないよう注意.
      boolean isSafe(int x, int y);
    }
    enum Direction {
      NORTH(0,-1) { Direction right() { return EAST; } },
      SOUTH(0, 1) { Direction right() { return WEST; } },
      EAST( 1,0) { Direction right() { return SOUTH; } },
      WEST(-1,0) { Direction right() { return NORTH; } };

      final int dx, dy;
      Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
      }
      abstract Direction right();
    }
    private int x, y;
    private Direction dir;
    private Confirm confirm;
    Turtle(int x, int y, Direction dir, Confirm confirm) {
      this.x = x;
      this.y = y;
      this.dir = dir;
      this.confirm = confirm;
    }
    int getX() { return x; }
    int getY() { return y; }
    void forward() {
      x += dir.dx;
      y += dir.dy;
      Direction sence = dir.right(); //この辺が右好き
      if(confirm.isSafe(x+sence.dx, y+sence.dy)) dir = sence;
    }
  }
}