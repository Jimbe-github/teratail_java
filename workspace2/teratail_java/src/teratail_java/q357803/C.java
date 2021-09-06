package teratail_java.q357803;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class C {
  public static void main(String[] args) {
    GameWindow gw = new GameWindow("同好会で作ったやつ", 550, 600);
    gw.add(new DrawCanvas());//描画領域の追加
    gw.setVisible(true);
  }
}

//ウィンドウクラス
class GameWindow extends JFrame {
  public GameWindow(String title, int width, int height) {
    super(title);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(width, height);
    setLocationRelativeTo(null);
    setResizable(false);
  }
}

//描画する紙を表すクラス
class DrawCanvas extends JPanel {
  public void paintComponent(Graphics g) {
    g.setColor(Color.yellow);
    g.fillRect(500, 100, 50, 50);
    g.setColor(new Color(0, 170, 0));
    g.fillRect(50, 100, 450, 450);

    g.setColor(Color.black);
    g.drawRect(250, 300, 50, 50);//矩形
    g.drawRect(300, 300, 50, 50);
    g.drawRect(300, 350, 50, 50);
    g.drawRect(250, 350, 50, 50);
    g.drawRect(200, 350, 50, 50);
    g.drawRect(200, 300, 50, 50);
    g.drawRect(200, 250, 50, 50);
    g.drawRect(250, 250, 50, 50);
    g.drawRect(300, 250, 50, 50);
    g.drawRect(350, 250, 50, 50);
    g.drawRect(350, 300, 50, 50);
    g.drawRect(350, 350, 50, 50);
    g.drawRect(350, 400, 50, 50);
    g.drawRect(300, 400, 50, 50);
    g.drawRect(250, 400, 50, 50);
    g.drawRect(200, 400, 50, 50);
    g.drawRect(150, 400, 50, 50);
    g.drawRect(150, 350, 50, 50);
    g.drawRect(150, 300, 50, 50);
    g.drawRect(150, 250, 50, 50);
    g.drawRect(150, 200, 50, 50);
    g.drawRect(200, 200, 50, 50);
    g.drawRect(250, 200, 50, 50);
    g.drawRect(300, 200, 50, 50);
    g.drawRect(350, 200, 50, 50);
    g.drawRect(400, 200, 50, 50);
    g.drawRect(400, 250, 50, 50);
    g.drawRect(400, 300, 50, 50);
    g.drawRect(400, 350, 50, 50);
    g.drawRect(400, 400, 50, 50);
    g.drawRect(400, 450, 50, 50);
    g.drawRect(350, 450, 50, 50);
    g.drawRect(300, 450, 50, 50);
    g.drawRect(250, 450, 50, 50);
    g.drawRect(200, 450, 50, 50);
    g.drawRect(150, 450, 50, 50);
    g.drawRect(100, 450, 50, 50);
    g.drawRect(100, 400, 50, 50);
    g.drawRect(100, 350, 50, 50);
    g.drawRect(100, 300, 50, 50);
    g.drawRect(100, 250, 50, 50);
    g.drawRect(100, 200, 50, 50);
    g.drawRect(100, 150, 50, 50);
    g.drawRect(150, 150, 50, 50);
    g.drawRect(200, 150, 50, 50);
    g.drawRect(250, 150, 50, 50);
    g.drawRect(300, 150, 50, 50);
    g.drawRect(350, 150, 50, 50);
    g.drawRect(400, 150, 50, 50);
    g.drawRect(450, 150, 50, 50);
    g.drawRect(450, 200, 50, 50);
    g.drawRect(450, 250, 50, 50);
    g.drawRect(450, 300, 50, 50);
    g.drawRect(450, 350, 50, 50);
    g.drawRect(450, 400, 50, 50);
    g.drawRect(450, 450, 50, 50);
    g.drawRect(450, 500, 50, 50);
    g.drawRect(400, 500, 50, 50);
    g.drawRect(350, 500, 50, 50);
    g.drawRect(300, 500, 50, 50);
    g.drawRect(250, 500, 50, 50);
    g.drawRect(200, 500, 50, 50);
    g.drawRect(150, 500, 50, 50);
    g.drawRect(100, 500, 50, 50);
    g.drawRect(50, 500, 50, 50);
    g.drawRect(50, 450, 50, 50);
    g.drawRect(50, 400, 50, 50);
    g.drawRect(50, 350, 50, 50);
    g.drawRect(50, 300, 50, 50);
    g.drawRect(50, 250, 50, 50);
    g.drawRect(50, 200, 50, 50);
    g.drawRect(50, 150, 50, 50);
    g.drawRect(50, 100, 50, 50);
    g.drawRect(100, 100, 50, 50);
    g.drawRect(150, 100, 50, 50);
    g.drawRect(200, 100, 50, 50);
    g.drawRect(250, 100, 50, 50);
    g.drawRect(300, 100, 50, 50);
    g.drawRect(350, 100, 50, 50);
    g.drawRect(400, 100, 50, 50);
    g.drawRect(450, 100, 50, 50);

    //点線1，２
    for (int x = 0; x < 50; x = x + 10) {
      g.drawLine(275 + x, 325, 280 + x, 325);
      g.drawLine(325, 325 + x, 325, 330 + x);
    }
    //点線３、４
    for (int x = 0; x < 100; x = x + 10) {
      g.drawLine(325 - x, 375, 320 - x, 375);

      g.drawLine(225, 375 - x, 225, 370 - x);
    }
    for (int x = 0; x < 150; x = x + 10) {
      g.drawLine(225 + x, 275, 230 + x, 275);
      g.drawLine(375, 275 + x, 375, 280 + x);
    }
    for (int x = 0; x < 200; x = x + 10) {
      g.drawLine(375 - x, 425, 370 - x, 425);
      g.drawLine(175, 425 - x, 175, 420 - x);
    }
    for (int x = 0; x < 250; x = x + 10) {
      g.drawLine(175 + x, 225, 180 + x, 225);
      g.drawLine(425, 225 + x, 425, 230 + x);
    }
    for (int x = 0; x < 300; x = x + 10) {
      g.drawLine(425 - x, 475, 420 - x, 475);
      g.drawLine(125, 475 - x, 125, 470 - x);
    }
    for (int x = 0; x < 350; x = x + 10) {
      g.drawLine(125 + x, 175, 130 + x, 175);
      g.drawLine(475, 175 + x, 475, 180 + x);
    }
    for (int x = 0; x < 400; x = x + 10) {
      g.drawLine(475 - x, 525, 480 - x, 525);
      g.drawLine(75, 525 - x, 75, 520 - x);
    }
    for (int x = 0; x < 430; x = x + 10) {
      g.drawLine(75 + x, 125, 80 + x, 125);
    }
    //スタート、ゴール
    g.drawString("ゴール", 500, 115);
    g.drawString("スタート", 252, 320);
    //イヴェント
    int a = new java.util.Random().nextInt(5) + 2;
    int b = new java.util.Random().nextInt(8) + 1;
    int c = new java.util.Random().nextInt(8) + 2;
    int d = new java.util.Random().nextInt(8) + 1;
    int e = new java.util.Random().nextInt(8) + 2;
    int f = new java.util.Random().nextInt(8) + 1;
    int s = new java.util.Random().nextInt(8) + 2;
    int h = new java.util.Random().nextInt(8) + 1;
    int i = new java.util.Random().nextInt(8) + 2;
    int j = new java.util.Random().nextInt(8) + 1;
    int k = new java.util.Random().nextInt(8) + 2;
    int l = new java.util.Random().nextInt(5) + 2;
    int m = new java.util.Random().nextInt(5) + 2;
    int n = new java.util.Random().nextInt(5) + 2;
    int o = new java.util.Random().nextInt(5) + 2;
    int p = new java.util.Random().nextInt(5) + 2;
    int q = new java.util.Random().nextInt(6) + 4;
    int r = new java.util.Random().nextInt(8) + 2;
    int t = new java.util.Random().nextInt(8) + 1;
    int u = new java.util.Random().nextInt(8) + 2;
    int v = new java.util.Random().nextInt(8) + 1;
    int w = new java.util.Random().nextInt(8) + 2;
    int y = new java.util.Random().nextInt(8) + 1;
    int z = new java.util.Random().nextInt(8) + 2;
    if (b != 5 & c != 5) {
      g.drawString("+" + a + "マス", 50 * b + 5, 50 * c + 15);
    }
    if (d != 5 & e != 5) {
      if (d != b & e != c) {
        g.drawString("盤面", 50 * d + 5, 50 * e + 15);
        g.drawString("⤵90°", 50 * d + 5, 50 * e + 28);
      }
    }
    if (f != 5 & s != 5) {
      if (f != b & s != c) {
        if (f != d & s != e) {
          g.drawString("+" + l + "マス", 50 * f + 5, 50 * s + 15);
        }
      }
    }
    if (h != 5 & i != 5) {
      if (h != b & i != c) {
        if (h != d & i != e) {
          if (h != f & i != s) {
            g.drawString("盤面", 50 * h + 5, 50 * i + 15);
            g.drawString(" ⤵ 180°", 50 * h + 5, 50 * i + 25);
          }
        }
      }
    }
    if (j != 5 & k != 5) {
      if (j != b & k != c) {
        if (j != d & k != e) {
          if (j != f & k != s) {
            if (j != h & k != i) {
              g.drawString("+" + o + "マス", 50 * j + 5, 50 * k + 15);
            }
          }
        }
      }
    }
    if (q != 5 & r != 5) {
      if (q != b & r != c) {
        if (q != d & r != e) {
          if (q != f & r != s) {
            if (q != h & r != i) {
              if (q != j & r != k) {
                g.drawString("2マス", 50 * q + 5, 50 * r + 15);
                g.drawString("左へ", 50 * q + 5, 50 * r + 25);
              }
            }
          }
        }
      }
    }
    if (t != 5 & u != 5) {
      if (t != b & u != c) {
        if (t != d & u != e) {
          if (t != f & u != s) {
            if (t != h & u != i) {
              if (t != j & u != k) {
                if (t != q & u != r) {
                  g.drawString("+" + m + "マス", 50 * q + 5, 50 * r + 15);
                }
              }
            }
          }
        }
      }
    }
    if (v != 5 & w != 5) {
      if (v != b & w != c) {
        if (v != d & w != e) {
          if (v != f & w != s) {
            if (v != h & w != i) {
              if (v != j & w != k) {
                if (v != q & w != r) {
                  if (v != t & w != u) {
                    g.drawString("+" + n + "マス", 50 * q + 5, 50 * r + 15);
                  }
                }
              }
            }
          }
        }
      }
    }
    if (y != 5 & z != 5) {
      if (y != b & z != c) {
        if (y != d & z != e) {
          if (y != f & z != s) {
            if (y != h & z != i) {
              if (y != j & z != k) {
                if (y != q & z != r) {
                  if (y != t & z != u) {
                    if (y != v & z != w) {
                      g.drawString("+" + p + "マス", 50 * q + 5, 50 * r + 15);
                    }
                  }
                }
              }
            }
          }
        }
      }
    }

  }
}