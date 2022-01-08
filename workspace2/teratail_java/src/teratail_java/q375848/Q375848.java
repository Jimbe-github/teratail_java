package teratail_java.q375848;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Q375848 {

}

class GamePanel extends JPanel {
  private Road road = new Road();
  private MyCar myCar = new MyCar();
  private CarController cont = new CarController();

  public GamePanel() {
    addKeyListener(cont);
    setFocusable(true); //キー入力を受け付けるため
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    road.draw(g); // 道路を描画
    myCar.draw(g); // 車を描画
  }
}

class Road {
  private ArrayList<Shape> shapes = new ArrayList<>();
  private Color color = new Color(100, 100, 100, 50);

  public Road() {
    shapes.add(new Rectangle(10, 10, 250, 30));
    shapes.add(new Rectangle(230, 10, 30, 250));
    shapes.add(new Rectangle(230, 230, 250, 30));
    shapes.add(new Rectangle(450, 230, 30, 250));
    shapes.add(new Rectangle(10, 450, 470, 30));
    shapes.add(new Rectangle(10, 10, 30, 470));
  }

  void draw(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;

    g2.setColor(color);
    for(Shape shape : shapes) g2.fill(shape);
  }

  boolean inside(int x, int y) {
    for(Shape shape : shapes) {
      if(shape.contains(x, y)) return true;
    }
    return false;
  }
}

class MyCar {
  private int x = 25; // 車の中心のX座標
  private int y = 25; // 車の中心のY座標
  private int angle = 0; // 車の角度
  private int speed = 0;
  private Image img = Toolkit.getDefaultToolkit().getImage("ダッシュキノコ.jpeg");
  private int w, h;

  MyCar() {
    w = img.getWidth(null);
    h = img.getHeight(null);
  }

  int getX() { return x; }
  int getY() { return y; }

  void forward() {
    x += speed * Math.cos(Math.toRadians(angle));
    y += speed * Math.sin(Math.toRadians(angle));
  }

  void right() {
    if((angle += 10) > 180) angle -= 360;
  }

  void left() {
    if((angle -= 10)<= -180) angle += 360;
  }

  void accel(boolean b) {
    speed = b ? 10 : 5;
  }

  void draw(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;

    g2.drawImage(img, x-w/2, y-h/2, null);
  }
}

// コントローラ（キー入力）
class CarController implements KeyListener {
  private boolean keyF, keyRight, keyLeft, keyA;

  CarController() {}

  @Override
  public void keyPressed(KeyEvent e) {
    switch(e.getKeyCode()) {
    case KeyEvent.VK_F: keyF = true;
    case KeyEvent.VK_LEFT: keyLeft = true;
    case KeyEvent.VK_RIGHT: keyRight = true;
    case KeyEvent.VK_A: keyA = true;
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {} //ignore

  @Override
  public void keyReleased(KeyEvent e) {
    switch(e.getKeyCode()) {
    case KeyEvent.VK_F: keyF = false;
    case KeyEvent.VK_LEFT: keyLeft = false;
    case KeyEvent.VK_RIGHT: keyRight = false;
    case KeyEvent.VK_A: keyA = false;
    }
  }

  void tick(MyCar car) {
    if(keyF) car.forward();
    if(keyLeft) car.left();
    if(keyRight) car.right();
    car.accel(keyA);
  }
}