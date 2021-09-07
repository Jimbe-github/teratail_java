package teratail_java.q357803;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;

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
      gx += 2; //少し内側(?)へ
      gy += 2;
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

  //描画する紙を表すクラス
  private static class GamePanel extends JPanel {
    private List<Square> squareList = new ArrayList<>(); //中心を index=0(number=1) とし、螺旋を展開した直線状態
    private Map<Point,Event> eventMap = new HashMap<>(); //座標とその位置のイベント
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

      eventMap.put(new Point(square_count/2,square_count/2), new Event("スタート"));
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

    //マス
    private class Square extends Point {
      private int gx, gy;
      Square(int x, int y) {
        super(x,y);
        gx = x * square_size;
        gy = y * square_size;
      }
      Point getCenterPoint() {
        return new Point(gx+square_size/2, gy+square_size/2);
      }
      Point getEastPoint() {
        return new Point(gx+square_size, gy+square_size/2);
      }
      void draw(Graphics g, int number) {
        Graphics g2 = g.create();
        g2.setColor(new Color(0, 170, 0));
        g2.fillRect(gx, gy, square_size, square_size);
        g2.dispose();

        g.drawRect(gx, gy, square_size, square_size); //枠

        drawStringAtBottomRight(g, ""+number); //番号
      }
      private void drawStringAtBottomRight(Graphics g, String text) {
        FontMetrics fm = g.getFontMetrics();
        int descent = fm.getDescent(); //ベースラインの高さ
        Rectangle2D rect = fm.getStringBounds(text, g);
        int width = (int)rect.getWidth(); //文字列の幅
        g.drawString(text, gx+square_size-width-1, gy+square_size-descent-1); //-1 はちょっと隙間
      }
    }

    //マス準備
    private void initSquareList() {
      Turtle turtle = new Turtle(square_count/2, square_count/2, Turtle.Direction.EAST, new Turtle.Confirm() {
        @Override
        public boolean isSafe(int x, int y) {
          return !squareList.contains(new Point(x,y)); //既に通った
        }
      });

      for(int number=1; number<=square_count*square_count; turtle.forward(), number++) {
        squareList.add(new Square(turtle.getX(), turtle.getY()));
      }
    }

    //"進む"イベント
    private void setForwardingEvent() {
      Point point = getRandomPoint();
      if(!eventMap.containsKey(point)) {
        int a = random.nextInt(5) + 2;
        eventMap.put(point, new Event("+" + a + "マス"));
      }
    }
    //盤面回転イベント
    private void setRotateEvent(String text2) {
      Point point = getRandomPoint();
      if(!eventMap.containsKey(point)) {
        eventMap.put(point, new Event("盤面", text2));
      }
    }
    //"左へ"イベント
    private void setToLeftEvent() {
      Point point = getRandomPoint(square_count-2, 2, square_count, 0);
      if(!eventMap.containsKey(point)) {
        eventMap.put(point, new Event("2マス", "左へ"));
      }
    }

    //ランダムにマスを選択
    private Point getRandomPoint() {
      return getRandomPoint(square_count,0,square_count,0);
    }

    private Point getRandomPoint(int xbound, int xoffset, int ybound, int yoffset) {
      int x = random.nextInt(xbound) + xoffset;
      int y = random.nextInt(ybound) + yoffset;
      return new Point(x, y);
    }

    public void paintComponent(Graphics g) {
      super.paintComponent(g);

      //ゴール
      g.setColor(Color.yellow);
      g.fillRect(square_size*square_count, 0, square_size, square_size);
      g.setColor(Color.black);
      g.drawString("ゴール", square_size*square_count+5, 13);

      //マス・点線
      DotLineDrawer dotLineDrawer = new DotLineDrawer(5);
      for(int i=0; i<squareList.size(); i++) {
        Square s = squareList.get(i);
        s.draw(g, i+1); //マス
        if(i == 0) { //最初
          dotLineDrawer.setStart(s.getCenterPoint());
        } else if(i == squareList.size()-1) { //最後
          dotLineDrawer.draw(g, s.getEastPoint());
        } else {
          dotLineDrawer.draw(g, s.getCenterPoint());
        }
      }

      //各イベント
      for(Map.Entry<Point,Event> entry : eventMap.entrySet()) {
        Point p = entry.getKey();
        Event e = entry.getValue();
        e.draw(g, p.x*square_size, p.y*square_size);
      }
    }
  }
}
