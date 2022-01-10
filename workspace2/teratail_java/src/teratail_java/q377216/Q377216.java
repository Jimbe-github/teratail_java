package teratail_java.q377216;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;

public class Q377216 extends JFrame {
  public static void main(String[] args) {
    new Q377216().setVisible(true);
  }

  Q377216() {
    super("Q377216");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(600, 480);
    setLocationRelativeTo(null);

    getContentPane().setLayout(null);

    Conveyor conveyor1 = new Conveyor(Conveyor.Anchor.SOUTH, Conveyor.FlowDir.LeftToRight);
    conveyor1.setBounds(100, 300, 200, 50);
    add(conveyor1);

    Machine machine = new Machine();
    machine.setBounds(300, 280, 100, 100);
    add(machine);

    Conveyor conveyor2 = new Conveyor(Conveyor.Anchor.EAST, Conveyor.FlowDir.BottomToTop);
    conveyor2.setBounds(320, 80, 50, 200);
    add(conveyor2);

    //コンベア1 から マシン を通って コンベア2 へ繋げる
    conveyor1.setNext(machine).setNext(conveyor2);

    JButton button1 = new JButton("投入");
    button1.addActionListener(new ActionListener() {
      private Random random = new Random();
      private Color[] colors = new Color[]{ Color.PINK, Color.RED, Color.CYAN, Color.WHITE, Color.GREEN, Color.YELLOW, Color.ORANGE };
      @Override
      public void actionPerformed(ActionEvent e) {
        conveyor1.set(new Material(colors[random.nextInt(colors.length)], random.nextInt(3)));
      }
    });
    button1.setBounds(50, 50, 100, 30);
    add(button1);

    conveyor1.start();
    machine.start();
    conveyor2.start();
  }
}

class Material {
  private static final int WIDTH = 20;
  private static final int HEIGHT = 20;
  private int x = Integer.MIN_VALUE;
  private int y = Integer.MIN_VALUE;
  private Color color;
  private int count;
  Material(Color color) {
    this(color, Integer.MIN_VALUE);
  }
  Material(Color color, int count) {
    this.color = color;
    this.count = count;
  }
  void setX(int x) { this.x = x; }
  void setY(int y) { this.y = y; }
  void setColor(Color color) { this.color = color; }
  void setCount(int count) { this.count = count; }

  int getX() { return x; }
  int getY() { return y; }
  int getWidth() { return WIDTH; }
  int getHeight() { return HEIGHT; }
  Color getColor() { return color; }
  int getCount() { return count; }

  Rectangle getRectangle() {
    return new Rectangle(x, y, WIDTH, HEIGHT);
  }

  void paint(Graphics g) {
    g.setColor(color);
    g.fillOval(x, y, WIDTH, HEIGHT);
    g.setColor(Color.BLACK);
    g.drawOval(x, y, WIDTH, HEIGHT);
    if(count != Integer.MIN_VALUE) g.drawString(""+count, x+7, y+15);
  }
}

interface InGate {
  void set(Material m);
}

interface Repeater extends InGate {
  Repeater setNext(Repeater next);
}

class Machine extends JPanel implements Repeater {
  //表示機能付きキュー
  private class Queue {
    private java.util.List<Material> q = new ArrayList<Material>();
    private int gridSize, gridRows, gridColumns;

    Queue(int gridSize, int gridRows, int gridColumns) {
      this.gridSize = gridSize;
      this.gridRows = gridRows;
      this.gridColumns = gridColumns;
    }

    boolean isEmpty() { return q.isEmpty(); }
    void addLast(Material m) { repaint(); q.add(m); }
    Material getFirst() { repaint(); return q.remove(0); }

    int getWidth() { return gridSize * gridColumns; }
    int getHeight() { return gridSize * gridRows; }

    void paint(Graphics g, int ox, int oy) {
      for(int i=0; i<gridRows*gridColumns; i++) {
        int x = ox + gridSize * (i % gridColumns);
        int y = oy + gridSize * (i / gridColumns);
        g.setColor(i < q.size() ? q.get(i).getColor() : Color.LIGHT_GRAY);
        g.fillRect(x, y, gridSize, gridSize);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, gridSize, gridSize);
      }
    }
  }

  private Queue queue = new Queue(10, 3, 5); //10x10 のマスを 3 行 5 列
  private Thread thread;
  private InGate nextGate;
  private Material sample = new Material(Color.LIGHT_GRAY); //表示用サンプル

  Machine() {
    super(null);
    setBackground(Color.WHITE);
  }

  @Override
  public Repeater setNext(Repeater next) {
    this.nextGate = next;
    return next;
  }

  @Override
  public void set(Material m) {
    synchronized(queue) {
      queue.addLast(m);
      queue.notify();
    }
  }

  void start() {
    stop(); //念の為
    thread = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          while(!Thread.currentThread().isInterrupted()) {
            Material m;
            synchronized(queue) {
              while(queue.isEmpty()) queue.wait();
              m = queue.getFirst();
            }
            sample.setColor(m.getColor());
            repaint();
            m = work(m);
            sample.setColor(Color.LIGHT_GRAY);
            repaint();
            if(nextGate != null) nextGate.set(m);
          }
        } catch(InterruptedException ignore) {
        }
      }
    });
    thread.start();
  }
  void stop() {
    if(thread != null && thread.isAlive()) {
      thread.interrupt();
      try {
        thread.join();
      } catch(InterruptedException e) {
        e.printStackTrace();
      }
    }
    thread = null;
  }

  private Material work(Material m) {
    try {
      m.setCount(m.getCount() + 1);
      Thread.sleep(2000); //処理に2秒掛かることにする
    } catch(InterruptedException e) {
      e.printStackTrace();
    }
    return m;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    g.setColor(Color.BLACK);
    g.drawRect(0, 0, getWidth()-1, getHeight()-1);

    int sp = (getHeight() - sample.getHeight() - queue.getHeight()) / 3;

    sample.setX((getWidth() - sample.getWidth()) / 2);
    sample.setY(sp);
    sample.paint(g);

    synchronized(queue) {
      queue.paint(g, (getWidth()-queue.getWidth())/2, sp+sample.getHeight()+sp);
    }
  }
}

