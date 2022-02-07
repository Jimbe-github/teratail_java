package teratail_java.q_0xrwgeqq8cknuy;

public class Main {

  public static void main(String[] args) {
    // TODO 自動生成されたメソッド・スタブ

  }

}

class ClientBoard {
  class Piece {
    private int index;
    private int preIndex = -1;
    private Piece(int index) {
      this.index = index;
    }

    String getArea() { return area[index]; }

    boolean canToRight() { return getRightIndex() >= maxColumn * maxLine; }
    int getRightIndex() { return index + 1; }
    void toRight() { preIndex = index; index = getRightIndex(); }

    boolean canToLeft() { return getLeftIndex() >= 0; }
    int getLeftIndex() { return index - 1; }
    void toLeft() { preIndex = index; index = getLeftIndex(); }

    boolean canToUp() { return getUpIndex() >= 0; }
    int getUpIndex() { return index - maxColumn; }
    void toUp() { preIndex = index; index = getUpIndex(); }

    boolean canToDown() { return getDownIndex() >= maxColumn * maxLine; }
    int getDownIndex() { return index + maxColumn; }
    void toDown() { preIndex = index; index = getDownIndex(); }
  }

  /** 盤の行数 */
  int maxLine;

  /** 盤の列数 */
  int maxColumn;

  String[] area;

  Piece createPiece(int index) { return new Piece(index); }

  String getArea(int index) { return area[index]; }

  boolean isEnable(int index) {
    return area[index].equals("-");
  }
}

interface Player {
}

class ComTron implements Player {

  /** プレイヤーの名前 */
  private String playerName;

  /** 盤の参照 */
  private ClientBoard board;

  public ComTron(String name, ClientBoard board) {
    playerName = name;
    this.board = board;
  }

  public String play(int order, int preCell) {  
    ClientBoard.Piece piece = board.createPiece(preCell);
    
    if(order == 1) {
      if(piece.canToRight()) {
        int postCell = piece.getRightIndex();
        if(judge(postCell, preCell)) return ""+postCell;
      }
      if(piece.canToLeft()) {
        int postCell = piece.getLeftIndex();
        if(judge(postCell, preCell)) return ""+postCell;
      }
    } else {
      if(piece.canToLeft()) {
        int postCell = piece.getLeftIndex();
        if(judge(postCell, preCell)) return ""+postCell;
      }
      if(piece.canToRight()) {
        int postCell = piece.getRightIndex();
        if(judge(postCell, preCell)) return ""+postCell;
      }
    }

    if(piece.canToUp()) {
      int postCell = piece.getUpIndex();
      if(judge(postCell, preCell)) return ""+postCell;
    }
    if(piece.canToDown()) {
      int postCell = piece.getDownIndex();
      if(judge(postCell, preCell)) return ""+postCell;
    }

    return ""+preCell;
  }

  public String getPlayerName() {
    return playerName;
  }

  public boolean judge(int nowCell, int preCell) {

    int j = preCell - board.maxColumn;
    if(j >= 0 && j != nowCell) {
      System.out.println("上  " + board.getArea(j));
      if(board.isEnable(j)) return true;
    }

    j = preCell - 1;
    if(j % board.maxColumn != 0 && j != nowCell) {
      System.out.println("左  " + board.getArea(j));
      if(board.isEnable(j)) return true;
    }

    j = preCell + 1;
    if(j % board.maxColumn != 0 && j != nowCell) {
      System.out.println("右  " + board.getArea(j));
      if(board.isEnable(j)) return true;
    }

    j = preCell + board.maxColumn;
    if(j < board.maxColumn * board.maxLine && j != nowCell) {
      System.out.println("下  " + board.getArea(j));
      if(board.isEnable(j)) return true;
    }

    return false;
  }
}