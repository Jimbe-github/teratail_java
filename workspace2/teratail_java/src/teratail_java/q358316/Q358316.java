package teratail_java.q358316;

import java.util.Scanner;

public class Q358316 {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    Q358316 q = new Q358316();
    for(String a=null; a==null; ) {
      q.print();
      a = q.input(scanner);
    }
  }

  String question =
      "トランザクション処理プログラムが，データベース更新の途中で異常終了した場合，ロールバック処理によってデータベースを復元する。このとき使用する情報はどれか。";
  String answers[] = {
      "最新のスナップショット情報",
      "最新のバックアップファイル情報",
      "ログファイルの更新後情報",
      "ログファイルの更新前情報",
  };

  public void print() {
    System.out.println(question);
    for(int i=0; i<answers.length; i++) {
      System.out.println(('A'+i)+"："+answers[i]);
    }
  }

  public String input(Scanner scanner) {
    System.out.println("選択肢を入力してください。");
    String answer = scanner.nextLine().toUpperCase();

    if (!checkAnswer(answer)) {
      System.out.println("選択肢の中から答えを入力してください。");
      return null;
    }
    return answer;
  }

  public boolean checkAnswer(String answer) {
    if(answer == null || answer.length() != 1) return false;
    int i = answer.charAt(0) - 'A';
    return 0 <= i && i < answers.length;
  }
}