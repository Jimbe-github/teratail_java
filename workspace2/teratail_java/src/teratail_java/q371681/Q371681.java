package teratail_java.q371681;

import java.util.Scanner;

public class Q371681 {
  public static void main(String[] args) {
    //ゲーム説明
    System.out.println("このゲームは相手の３桁の数字を当てるゲームです");
    System.out.println("数字、桁両方合っていればHit、数字のみならばBlowと表示されます。");

    try(Scanner scanner = new Scanner(System.in);) {

      //ゲーム開始（player1の入力）
      System.out.println("p1さんは３桁の数字を入力してください");
      Numbers numbers = new Numbers(inputValue(scanner));

      // player2の入力
      System.out.println("p2さんは相手の３桁の数字を予想してください");
      while(true) {

        Numbers.Judgement judgement = numbers.judge(inputValue(scanner));
        if(judgement.hit == 3) {
          if(judgement.count == 1) {
            System.out.println("1回で正解しました。おめでとうございます！");
          } else {
            System.out.println("p2さんは"+judgement.count+"回目で正解しました。");
          }
          break;
        }

        System.out.println(judgement);
        //System.out.println("違います、再入力してください");
      }
    }
  }

  private static int inputValue(Scanner scanner) {
    int input = scanner.nextInt();
    while(!Numbers.check(input)) {
      System.out.println("数字は３桁です。");
      System.out.println("再入力してください");
      input = scanner.nextInt();
    }
    return input;
  }

  private static class Numbers {
    static class Judgement {
      final int count, hit, blow;
      Judgement(int count, int hit, int blow) {
        this.count = count;
        this.hit = hit;
        this.blow = blow;
      }
      @Override
      public String toString() {
        return hit+" hit, "+blow+" blow";
      }
    }

    static boolean check(int number) {
      return (100 <= number && number <= 999);
    }

    private int correct;
    private int count;
    Numbers(int correct) {
      if(!check(correct)) throw new IllegalArgumentException();
      this.correct = correct;
    }
    Judgement judge(int answer) {
      if(!check(answer)) throw new IllegalArgumentException();

      count ++;

      char[] corChars = String.valueOf(correct).toCharArray();
      char[] ansChars = String.valueOf(answer).toCharArray();

      int hit = 0;
      for(int i=0; i<ansChars.length; i++) {
        if(ansChars[i] == corChars[i]) {
          hit ++;
          ansChars[i] = '\0';
          corChars[i] = '\0';
        }
      }

      int blow = 0;
      for(int i=0; i<ansChars.length; i++) {
        if(ansChars[i] == '\0') continue;
        for(int j=0; j<corChars.length; j++) {
          if(ansChars[i] == corChars[j]) {
            blow ++;
            corChars[j] = '\0';
            break;
          }
        }
      }

      return new Judgement(count, hit, blow);
    }
  }
}