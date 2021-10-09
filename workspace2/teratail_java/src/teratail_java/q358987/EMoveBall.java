package teratail_java.q358987;

import java.awt.*;
import java.util.Random;

import javax.swing.JFrame;

public class EMoveBall {
  int width, height; //縦横サイズ
  JFrame mainF; //フレーム
  Canvas mainC; //キャンバス

  public static void main(String[] args) {
    EMoveBall frame = new EMoveBall();
  }

  EMoveBall() {
    //サイズ設定して
    width = 500;
    height = 500;

    //キャンバスの大きさと背景色を指定
    mainC = new MainCanvas();
    mainC.setSize(200, 200);
    mainC.setBackground(Color.blue);

    mainF = new JFrame("moving ball");
    mainF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainF.setBackground(Color.green);
    mainF.setSize(width, height);
    mainF.setLayout(new FlowLayout());
    mainF.add(mainC);

    mainF.setVisible(true);

    while (true) {
      long st = System.currentTimeMillis();

      go();

      long en = System.currentTimeMillis();
      try {
        Thread.sleep(1000 / 60 - (en - st));
      } catch (Exception ex) {
        System.out.println("<Error> +" + ((en - st) - 1000 / 60) + "ms");
      }
    }
  }

  void go() {
    mainC.repaint();
  }
}

class MainCanvas extends Canvas {
  int count = 3;
  int[] x = new int[count];
  int[] y = new int[count]; //ボールの位置
  int vx = 1; //移動量
  int vy = 1; //移動量

  MainCanvas() {
    Random r = new Random();
    for (int i = 0; i < count; i++) {
      x[i] = r.nextInt(50) + 50;
      y[i] = r.nextInt(50) + 50;
    }
  }

  public void paint(Graphics g) {

    for (int i = 0; i < count; i++) {
      x[i] += vx;
      y[i] += vy;
    }

    g.setColor(Color.white);
    for (int i = 0; i < count; i++)
      g.fillOval(x[i], y[i], 10, 10);
  }
}