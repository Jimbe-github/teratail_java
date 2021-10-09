package Gomokunarabe;

import java.util.*;

public class Game {
  public static void main(String args[]) {
    new Game().play();
  }

  private Board board;

  void play() {
    Scanner scanner = new Scanner(System.in);

    //board = new Board(10, 5); //1辺10マス, 勝利には5石必要
    board = new Board();
    board.show();
    Stone stone = Stone.BLACK; //先手:黒

    while(true) {
      System.out.println(stone.getName()+"の番です");
      if(inputAndSet(scanner, stone)) break;
      if(!board.hasEmpty()) {
        stone = Stone.EMPTY; //引き分け
        break;
      }
      board.show();
      stone = stone.nextTurn();
    }

    board.show();
    if(stone.isEmpty()) {
      System.out.println("引き分けです");
    } else {
      System.out.println(stone.getName()+"の勝利です！");
    }
    System.out.println("ゲームを終了します!");
  }

  //石配置処理
  private boolean inputAndSet(Scanner scanner, Stone stone) {
    while(true) {
      System.out.println("石の位置を入力してください");
      int x = input(scanner, 0, board.getSize()-1, "X=");
      int y = input(scanner, 0, board.getSize()-1, "Y=");
      if(board.getStone(x, y).isEmpty()) {
        return board.setStone(x, y, stone);
      }
      System.out.println("石が置かれていない場所を選択してください");
    }
  }

  private int input(Scanner scanner, int min, int max, String format, Object... args) {
    StringBuilder prompt = new StringBuilder();
    try(Formatter formatter = new Formatter(prompt);) {
      formatter.format(Locale.getDefault(Locale.Category.FORMAT), format, args);
    }

    while(true) {
      try {
        System.out.print(prompt);
        int v = scanner.nextInt();
        if(min <= v && v <= max) return v;
        System.out.println(min+"～"+max+"のどれかで入力してください");
      } catch(InputMismatchException e) {
        scanner.next(); //読み捨て
        System.out.println("整数で入力してください");
      }
    }
  }
}
