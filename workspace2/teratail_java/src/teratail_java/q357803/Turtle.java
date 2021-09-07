package teratail_java.q357803;

//右好きの亀さん
class Turtle {
  interface Confirm {
    //安全確認. x,y にマイナスや大きな値が入っても例外を出さないよう注意.
    boolean isSafe(int x, int y);
  }
  enum Direction {
    NORTH(0,-1), /*右回りに定義すること*/
    EAST(1,0),
    SOUTH(0,1),
    WEST(-1,0);

    final int dx, dy;
    Direction(int dx, int dy) {
      this.dx = dx;
      this.dy = dy;
    }
    Direction right() { return values()[(ordinal()+1)%values().length]; }
    Direction left() { return values()[(ordinal()-1+values().length)%values().length]; }
  }
  private int x, y;
  private Turtle.Direction dir;
  private Turtle.Confirm confirm;
  Turtle(int x, int y, Turtle.Direction dir, Turtle.Confirm confirm) {
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
    Turtle.Direction sence = dir.right(); //この辺が右好き
    if(confirm.isSafe(x+sence.dx, y+sence.dy)) dir = sence;
  }
}