package teratail_java.q_6pkl4owg6y8zae;

import java.util.Random;
import java.util.Scanner;

// ジャンケンゲームのプログラム
public class Main {
  private static final int SCORE_TO_END = 30;
  // mainメソッド
  public static void main(String[] args) {

    // プレイヤーの準備
    // 自分（操作プレイヤー）
    Player self = new Player("自分") {
      private Scanner sc = new Scanner(System.in); // ゲーム中に操作入力するための変数
      Hand selectHand() {
        // 自分の選択（選択肢を表示して操作入力）
        System.out.println("何を出しますか？");
        Hand[] hands = Hand.values();
        for(int i = 0; i < hands.length; i++) { // 手札（「グー」「チョキ」「パー」）の選択肢
          System.out.println((i+1) + ": " + hands[i]);
        }
        System.out.println("0: (ゲーム終了)"); // 「ゲーム終了」の選択肢
        int c1 = sc.nextInt(); // ここで操作入力により選択する nextIntは数字を取り込むために使用
        if(c1 == 0) { // 「ゲーム終了」が選択された場合
          return null;
        }
        return Hand.values()[c1];
      }
    };

    // 対戦相手（コンピュータ）
    Player rival = new Player("相手") {
      private Random ran = new Random(); // ランダム値を出力するための変数
      Hand selectHand() {
        int c2 = ran.nextInt(100); // 0〜99のうちの整数がランダムで出力
        if(c2 < 40) { // 0〜32の場合（33%の確率）→0〜39 (40%の確率)
          return Hand.Rock; // 「グー」を代入
        } else if(c2 < 80) { // 33〜65の場合（33%の確率）→40〜79 (40%の確率)
          return Hand.Scissors; // 「チョキ」を代入
        }
        // 66〜99の場合（34%の確率）→80〜99 (20%の確率)
        return Hand.Paper; // 「パー」を代入
      }
    };

    // ゲーム開始
    // どちらかのプレイヤーが勝利条件を満たしたら終了
    while(self.getPoint() < SCORE_TO_END && rival.getPoint() < SCORE_TO_END) {

      // 自分の選択（選択肢を表示して操作入力）
      Hand selfHand = self.selectHand(); // 選択された手札を変数に代入
      if(selfHand == null) {
        break;
      }

      // 相手の選択（ランダムで選択される）
      Hand rivalHand = rival.selectHand();

      System.out.println();

      // 勝負（勝敗表示と得点計算）
      System.out.println("自分：" + selfHand.name);
      System.out.println("相手：" + rivalHand.name);

      if(selfHand.isWinTo(rivalHand)) { // 自分が勝った場合
        resultAction(self, selfHand, rival, rivalHand);
      } else if(rivalHand.isWinTo(selfHand)) { // 相手が勝った場合
        resultAction(rival, rivalHand, self, selfHand);
      } else { // あいこの場合
        System.out.println("あいこ");
      }
      System.out.println();

      // プレイヤー情報の表示
      System.out.println(self);
      System.out.println(rival);
      System.out.println();
    }
    System.out.println("ゲーム終了");
  }

  private static void resultAction(Player winner, Hand winHand, Player loser, Hand loseHand) {
    System.out.print(winner.name + "の勝ち！");
    System.out.println("（+" + winHand.winPoint + " point）");
    winner.addPoint(winHand.winPoint);
    loser.addPoint(loseHand.losePoint);
  }
}

// 手札のクラス
enum Hand {
  Rock("グー", 1, 0),
  Scissors("チョキ", 3, -1),
  Paper("パー", 1, 0);

  final String name; // 手札の名前（「グー」「チョキ」「パー」）
  final int winPoint; // その手札で勝った時の得点
  final int losePoint; // その手札で負けた時の得点

  // コンストラクタ
  Hand(String name, int winPoint, int losePoint) {
    this.name = name;
    this.winPoint = winPoint;
    this.losePoint = losePoint;
  }

  // 手札の勝ち負けを真偽値として返すメソッド
  boolean isWinTo(Hand hand) { // この変数「hand」は対戦相手の手札
    return next() == hand;
  }

  //一つ後ろ(グー→チョキ, チョキ→パー, パー→グー)を返す
  private Hand next() {
    return values()[(ordinal()+1)%values().length];
  }

  @Override
  public String toString() {
    return name + " (Point: Win " + winPoint + ", Lose "+ losePoint + ")";
  }
}

// プレイヤーのクラス
abstract class Player {
  final String name; // プレイヤー名
  private int point = 0; // 手持ちの点数

  Player(String name) {
    this.name = name;
  }

  abstract Hand selectHand();

  int getPoint() { return point; }

  void addPoint(int point) {
    this.point += point;
  }

  @Override
  public String toString() {
    return name + ":¥t" + point + " point";
  }
}