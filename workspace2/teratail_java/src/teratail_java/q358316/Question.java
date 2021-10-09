package teratail_java.q358316;

import java.util.Arrays;
import java.util.Scanner;

public class Question {
    String answer;

    String str = "トランザクション処理プログラムが，データベース更新の途中で異常終了した場合，ロールバック処理によってデータベースを復元する。このとき使用する情報はどれか。";
    String A = "A：最新のスナップショット情報";
    String B = "B：最新のバックアップファイル情報";
    String C = "C：ログファイルの更新後情報";
    String D = "D：ログファイルの更新前情報";

    String[] st = { "A", "B", "C", "D" };

    public void print() {
        System.out.println(str);
        System.out.println(A);
        System.out.println(B);
        System.out.println(C);
        System.out.println(D);
        System.out.println("選択肢を入力してください。");

        input();
    }

    public void input() {
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine().toUpperCase();

        if (checkAnswer(answer, st) == false) {
            System.out.println("選択肢の中から答えを入力してください。");
            print();
        }
    }

    public static boolean checkAnswer(String answer, String[] st) {
        if (Arrays.asList(st).contains(answer)) {
            return true;
        } else {
            return false;
        }
    }

}