package teratail_java.q357803;

//右好きの亀さん
class Turtle {
  interface Confirm {
    //安全確認. x,y にマイナスや大きな値が入っても例外を出さないよう注意.
    boolean isSafe(int x, int y);
  }
  enum Direction {
    NORTH(0,-1, 1,3),
    EAST(1,0, 2,0),
    SOUTH(0,1, 3,1),
    WEST(-1,0, 0,2);

    final int dx, dy;
    private int right, left;
    Direction(int dx, int dy, int right, int left) {
      this.dx = dx;
      this.dy = dy;
      this.right = right;
      this.left = left;
    }
    Direction right() { return values()[right]; }
    Direction left() { return values()[left]; }
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