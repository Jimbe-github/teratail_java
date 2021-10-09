package Gomokunarabe;

public class Board {
  //碁盤の一辺の枠数
  private final int size;
  //勝利に必要な石数
  private final int victory;

  //石の位置を保存
  private Stone[][] stones;
  //残り空き数
  private int leftEmpty;

  Board() {
    this(5, 5);
  }
  Board(int size, int victory) {
    this.size = size;
    this.victory = victory;

    stones = new Stone[size][size];
    for(int y=0; y<size; y++) {
      for(int x=0; x<size; x++) {
        stones[y][x] = Stone.EMPTY;
      }
    }
    leftEmpty = size * size;
  }

  int getSize() {
    return size;
  }

  boolean setStone(int x, int y, Stone stone) {
    if(!checkPosition(x,y)) throw new IllegalArgumentException();
    if(getStone(x,y) != Stone.EMPTY) throw new IllegalStateException();
    stones[y][x] = stone;
    leftEmpty --;
    return winCheck(x, y, stone);
  }
  Stone getStone(int x, int y) {
    if(!checkPosition(x,y)) throw new IllegalArgumentException();
    return stones[y][x];
  }
  private boolean checkPosition(int x, int y) {
    return 0<=x && x<size && 0<=y && y<size;
  }

  boolean hasEmpty() {
    return leftEmpty > 0;
  }

  void show() {
    System.out.print(" ");
    for(int x=0; x<size; x++) {
      System.out.print(x);
      if(x < size-1) System.out.print(",");
    }
    System.out.println();

    for(int y=0; y<size; y++) {
      System.out.print(y);
      for(int x=0; x<size; x++) {
        System.out.print(stones[y][x]);
      }
      System.out.println();
    }
  }

  //8方向
  private static final int DELTA[]= {
      -1, 0,   1,0, // "─"
      -1,-1,   1,1, // "＼"
       0,-1,   0,1, // "│"
       1,-1,  -1,1, // "／"
  };

  private boolean winCheck(int cx, int cy, Stone stone) {
    int counts[] = new int[4]; //縦・横・斜めx2 の4方向の同石の数
    for(int i=0; i<DELTA.length; i+=2) {
      for(int x=cx+DELTA[i], y=cy+DELTA[i+1]; checkPosition(x,y) && stones[y][x]==stone; x+=DELTA[i], y+=DELTA[i+1]) {
        counts[i/4] ++;
      }
    }
    for(int count : counts) if(count+1 == victory) return true;
    return false;
  }
}
