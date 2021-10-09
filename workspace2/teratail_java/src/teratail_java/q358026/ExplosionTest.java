package teratail_java.q358026;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ExplosionTest extends JFrame {
  public static void main(String[] args) throws IOException {
    new ExplosionTest().setVisible(true);
  }

  ExplosionTest() throws IOException {
    super("ExplosionTest");
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    add(new MainPanel());

    pack();
    setResizable(false);
    setLocationRelativeTo(null);
  }

  private static class MainPanel extends JPanel implements FocusListener, Runnable {
    private static final String EXPLOSION_FILENANE = "Q358026_explosion.png";  //爆発画像
    private ImageManager imageManager;
    private KeyManager keyManager;
    private Explosion explosion = null;

    MainPanel() throws IOException {
      super(null);
      setPreferredSize(new Dimension(100, 100));
      setBackground(Color.CYAN);

      setFocusable(true);
      addFocusListener(this);

      imageManager = new ImageManager();
      imageManager.readImage(EXPLOSION_FILENANE);

      keyManager = new KeyManager();
      addKeyListener(keyManager);
    }

    //各キーの押下状態を管理.
    private class KeyManager implements KeyListener {
      private int keyBits = 0;

      boolean pressedEscape() { return (keyBits & 32) != 0; } //ESC が押されていたら true

      @Override
      public void keyTyped(KeyEvent e) { } //ignore
      @Override
      public void keyPressed(KeyEvent e) {
        //System.out.println("keyPressed: "+e.getKeyCode());
        switch(e.getKeyCode()) {
          case KeyEvent.VK_ESCAPE:  keyBits |= 32; return;
        }
      }
      @Override
      public void keyReleased(KeyEvent e) {
        //System.out.println("keyReleased: "+e.getKeyCode());
        switch(e.getKeyCode()) {
          case KeyEvent.VK_ESCAPE: keyBits &= ~32; return;
        }
      }
    }

    //画像ロード担当
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

    //爆発(背景)
    private class Explosion extends Background {
      static final int N = 4; // N回 draw 毎に絵が変わる
      Explosion(int cx, int cy, int cw, int ch) {
        super(EXPLOSION_FILENANE);
        max = h / w * N;
        //中心位置を合わせる
        this.x = cx + (cw - w) / 2;
        this.y = cy + (ch - w) / 2;
      }
      public boolean draw(Graphics g) {
        int dy = count / N * w;
        g.drawImage(image, x, y, x+w, y+w, 0, dy, w, dy+w, null);
        return (++count < max);
      }
    }

    @Override
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      if(explosion != null && !explosion.draw(g)) explosion = null;
    }

    //メインループ
    @Override
    public void run() {
      if(explosion == null && keyManager.pressedEscape()) { //ESC が押されたら爆発
        explosion = new Explosion(50, 50, 20, 20);
      }
      repaint(); //再描画依頼
    }

    private boolean stop = false;
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> future;
    @Override
    public void focusGained(FocusEvent e) {
      if(!stop) future = scheduler.scheduleAtFixedRate(this, 0, 20, TimeUnit.MILLISECONDS); //20ms毎に run()を呼ぶ
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