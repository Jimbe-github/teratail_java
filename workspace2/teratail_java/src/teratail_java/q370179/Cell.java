package teratail_java.q370179;

import java.awt.*;

enum Cell {
  SPACE("引き分け") {
    void draw(Graphics g, int x, int y, int size) {}
  },
  CIRCLE("〇の勝ち") {
    void draw(Graphics g, int x, int y, int size) {
      Graphics2D g2 = (Graphics2D)g.create();
      g2.setStroke(new BasicStroke(5));
      g2.setColor(Color.RED);
      g2.drawOval(x,y,size,size);
    }
  },
  CROSS("×の勝ち"){
    void draw(Graphics g, int x, int y, int size) {
      Graphics2D g2 = (Graphics2D)g.create();
      g2.setColor(Color.BLUE);
      g2.setStroke(new BasicStroke(5));
      g2.drawLine(x,y,x+size,y+size);
      g2.drawLine(x,y+size,x+size,y);
    }
  };

  private String result;
  Cell(String result) {
    this.result = result;
  }
  abstract void draw(Graphics g, int x, int y, int size);
  Cell next() { return this == CIRCLE ? CROSS : CIRCLE; }
  String getResult() { return result; }
}