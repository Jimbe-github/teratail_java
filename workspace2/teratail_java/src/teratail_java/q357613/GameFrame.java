package teratail_java.q357613;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameFrame extends JFrame {
  public static void main(String[] args) {
    new GameFrame().setVisible(true);
  }
  GameFrame() {
    super("ゲーム画面");
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setResizable(false);
    add(new GamePanel());
    pack();
  }
  private class GamePanel extends JPanel {
    private Ball ball;
    private Rectangle frame;
    private ScheduledExecutorService executor;
    GamePanel() {
      super(new FlowLayout());
      Dimension size = new Dimension(200, 200);
      setPreferredSize(size);
      frame = new Rectangle(size);

      ball = new Ball(90, 100, 0.9f, 0.5f);

      executor = Executors.newSingleThreadScheduledExecutor();

      JButton startButton = new JButton("start");
      add(startButton);
      startButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          startButton.setEnabled(false);
          executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
              ball.move(frame);
              repaint();
            }
          }, 10, 10, TimeUnit.MILLISECONDS);
        }
      });
    }
    @Override
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      ball.draw(g);
    }
  }
  private class Ball {
    private float x, y, dx, dy;
    private int w=20, h=20;
    Ball(int x, int y, float dx, float dy) {
      this.x = x;
      this.y = y;
      this.dx = dx;
      this.dy = dy;
    }
    void move(Rectangle frame) {
      for(boolean xover=false, yover=false; !xover || !yover; ) {
        xover = frame.contains(x+dx, y, w, h);
        if(!xover) dx *= -1;
        yover = frame.contains(x, y+dy, w, h);
        if(!yover) dy *= -1;
      }
      x += dx;
      y += dy;
    }
    void draw(Graphics g) {
      g.setColor(Color.BLUE);
      g.fillOval((int)x, (int)y, w, h);
    }
  }
}
