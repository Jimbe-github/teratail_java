package teratail_java.q370706;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Q370706 extends JFrame {
  public static void main(String[] args) {
    new Q370706().setVisible(true);
  }
  Q370706() {
    super("Q370706");
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    GamePanel panel = new GamePanel();
    add(panel);
    pack();

    panel.start();
  }
}

class GamePanel extends JPanel {
  public static final int WIDTH = 300;
  public static final int HEIGHT = 300;

  private Jiki jiki;
  private Controller controller;
  private List<Teki> tekiList = new ArrayList<Teki>();
  private List<Tama> tamaList = new ArrayList<Tama>();
  private Object lock[] = new Object[0];
  private Random random = new Random();

  GamePanel() {
    super(null);
    setPreferredSize(new Dimension(WIDTH,HEIGHT));
    setMinimumSize(getPreferredSize());
    setFocusable(true);
    setBackground(Color.BLACK);

    jiki = new Jiki(this, (WIDTH-Jiki.WIDTH)/2, HEIGHT-50);

    tekiList.add(new Teki(this, (WIDTH-Teki.WIDTH)/2, 20, random.nextInt(2)*20-10));
    tekiList.add(new Teki(this, (WIDTH-Teki.WIDTH)/2, 50, random.nextInt(2)*20-10));
    tekiList.add(new Teki(this, (WIDTH-Teki.WIDTH)/2, 80, random.nextInt(2)*20-10));

    controller = new Controller();
    addMouseListener(controller);
    addKeyListener(controller);
  }

