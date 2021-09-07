package teratail_java.q358026;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

//メインフレーム
public class ShootingGame extends JFrame {
  public static void main(String[] args) throws IOException {
    new ShootingGame().setVisible(true);
  }
  ShootingGame() throws IOException {
    super("ShootingGame");
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    add(new ShootingPanel());

    pack();
    setResizable(false);
    setLocationRelativeTo(null);
  }

  //メインパネル
  private static class ShootingPanel extends JPanel implements FocusListener, Runnable {

    private class ImageManager {
      private Map<String,BufferedImage> imageMap = new HashMap<>();

      void readImage(String filename) throws IOException {
        BufferedImage image = ImageIO.read(new File(filename));
        tranparent(image);
        imageMap.put(filename, image);
      }
      BufferedImage getImage(String filename) {
        return imageMap.get(filename);
      }
      //左上角の色と同じ色を透明にする
      protected void tranparent(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        int target = image.getRGB(0,0);
        for(int y=0; y<h; y++) {
          for(int x=0; x<w; x++) {
            if(image.getRGB(x,y) == target) image.setRGB(x,y,0);
          }
        }
      }
    }

    //キャラ(当たり判定有り)
    abstract class Character {
      protected int state;
      protected int x, y, w, h;
      protected BufferedImage image;

      Character() throws IOException {}
      Character(String filename) {
        image = imageManager.getImage(filename);
        w = image.getWidth();
        h = image.getHeight();
      }

      abstract public void paint(Graphics g);
      public Rectangle getRectangle() { return new Rectangle(x, y, w, h); }
    }

    //背景(当たり判定無し)
    abstract class Background {
      protected int count, max;
      protected int x, y, w, h;
      protected BufferedImage image;
      Background(String filename) {
        image = imageManager.getImage(filename);
        w = image.getWidth();
        h = image.getHeight();
      }
      //false ならまだ必要
      abstract public boolean draw(Graphics g);
    }

    //爆発
    class Explosion extends Background {
      Explosion(int cx, int cy, int cw, int ch) {
        super("Q358026_explosion.png");
        max = h / w;
        this.x = cx + (cw - w) / 2;
        this.y = cy + (ch - w) / 2;
      }
      public boolean draw(Graphics g) {
        int dy = (count/4) * w;
        g.drawImage(image, x, y, x+w, y+w, 0, dy, w, dy+w, null);
        return (++ count < max*4);
      }
    }

    //各キーの押下状態を管理.
    private class KeyManager implements KeyListener {
      private int keyBits = 0;

      KeyControl getKeyControl() {
        return KeyControl.BINARRAY[keyBits & 0xf];
      }

      @Override
      public void keyTyped(KeyEvent e) { } //ignore
      @Override
      public void keyPressed(KeyEvent e) {
        //System.out.println("keyPressed: "+e.getKeyCode());
        switch(e.getKeyCode()) {
          case KeyEvent.VK_LEFT:  keyBits |= 1; return;
          case KeyEvent.VK_UP:    keyBits |= 2; return;
          case KeyEvent.VK_RIGHT: keyBits |= 4; return;
          case KeyEvent.VK_DOWN:  keyBits |= 8; return;
          case KeyEvent.VK_SPACE:  keyBits |= 16; return;
        }
      }
      @Override
      public void keyReleased(KeyEvent e) {
        //System.out.println("keyReleased: "+e.getKeyCode());
        switch(e.getKeyCode()) {
          case KeyEvent.VK_LEFT:  keyBits &= ~1; return;
          case KeyEvent.VK_UP:    keyBits &= ~2; return;
          case KeyEvent.VK_RIGHT: keyBits &= ~4; return;
          case KeyEvent.VK_DOWN:  keyBits &= ~8; return;
          case KeyEvent.VK_SPACE:  keyBits &= ~16; return;
        }
      }
    }

    private static final int SPEED = 5;

    enum KeyControl {
      NULL(0,0),
      LEFT(-SPEED,0),
      UP_LEFT(-SPEED,-SPEED),
      UP(0,-SPEED),
      UP_RIGHT(SPEED,-SPEED),
      RIGHT(SPEED,0),
      DOWN_RIGHT(SPEED,SPEED),
      DOWN(0,SPEED),
      DOWN_LEFT(-SPEED,SPEED);

      static final KeyControl BINARRAY[] = {
          NULL, LEFT, UP, UP_LEFT, RIGHT, NULL, UP_RIGHT, UP,
          DOWN, DOWN_LEFT, NULL, LEFT, DOWN_RIGHT, DOWN, RIGHT, NULL
      };
      final int dx, dy;
      KeyControl(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
      }
    }

    //自機
    class MyChar extends Character {
      MyChar() {
        super("Q358026_myc.png");
        x = 100;
        y = 250;
      }

      public void move(KeyManager keyManager, Rectangle panelframe) {
        KeyControl kc = keyManager.getKeyControl();
        Rectangle rect = new Rectangle(x+kc.dx, y+kc.dy, w, h);
        if(panelframe.contains(rect)) { //画面内
          x += kc.dx;
          y += kc.dy;
        }
      }
      public void paint(Graphics g) {
        if(state == 0) { //正常
          g.drawImage(image, x, y, null);
        }
      }
    }

    //敵機
    class EnemyChar extends Character {
      EnemyChar() throws IOException {
        super();
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

    private Rectangle panelframe;
    private ImageManager imageManager;
    private MyChar myc;
    private List<Character> cList = new ArrayList<>();
    private List<Background> bList = new ArrayList<>();
    private KeyManager keyManager;

    public ShootingPanel() throws IOException {
      super(null);
      setPreferredSize(new Dimension(400, 300));
      setBackground(Color.BLACK);

      setFocusable(true);
      addFocusListener(this);

      panelframe = new Rectangle(getPreferredSize());

      imageManager = new ImageManager();
      imageManager.readImage("Q358026_myc.png");
      imageManager.readImage("Q358026_explosion.png");

      keyManager = new KeyManager();
      myc = new MyChar();

      addKeyListener(keyManager);

      cList.add(new EnemyChar());
    }

    @Override
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      for(int i=bList.size()-1; i>=0; i--) {
        if(!bList.get(i).draw(g)) bList.remove(i);
      }
      if(myc.state == 0) myc.paint(g);
      for(Character c : cList) c.paint(g);
    }

    @Override
    public void run() {
      if(myc.state == 0) {
        myc.move(keyManager, panelframe); //動かす

        Rectangle myr = myc.getRectangle();
        for(Character c : cList.toArray(new Character[0])) {
          if(c.getRectangle().intersects(myr)) { //敵衝突判定
            myc.state = 1; //ヤラレタ
            bList.add(new Explosion(myc.x, myc.y, myc.w, myc.h)); //爆発
            cList.remove(c);
            bList.add(new Explosion(c.x, c.y, c.w, c.h)); //爆発
            break;
          }
        }
      }
      repaint(); //再描画依頼
    }

    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> future;
    @Override
    public void focusGained(FocusEvent e) {
      future = scheduler.scheduleAtFixedRate(this, 0, 20, TimeUnit.MILLISECONDS);
    }

    @Override
    public void focusLost(FocusEvent e) {
      if(future != null) {
        future.cancel(false);
        future = null;
      }
    }
  }
}