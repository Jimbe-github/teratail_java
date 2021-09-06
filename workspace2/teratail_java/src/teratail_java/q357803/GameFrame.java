package teratail_java.q357803;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameFrame extends JFrame {
  public static void main(String[] args) {
    new GameFrame().setVisible(true);
  }

  public GameFrame() {
    super("同好会で作ったやつ");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(550, 600);
    setLocationRelativeTo(null);

    //ウインドウを大きくしても GamePanel が中央に位置するようにレイアウトを設定
    GridBagLayout layout = new GridBagLayout();
    setLayout(layout);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.anchor = GridBagConstraints.CENTER;
    add(new GamePanel(50, 9), gbc); //描画領域の追加
  }

  //マス
  private static class Square {
    enum Direction {
      CENTER, EAST, WEST, NORTH, SOUTH;
    }
    private int number, x, y;
    private int gx, gy, size;
    private Color background = new Color(0, 170, 0);
    private Event event;
    Square(int number, int x, int y, int size) {
      this.number = number;
      this.x = x;
      this.y = y;
      this.size = size;

      this.gx = x * size;
      this.gy = y * size;
    }
    void setEvent(Event event) { this.event = event; }
    //イベントがあるか
    boolean hasEvent() { return event != null; }
    //マスの各方向の座標
    Point getPoint(Direction dir) {
      switch(dir) {
      case EAST:
        return new Point(gx+size, gy+size/2);
      case WEST:
        return new Point(gx, gy+size/2);
      case NORTH:
        return new Point(gx+size/2, gy);
      case SOUTH:
        return new Point(gx+size/2, gy+size);
      default: //CENTER
        return new Point(gx+size/2, gy+size/2);
      }
    }
    void draw(Graphics g) {
      Graphics g2 = g.create();
      g2.setColor(background);
      g2.fillRect(gx, gy, size, size);
      g2.dispose();

      g.drawRect(gx, gy, size, size); //枠

      drawStringAtBottomRight(g, ""+number); //番号
      if(hasEvent()) event.draw(g, gx+2, gy+2); //イベント
    }
    private void drawStringAtBottomRight(Graphics g, String text) {
      FontMetrics fm = g.getFontMetrics();
      int descent = fm.getDescent(); //ベースラインの高さ
      Rectangle2D rect = fm.getStringBounds(text, g);
      int width = (int)rect.getWidth(); //文字列の幅
      g.drawString(text, gx+size-width-1, gy+size-descent-1); //-1 はちょっと隙間
    }
  }

  //イベント
  private static class Event {
    private String text1, text2;
    Event(String text) {
      this.text1 = text;
    }
    Event(String text1, String text2) {
      this.text1 = text1;
      this.text2 = text2;
    }
    void draw(Graphics g, int gx, int gy) {
      FontMetrics fm = g.getFontMetrics();
      gy += fm.getAscent(); //ベースラインから文字頂までの高さ
      g.drawString(text1, gx, gy);
      if(text2 != null) {
        Rectangle2D rect = fm.getStringBounds(text2, g);
        gy += (int)rect.getHeight();
        g.drawString(text2, gx, gy);
      }
    }
  }

  //点線描画
  private static class DotLineDrawer {
    private int x, y;
    private BasicStroke dashed;
    DotLineDrawer(int solid_period) {
      dashed = new BasicStroke(0, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, new float[]{solid_period}, 0);
    }
    //始点
    void setStart(Point start) {
      x = start.x;
      y = start.y;
    }
    //始点もしくは最後に draw した後から描画
    void draw(Graphics g, Point end) {
      Graphics2D g2 = (Graphics2D)g.create();
      g2.setStroke(dashed);
      g2.drawLine(x, y, end.x, end.y);
      g2.dispose();
      x = end.x;
      y = end.y;
    }
  }

  //右好きの亀さん
  private static class Turtle {
    interface Confirm {
      //安全確認. x,y にマイナスや大きな値が入っても例外を出さないよう注意.
      boolean isSafe(int x, int y);
    }
    enum Direction {
      NORTH(0,-1) { Direction right() { return EAST; } },
      SOUTH(0, 1) { Direction right() { return WEST; } },
      EAST( 1,0) { Direction right() { return SOUTH; } },
      WEST(-1,0) { Direction right() { return NORTH; } };

      final int dx, dy;
      Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
      }
      abstract Direction right();
    }
    private int x, y;
    private Direction dir;
    private Confirm confirm;
    Turtle(int x, int y, Direction dir, Confirm confirm) {
      this.x = x;
      this.y = y;
      this.dir = dir;
      this.confirm = confirm;
    }
    int getX() { return x; }
    int getY() { return y; }
    void forward() {
      x += dir.dx;
      y += dir.dy;
      Direction sence = dir.right(); //この辺が右好き
      if(confirm.isSafe(x+sence.dx, y+sence.dy)) dir = sence;
    }
  }

  //描画する紙を表すクラス
  private static class GamePanel extends JPanel {
    private List<Square> squareList = new ArrayList<>(); //中心を index=0 とし、螺旋を展開した直線状態
    private Random random = new Random();
    private final int square_size;
    private final int square_count;

    GamePanel(int square_size, int square_count) {
      super(null);
      this.square_size = square_size;
      this.square_count = square_count;

      setPreferredSize(new Dimension(square_size*(square_count+1), square_size*square_count));
      setMinimumSize(getPreferredSize());

      initSquareList();

      squareList.get(0).setEvent(new Event("スタート"));

      setForwardingEvent(); //"進む"イベント(1)
      setRotateEvent("⤵ 90°"); //盤面回転イベント(1)
      setForwardingEvent(); //"進む"イベント(2)
      setRotateEvent("⤵ 180°"); //盤面回転イベント(2)
      setForwardingEvent(); //"進む"イベント(3)
      setToLeftEvent(); //"左へ"イベント
      setForwardingEvent(); //"進む"イベント(4)
      setForwardingEvent(); //"進む"イベント(5)
      setForwardingEvent(); //"進む"イベント(6)
    }

    //マス準備
    private void initSquareList() {
      Turtle turtle = new Turtle(square_count/2, square_count/2, Turtle.Direction.EAST, new Turtle.Confirm() {
        @Override
        public boolean isSafe(int x, int y) {
          for(Square s : squareList) if(s.x == x && s.y == y) return false; //既に通った
          return true;
        }
      });

      for(int number=1; number<=square_count*square_count; turtle.forward(), number++) {
        squareList.add(new Square(number, turtle.getX(), turtle.getY(), square_size));
      }
    }

    //"進む"イベント
    private void setForwardingEvent() {
      Square square = getRandomSquare();
      if(!square.hasEvent()) {
        int a = random.nextInt(5) + 2;
        square.setEvent(new Event("+" + a + "マス"));
      }
    }
    //盤面回転イベント
    private void setRotateEvent(String text2) {
      Square square = getRandomSquare();
      if(!square.hasEvent()) {
        square.setEvent(new Event("盤面", text2));
      }
    }
    //"左へ"イベント
    private void setToLeftEvent() {
      Square square = getRandomSquare();
      if(!square.hasEvent()) {
        square.setEvent(new Event("2マス", "左へ"));
      }
    }

    //ランダムにマスを選択
    private Square getRandomSquare() {
      return getRandomSquare(square_count,0,square_count,0);
    }
    private Square getRandomSquare(int xbound, int xoffset, int ybound, int yoffset) {
      int x = random.nextInt(xbound) + xoffset;
      int y = random.nextInt(ybound) + yoffset;
      return getSquare(x, y);
    }
    //x,y指定から Square を得る
    private Square getSquare(int x, int y) {
      for(Square s : squareList) if(s.x == x && s.y == y) return s;
      return null;
    }

    public void paintComponent(Graphics g) {
      super.paintComponent(g);

      //ゴール
      g.setColor(Color.yellow);
      g.fillRect(square_size*square_count, 0, square_size, square_size);
      g.setColor(Color.black);
      g.drawString("ゴール", square_size*square_count+5, 13);

      //各マス
      for(Square s : squareList) s.draw(g);

      //点線
      DotLineDrawer dotLineDrawer = new DotLineDrawer(5);
      dotLineDrawer.setStart(squareList.get(0).getPoint(Square.Direction.CENTER));
      for(Square s : squareList.subList(1, squareList.size()-1)) {
        dotLineDrawer.draw(g, s.getPoint(Square.Direction.CENTER));
      }
      dotLineDrawer.draw(g, squareList.get(squareList.size()-1).getPoint(Square.Direction.EAST)); //最後はマスの右端まで
    }
  }
}