class Conveyor extends JPanel implements Repeater {
  private enum Axis { X_AXIS, Y_AXIS } //縦横
  enum FlowDir { //何処から何処へ流れるか
    BottomToTop(Axis.Y_AXIS, 0,-1), LeftToRight(Axis.X_AXIS, 1, 0),
    TopToBottom(Axis.Y_AXIS, 0, 1), RightToLeft(Axis.X_AXIS,-1, 0);
    final Axis axis;
    final int sx, sy;
    FlowDir(Axis axis, int sx, int sy) {
      this.axis = axis;
      this.sx = sx;
      this.sy = sy;
    }
    int moveX(int x, int dx) { return x+(dx*sx); }
    int moveY(int y, int dy) { return y+(dy*sy); }
  }
  enum Anchor { //どの辺に沿って描画するか
    NORTH(Axis.X_AXIS), EAST(Axis.Y_AXIS),
    SOUTH(Axis.X_AXIS), WEST(Axis.Y_AXIS);
    final Axis axis;
    Anchor(Axis axis) {
      this.axis = axis;
    }
  }
  private static final int BOARD_SIZE = 5;
  private static final int BOARD_THICK = 2;
  private static final int TRANSPORT_STEP = 2;

  private Anchor anchor = Anchor.SOUTH;
  private FlowDir flowdir = FlowDir.LeftToRight;
  private ArrayList<Material> mlist = new ArrayList<Material>();
  private Thread thread;
  private int offsetX = 0, offsetY = 0;
  private InGate nextGate;

  Conveyor(Anchor anchor, FlowDir flowdir) {
    super(null);
    if(anchor.axis != flowdir.axis) throw new IllegalArgumentException();
    this.anchor = anchor;
    this.flowdir = flowdir;
    setBackground(Color.WHITE);
  }

  @Override
  public Repeater setNext(Repeater next) {
    this.nextGate = next;
    return next;
  }

  @Override
  public void set(Material m) {
    setPosition(m);
    synchronized(mlist) {
      mlist.add(m);
    }
  }

  private void setPosition(Material m) {
    if(flowdir == FlowDir.LeftToRight) {
      m.setX(- m.getWidth());
    } else if(flowdir == FlowDir.RightToLeft) {
      m.setX(getWidth());
    } else if(anchor == Anchor.WEST) {
      m.setX(BOARD_THICK);
    } else { //anchor == Anchor.EAST
      m.setX(getWidth() - BOARD_THICK - m.getWidth());
    }
    if(flowdir == FlowDir.TopToBottom) {
      m.setY(- m.getHeight());
    } else if(flowdir == FlowDir.BottomToTop) {
      m.setY(getHeight());
    } else if(anchor == Anchor.NORTH) {
      m.setY(BOARD_THICK);
    } else { //anchor == Anchor.SOUTH
      m.setY(getHeight() - BOARD_THICK - m.getHeight());
    }
  }

  private void move() {
    offsetX = flowdir.moveX(offsetX, TRANSPORT_STEP);
    if(offsetX <= -BOARD_SIZE) offsetX += BOARD_SIZE*2;
    else if(offsetX >= BOARD_SIZE) offsetX -= BOARD_SIZE*2;

    offsetY = flowdir.moveY(offsetY, TRANSPORT_STEP);
    if(offsetY <= -BOARD_SIZE) offsetY += BOARD_SIZE*2;
    else if(offsetY >= BOARD_SIZE) offsetY -= BOARD_SIZE*2;

    synchronized(mlist) {
      for(Iterator<Material> ite=mlist.iterator(); ite.hasNext(); ) {
        Material m = ite.next();
        m.setX(flowdir.moveX(m.getX(), TRANSPORT_STEP));
        m.setY(flowdir.moveY(m.getY(), TRANSPORT_STEP));

        if(!getRectangle().intersects(m.getRectangle())) {
          ite.remove();
          if(nextGate != null) { nextGate.set(m);}
        }
      }
    }
  }

  private Rectangle getRectangle() {
    return new Rectangle(0, 0, getWidth(), getHeight());
  }

  void start() {
    stop(); //念の為
    thread = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          repaint();
          while(!Thread.currentThread().isInterrupted()) {
            Thread.sleep(100);
            move();
            repaint();
          }
        } catch(InterruptedException ignore) {
        }
      }
    });
    thread.start();
  }
  void stop() {
    if(thread != null && thread.isAlive()) {
      thread.interrupt();
      try {
        thread.join();
      } catch(InterruptedException e) {
        e.printStackTrace();
      }
    }
    thread = null;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    g.setColor(Color.BLACK);
    if(anchor.axis == Axis.X_AXIS) {
      int y = anchor == Anchor.NORTH ? 0 : getHeight() - BOARD_THICK;
      for(int x=offsetX; x<getWidth(); x+=BOARD_SIZE*2) {
        g.fillRect(x, y, BOARD_SIZE, BOARD_THICK);
      }
    } else {
      int x = anchor == Anchor.WEST ? 0 : getWidth() - BOARD_THICK;
      for(int y=offsetY; y<getHeight(); y+=BOARD_SIZE*2) {
        g.fillRect(x, y, BOARD_THICK, BOARD_SIZE);
      }
    }

    synchronized(mlist) {
      for(Material m : mlist) m.paint(g);
    }
  }
}