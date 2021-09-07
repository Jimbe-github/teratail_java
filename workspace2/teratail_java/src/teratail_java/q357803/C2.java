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

    private static final int SIZE=50, COUNT=9, XOFF=50, YOFF=100;
    //番号・点線
    private void drawNumberAndDotLine(Graphics g) {
      Set<Point> used = new HashSet<>();

      Turtle turtle = new Turtle(COUNT/2, COUNT/2, Turtle.Direction.EAST, new Turtle.Confirm() {
        @Override
        public boolean isSafe(int x, int y) {
          return !used.contains(new Point(x,y)); //まだ通ってない
        }
      });

      Graphics2D g2 = (Graphics2D)g.create();
      g2.setStroke(new BasicStroke(0, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, new float[]{5}, 0)); //点線

      Point prev = null;
      for(int number=1; number<=COUNT*COUNT; turtle.forward(), number++) {
        Point point = new Point(turtle.getX(), turtle.getY());
        used.add(point); //通ったことを登録
        point = center(point);
        g2.drawString(""+number, point.x+10, point.y+20); //番号
        if(prev != null) g2.drawLine(prev.x, prev.y, point.x, point.y); //点線
        prev = point;
      }
      g2.drawLine(prev.x, prev.y, prev.x+SIZE/2, prev.y); //最後だけ特別(ゴールへの接続)

      g2.dispose();
    }
    //マスの中央座標に変換
    private Point center(Point p) { return new Point(p.x*SIZE+SIZE/2+XOFF, p.y*SIZE+SIZE/2+YOFF); }

    private void drawForwardEvent(Graphics g, Random random, Set<Point> used) {
      Point point = new Point(random.nextInt(9), random.nextInt(9));
      if (!used.contains(point)) {
        used.add(point);
        int p = random.nextInt(5) + 2;
        g.drawString("+" + p + "マス", point.x*SIZE + 5 + XOFF, point.y*SIZE + 15 + YOFF);
      }
    }

    private void drawRotateEvent(Graphics g, Random random, Set<Point> used, String text) {
      Point point = new Point(random.nextInt(8), random.nextInt(9));
      if (!used.contains(point)) {
        used.add(point);
        g.drawString("盤面", point.x*SIZE + 5 + XOFF, point.y*SIZE + 15 + YOFF);
        g.drawString(text, point.x*SIZE + 5 + XOFF, point.y*SIZE + 28 + YOFF);
      }
    }

    private void drawToLeftEvent(Graphics g, Random random, Set<Point> used) {
      Point qr = new Point(random.nextInt(7)+2, random.nextInt(9));
      if (!used.contains(qr)) {
        used.add(qr);
        g.drawString("2マス", qr.x*SIZE + 5 + XOFF, qr.y*SIZE + 15 + YOFF);
        g.drawString("左へ", qr.x*SIZE + 5 + XOFF, qr.y*SIZE + 25 + YOFF);
      }
    }
  }
}