  void start() {
    new Timer(50, new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        move();
        collision();
        repaint();
      }
    }).start();
  }

  void move() {
    if(jiki != null) {
      int dx = controller.getDeltaX();
      jiki.setDeltaX(dx);
      jiki.move();
      if(intersect(jiki)) { //外に出そうなら戻す
        jiki.setDeltaX(-dx);
        jiki.move();
      }
      if(controller.fire) {
        jiki.fire();
        controller.fire = false;
      }
    }

    for(Teki teki : tekiList) {
      teki.move();
      if(intersect(teki)) { //外に出そうなら反転
        teki.setDeltaX(-teki.getDeltaX());
        teki.move();
      }
      if(random.nextInt(20) == 0) teki.fire(); //テキトウに撃つ
    }

    synchronized (lock) { //paintComponent 中に remove の可能性があるため
      for(Iterator<Tama> ite=tamaList.iterator(); ite.hasNext(); ) {
        Tama tama = ite.next();
        tama.move();
        if(!contains(tama)) ite.remove();
      }
    }
  }

  void collision() {
    synchronized (lock) { //paintComponent 中に remove の可能性があるため
      Tama[] tamas = tamaList.toArray(new Tama[0]);
      for(int i=0; i<tamas.length; i++) {
        if(tamas[i] == null) continue;
        //他の弾と衝突?
        for(int j=i+1; j<tamas.length; j++) {
          if(tamas[j] == null) continue;
          if(tamas[i].collision(tamas[j])) {
            tamaList.remove(tamas[i]);
            tamas[i] = null;
            tamaList.remove(tamas[j]);
            tamas[j] = null;
            break;
          }
        }
        if(tamas[i] == null) continue;
        //敵と衝突
        for(Iterator<Teki> ite=tekiList.iterator(); ite.hasNext();) {
          Teki teki = ite.next();
          if(teki.iff.isEnemy(tamas[i].iff) && teki.collision(tamas[i])) {
            ite.remove();
            tamaList.remove(tamas[i]);
            tamas[i] = null;
            break;
          }
        }
        if(tamas[i] == null) continue;
        //自機と衝突?
        if(jiki != null && jiki.iff.isEnemy(tamas[i].iff) && jiki.collision(tamas[i])) {
          jiki = null;
          tamaList.remove(tamas[i]);
          tamas[i] = null;
        }
      }
    }
  }

  //一部でもパネルの中
  boolean contains(GameObject obj) {
    return 0 <= obj.x+obj.w && obj.x < WIDTH &&
            0 <= obj.y+obj.h && obj.y < HEIGHT;
  }
  //一部(以上)パネルの外
  boolean intersect(GameObject obj) {
    return obj.x < 0 || WIDTH < obj.x+obj.w ||
            obj.y < 0 || HEIGHT < obj.y+obj.h;
  }

  int countTama(IFF iff) {
    int count = 0;
    for(Tama tama : tamaList) if(tama.iff == iff) count++;
    return count;
  }
  void add(Tama tama) {
    tamaList.add(tama);
  }
  void remove(Tama tama) {
    tamaList.remove(tama);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D)g;
    synchronized (lock) {
      if(jiki != null) jiki.draw(g2);
      for(Teki teki : tekiList) teki.draw(g2);
      for(Tama tama : tamaList) tama.draw(g2);
    }
  }

  class Controller extends MouseAdapter implements KeyListener {
    private static final int LEFT_KEY = KeyEvent.VK_A;
    private static final int RIGHT_KEY = KeyEvent.VK_D;
    private static final int DELTA = 5;

    private boolean left, right;
    private boolean fire;

    void setLeft(boolean left) { this.left = left; }
    void setRight(boolean right) { this.right = right; }
    void setFire(boolean fire) { this.fire = fire; }

    int getDeltaX() {
      return left == right ? 0 : right ? DELTA : -DELTA;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      setFire(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {} //ignore
    @Override
    public void keyReleased(KeyEvent e) {
      if(e.getKeyCode() == LEFT_KEY) setLeft(false);
      if(e.getKeyCode() == RIGHT_KEY) setRight(false);
    }
    @Override
    public void keyPressed(KeyEvent e) {
      if(e.getKeyCode() == LEFT_KEY) setLeft(true);
      if(e.getKeyCode() == RIGHT_KEY) setRight(true);
    }
  }

  enum IFF {
    UNKNOWN, A, B;

    boolean isEnemy(IFF other) {
      return this != other && this != UNKNOWN && other != UNKNOWN;
    }
  }

  abstract class GameObject {
    protected int x, y; //左上座標
    protected int w, h; //幅, 高さ
    protected int dx, dy; //移動距離
    protected IFF iff; //敵味方識別
    protected Rectangle rect;

    GameObject(IFF iff, int x, int y, int w, int h) {
      this.iff = iff;
      this.x = x;
      this.y = y;
      this.w = w;
      this.h = h;
      rect = new Rectangle(x,y,w,h);
    }

    void move() {
      x += dx;
      y += dy;
      rect.setLocation(x,y);
    }
    boolean collision(GameObject obj) {
      return iff.isEnemy(obj.iff) ? rect.intersects(obj.rect) : false;
    }

    abstract void draw(Graphics2D g);
  }

  abstract class GameTarget extends GameObject {
    protected GamePanel world;

    GameTarget(GamePanel world, IFF iff, int x, int y, int w, int h) {
      super(iff, x, y, w, h);
      this.world = world;
    }

    void fire() {
      Tama tama = createTama(world.countTama(iff));
      if(tama != null) world.add(tama);
    }

    abstract Tama createTama(int count);
  }

  class Jiki extends GameTarget {
    public static final int WIDTH = 20;
    public static final int HEIGHT = 20;
    private static final int TAMA_MAX = 5;

    private int nPoints = 3;
    private int[] xPoints = new int[nPoints];
    private int[] yPoints = new int[nPoints];

    Jiki(GamePanel world, int x, int y) {
      super(world, IFF.A, x, y, WIDTH, HEIGHT);
      setPolygon();
    }

    void setDeltaX(int dx) {
      this.dx = dx;
    }

    void setPolygon() {
      xPoints[0] = x+10;
      yPoints[0] = y;
      xPoints[1] = x;
      yPoints[1] = y+20;
      xPoints[2] = x+20;
      yPoints[2] = y+20;
    }

    @Override
    Tama createTama(int count) {
      if(count >= TAMA_MAX) return null;
      return new Tama(iff, x+(WIDTH-Tama.WIDTH)/2, y-Tama.HEIGHT, -5);
    }
    @Override
    void draw(Graphics2D g) {
      g.setColor(Color.GREEN);
      setPolygon();
      g.fillPolygon(xPoints, yPoints, nPoints);
    }
  }

  class Teki extends GameTarget {
    public static final int WIDTH = 20;
    public static final int HEIGHT = 20;

    Teki(GamePanel world, int x, int y, int dx) {
      super(world, IFF.B, x, y, WIDTH, HEIGHT);
      this.dx = dx;
    }

    int getDeltaX() {
      return dx;
    }
    void setDeltaX(int dx) {
      this.dx = dx;
    }

    @Override
    Tama createTama(int count) {
      return new Tama(iff, x+(WIDTH-Tama.WIDTH)/2, y+HEIGHT, 5);
    }
    @Override
    void draw(Graphics2D g) {
      g.setColor(Color.RED);
      g.fillRect(x, y, WIDTH, HEIGHT);
    }
  }

  class Tama extends GameObject {
    public static final int WIDTH = 5;
    public static final int HEIGHT = 10;

    Tama(IFF iff, int x, int y, int dy) {
      super(iff, x, y, WIDTH, HEIGHT);
      this.dy = dy;
    }

    @Override
    void draw(Graphics2D g) {
      g.setColor(iff == IFF.A ? Color.YELLOW : Color.MAGENTA);
      g.fillOval(x, y, w, h);
    }
  }
}