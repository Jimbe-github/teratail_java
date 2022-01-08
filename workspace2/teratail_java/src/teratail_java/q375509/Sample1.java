package teratail_java.q375509;

import java.io.Console;
import java.io.PrintWriter;
import java.util.*;

//ユーザーインターフェース
interface IO {
  void println(String s);
  char[] input();
  char[] inputPassword();
}

//ユーザーインターフェース(コンソール版)
class ConsoleIO implements IO {
  private Console cons;
  private PrintWriter out;
  ConsoleIO() {
    cons = System.console();
    if(cons == null) {
      throw new IllegalStateException();
    }
    out = cons.writer();
  }
  @Override
  public void println(String s) {
    out.println(s);
  }
  @Override
  public char[] input() {
    return cons.readLine().toCharArray();
  }
  @Override
  public char[] inputPassword() {
    return cons.readPassword();
  }
}

//ユーザーインターフェース(System版)
class SystemIO implements IO {
  private Scanner sc;
  SystemIO(){
    sc = new Scanner(System.in);
  }
  @Override
  public void println(String s) {
    System.out.println(s);
  }
  @Override
  public char[] input() {
    return sc.nextLine().toCharArray();
  }
  @Override
  public char[] inputPassword() {
    return sc.nextLine().toCharArray();
  }
}

//三桁の数字
class Numbers {
  static boolean checkNumbers(char[] s) {
    if(s.length != 3 || s[0] == s[1] || s[1] == s[2] || s[2] == s[0]) return false;
    for(int i=0; i<s.length; i++) if("0123456789".indexOf(s[i]) < 0) return false;
    return true;
  }

  static class HitAndBlow {
    final int hit, blow;

    HitAndBlow(int hit, int blow) {
      this.hit = hit;
      this.blow = blow;
    }

    @Override
    public String toString() {
      return hit + "Hit " + blow + "Blow";
    }
  }

  private final char[] numbers;
  Numbers(char[] numbers) {
    if(!checkNumbers(numbers)) throw new IllegalArgumentException();
    this.numbers = numbers;
  }

  HitAndBlow checkHitAndBlow(Numbers other) {
    //ロジックはオリジナル尊重
    int hit = 0, blow = 0;
    for(int i = 0; i < numbers.length; i++) {
      if(numbers[i] == other.numbers[i]) {
        hit++;
      } else {
        for(int j = 0; j < numbers.length; j++) {
          if(numbers[i] == other.numbers[j]) {
            blow++;
          }
        }
      }
    }
    return new HitAndBlow(hit, blow);
  }

  @Override
  public String toString() {
    return Arrays.toString(numbers);
  }
}

//プレイヤー
class Player {
  final String name;
  private final Numbers numbers;
  private int predictCount;
  Player(String name, Numbers numbers) {
    this.name = name;
    this.numbers = numbers;
    this.predictCount = 0;
  }

  void incrementPredictCount() { predictCount++; }
  int getPredictCount() { return predictCount; }

  Numbers.HitAndBlow checkHitAndBlow(Numbers other) {
    return numbers.checkHitAndBlow(other);
  }

  @Override
  public String toString() {
    StringJoiner sj = new StringJoiner(",", "Player[", "]");
    sj.add("name=" + name);
    sj.add("numbers=" + numbers);
    sj.add("predictCount=" + predictCount);
    return sj.toString();
  }
}

public class Sample1 {
  private static final String[] NAMES = new String[]{ "player1", "player2" };

  public static void main(String[] args) {
    IO io = new ConsoleIO();
    //IO io = new SystemIO(); //テスト用 (inputPassword の入力が見えてしまう)

    //ゲーム説明
    io.println("このゲームは３桁の数字を当てるゲームです");
    io.println("正解するまでの回数が少なかった方が勝利です");
    io.println("数字と桁が両方合っていればHit,数字のみならばBlowと表示されます");

    //プレイヤー
    Player[] players = new Player[NAMES.length];
    for(int i=0; i<NAMES.length; i++) {
      io.println(NAMES[i] + "さん3桁の数字を設定してください");
      Numbers numbers = inputNumbers(io, true);
      players[i] = new Player(NAMES[i], numbers);
    }

    //プレイ
    for(int i=0; i<players.length; i++) {
      Player target = players[(i+1)%players.length]; //予想する相手
      io.println(players[i].name + "さん, " + target.name + "さんの３桁を予想してください");
      while(true) {
        Numbers predict = inputNumbers(io, false);
        players[i].incrementPredictCount();
        Numbers.HitAndBlow hab = target.checkHitAndBlow(predict);
        if(hab.hit == 3) {
          io.println(players[i].name + "さんは" + players[i].getPredictCount() + "回目で正解しました。");
          break;
        }
        io.println(hab.toString());
      }
    }

    //結果
    List<Player> winnerList = judgement(players);
    if(winnerList.size() == players.length) { //全員勝利=同点
      io.println("引き分けです");
    } else {
      StringJoiner sj = new StringJoiner(",", "", "さんの勝利です！");
      for(Player p : winnerList) sj.add(p.name);
      io.println(sj.toString());
    }
  }

  //数字入力
  static Numbers inputNumbers(IO io, boolean hide) {
    char[] s;
    while(true) {
      s = hide ? io.inputPassword() : io.input();
      //System.out.println("s="+Arrays.toString(s));
      if(Numbers.checkNumbers(s)) break;
      io.println("数字は重複せず、３桁です");
    }
    return new Numbers(s);
  }

  //最終判定
  static List<Player> judgement(Player[] players) {
    List<Player> winnerList = new ArrayList<Player>(Arrays.asList(players));
    Collections.sort(winnerList, (o1,o2) -> o1.getPredictCount()-o2.getPredictCount());
    //for(Player p : winnerList) System.out.println(p);
    for(int i=winnerList.size()-1; winnerList.get(i).getPredictCount()>winnerList.get(0).getPredictCount(); i--) winnerList.remove(i);
    return winnerList;
  }
}