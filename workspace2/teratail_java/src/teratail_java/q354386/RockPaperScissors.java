package teratail_java.q354386;

import java.util.Random;

public class RockPaperScissors {

  enum Judgement {
    Draw, Win, Lose;
  }

  enum Hand {
    Rock("グー　",    new Judgement[]{Judgement.Draw, Judgement.Win, Judgement.Lose}),
    Scissor("チョキ", new Judgement[]{Judgement.Lose, Judgement.Draw, Judgement.Win}),
    Paper("パー　",   new Judgement[]{Judgement.Win, Judgement.Lose, Judgement.Draw});

    private static Random random = new Random();
    static Hand getRandom() { return values()[random.nextInt(values().length)]; }

    private String name;
    private Judgement judgements[];
    Hand(String name, Judgement judgements[]) {
      this.name = name;
      this.judgements = judgements;
    }
    Judgement judge(Hand other) { return judgements[other.ordinal()]; }
    public String toString() { return name; }
  }

  static class Player {
    private String name;
    Player(String name) {
      this.name = name;
    }
    Hand getHand() { return Hand.getRandom(); }
    public String toString() { return name; }
  }

  public static void main(String[] args) {
    Player playerA = new Player("Ａさん");
    Player playerB = new Player("Ｂさん");

    int loseCount = 0;
    for(int i=1; i<=10 && loseCount<3; i++) {
      Hand handA = playerA.getHand();
      Hand handB = playerB.getHand();

      System.out.printf("%2d回目 %s%s %s%s ", i, playerA, handA, playerB, handB);

      Judgement judgement = handA.judge(handB);
      if(judgement == Judgement.Win) {
        System.out.println("勝者:"+playerA);
        loseCount = 0;
      } else if(judgement == Judgement.Lose) {
        System.out.println("勝者:"+playerB);
        loseCount ++;
      } else {
        System.out.println("引き分け");
        loseCount = 0;
      }
    }
    if(loseCount >= 3) {
      System.out.println(playerA+"の 3 連敗により終了");
    }
  }
}
