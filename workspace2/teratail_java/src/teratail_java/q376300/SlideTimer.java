package teratail_java.q376300;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Hashtable;

import javax.swing.*;

public class SlideTimer extends JFrame {
  public static void main(String[] args) {
    new SlideTimer().setVisible(true);
  }

  public SlideTimer() {
    super("スライダー");
    setBounds(10, 10, 300, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    TimePanel timePanel = new TimePanel();
    add(timePanel, BorderLayout.CENTER);

    JSlider slider = new JSlider(8, 18, 8);
    slider.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    slider.addChangeListener(e -> timePanel.start(slider.getValue(), 0, 0));
    slider.setLabelTable(createLabelTable(8, 18));
    slider.setPaintLabels(true);
    add(slider, BorderLayout.SOUTH);

    timePanel.start(8, 31, 20);
  }

  private Hashtable<Integer,JComponent> createLabelTable(int from, int to) {
    Hashtable<Integer,JComponent> table = new Hashtable<>();
    for(int i=from; i<=to; i++) table.put(i, new JLabel(""+i));
    return table;
  }
}

class TimePanel extends JPanel {
  private static final Font FONT = new Font("ＭＳ Ｐゴシック", Font.PLAIN, 30);

  private final FontMetrics fontMetrics = getFontMetrics(FONT);

  private class TimeCounter {
    private int hour, min, sec;
    TimeCounter(int hour, int min, int sec) {
      this.sec = sec;
      this.min = min;
      this.hour = hour;
      normalize();
    }
    void countUp() {
      sec ++;
      normalize();
    }
    private void normalize() {
      while(sec < 0) { sec+=60; min--; }
      while(sec >= 60) { sec-=60; min++; }
      while(min < 0) { min+=60; hour--; }
      while(min >= 60) { min-=60; hour++; }
      while(hour < 0) { hour+=24; }
      while(hour >= 24) { hour-=24; }
    }
    @Override
    public String toString() {
      return String.format("%02d:%02d:%02d", hour, min, sec);
    }
  }
  private TimeCounter time;
  private Object[] counterLock = new Object[0];
  private Timer timer;

  TimePanel() {
    super(null);
    setBackground(Color.WHITE);
  }

  void start(int hour, int min, int sec) {
    stop();

    synchronized(counterLock) {
      time = new TimeCounter(hour, min ,sec);
    }

    if(timer == null) {
      timer = new Timer(1000, e -> {
        synchronized(counterLock) {
          time.countUp();
          //System.out.println(time);
        }
        repaint();
      });
      timer.start();
    } else {
      timer.restart();
    }
    repaint();
  }

  void stop() {
    if(timer != null && timer.isRunning()) {
      timer.stop();
    }
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    String str;
    synchronized(counterLock) {
      str = time.toString();
    }

    g.setColor(Color.black);
    g.setFont(FONT);
    Rectangle2D rect = fontMetrics.getStringBounds(str, g);
    int x = (getWidth() -(int)rect.getWidth() )/2;
    int y = (getHeight()-(int)rect.getHeight())/2;
    g.drawString(str, x, y+fontMetrics.getMaxAscent()); //ベースライン

    //Graphics2D g2 = (Graphics2D)g;
    //g2.drawRect(x, y, (int)rect.getWidth(), (int)rect.getHeight());
  }
}
