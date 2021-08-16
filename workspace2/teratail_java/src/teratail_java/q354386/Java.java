package teratail_java.q354386;
//package java;

import java.util.Random;

public class Java {

  public static void main(String[] args) {
    // TODO 自動生成されたメソッド・スタブ

    //整数型の変数グーに0を代入する
    int Goo = 0;

    //整数型の変数チョキに1を代入する
    int Choki = 1;

    //整数型の変数パーに2を代入する
    int Par = 2;

    //文字列型の配列resultに、「グー,チョキ,パー」を代入する
    String result[] = { "グー　", "チョキ", "パー　" };

    //文字型の変数judgeを宣言する
    String judge = null;

    //整数型の変数losecountに、0を代入する
    int losecount = 0;

    //乱数クラス型の変数rdに、実体化させた乱数を作る機能や処理をまとまたものを代入する
    Random rd = new Random();

    //整数型の変数iに、1を代入して、変数iに1ずつ足し、変数iが10以下の場合、処理を繰り返す
    for (int i = 1; i <= 10; i++) {

      //整数型の変数aに、ランダムに取得した3未満の整数を代入する
      int a = rd.nextInt(3);

      //整数型の変数bに、ランダムに取得した3未満の整数を代入する
      int b = rd.nextInt(3);

      //じゃんけんの結果があいこだった場合下記の処理を行う
      if (a == b) {

        //変数judgeに、「引き分け」を代入
        judge = "　引き分け";

        //変数losecountに0を代入する
        losecount = 0;

        //じゃんけんの手がAさんがグー、Bさんがチョキまたは、Aさんがチョキ、Bさんがパーまたは、Aさんがパー、Bさんがグーの場合下記の処理を行う
      } else if ((a == Goo && b == Choki) || (a == Choki && b == Par) || (a == Par && b == Goo)) {

        //変数judgeに、「勝者：Aさん」を代入
        judge = "　勝者：Aさん";

        //変数losecountに0を代入する
        losecount = 0;

        //上記2つの計算条件に当てはまらないければ、下記処理を実行する
      } else {

        //変数judgeに、「勝者：Bさん」を代入
        judge = "　勝者：Bさん";

        //変数losecountに1ずつ足す
        losecount++;
      }

      System.out.println(i + "回目　Aさん" + result[a] + "　Bさん" + result[b] + judge);

      //変数losecountの値が3となったら下記処理を実行する
      if (losecount == 3) {

        //繰り返し処理を終了する
        break;
      }
    }
  }
}