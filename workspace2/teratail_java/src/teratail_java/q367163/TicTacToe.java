package teratail_java.q367163;

import java.util.*;

public class TicTacToe {
  public static void main(String[] args) {
    new TicTacToe().start();
  }

  private static final int LINES[][] = {
      {0,1,2},{3,4,5},{6,7,8}, //横
      {0,3,6},{1,4,7},{2,5,8}, //縦
      {0,4,8},{2,4,6} //斜め
  };
  private static final String NUMBERS[] = {"1","2","3","4","5","6","7","8","9"};

  private int field[] = new int[3*3];

  void start() {
    Player players[] = new Player[]{ new User(1), new Computer(2,1) };

    printField(field, 1);
    boolean bingo = false;
    for(int j=0; j<3*3; j++) {
      Player p = players[j%2];
      p.decidePlaceAndWriteTo(field);
      printField(field, 1);
      if(bingo = isBingo(p.getNumber())) {
        System.out.println(p.getName()+"の勝ち");
        break;
      }
    }
    if(!bingo) {
      System.out.println("引き分け");
    }
  }

  private boolean isBingo(int v) {
    for(int l[] : LINES) {
      if(field[l[0]] == v && field[l[1]] == v && field[l[2]] == v) return true;
    }
    return false;
  }

  void printField(int f[], int v) {
    for(int i=0; i<3*3; i++) {
      System.out.print(f[i]==0?NUMBERS[i]:f[i]==v?"O":"X");
      if(i == 2 || i == 5 || i == 8) System.out.println();
    }
  }

  interface Player {
    void decidePlaceAndWriteTo(int field[]);
    String getName();
    int getNumber();
  }

  class User implements Player {
    private int num;
    private Scanner sc;

    User(int number) {
      num = number;
      sc = new Scanner(System.in);
    }

    public String getName() { return "ユーザ"; }
    public int getNumber() { return num; }
    public void decidePlaceAndWriteTo(int f[]) {
      while(true) {
        System.out.print("どこに置きますか: ");
        int i = sc.nextInt() - 1;
        if(i < 0 || f.length <= i) continue;
        if(f[i] == 0) {
          f[i] = num;
          return;
        }
        System.out.println("その場所はすでに置かれています");
      }
    }
  }

  class Computer implements Player {
    private final int CORNERS[] = { 0,2,6,8 };
    private final int SIDES[] = { 1,3,5,7 };

    private int mnum, onum;
    private Random random = new Random();

    Computer(int number, int opponent) {
      mnum = number;
      onum = opponent;
    }

    public String getName() { return "コンピュータ"; }
    public int getNumber() { return mnum; }
    public void decidePlaceAndWriteTo(int f[]) {
      int i = decidePlace(f);
      System.out.println(getName()+": "+(i+1));
      f[i] = mnum;
    }
    private int decidePlace(int f[]) {
      int index = onlyThisOne(f, mnum); //自分の勝ちの手
      if(index > 0) return index;

      index = onlyThisOne(f, onum); //相手の勝ちを阻止する手
      if(index > 0) return index;

      int n = 0; //何手終えているか
      for(int i=0; i<3*3; i++) if(f[i] != 0) n++;

      switch(n) {
      case 0: //先手
        return selectChoices(f, CORNERS); //角取って相手のミスを待つ
      case 2:
        if(f[4] == onum) { //相手が中央、自分が角なら対角位置
          if(f[0] == mnum) return 8;
          if(f[2] == mnum) return 6;
          if(f[6] == mnum) return 2;
          if(f[8] == mnum) return 0;
        }
        break;
      case 3:
        if(f[4] == mnum && ((f[0]==onum && f[8]==onum) || (f[2]==onum && f[6]==onum))) { //中央を挟んで対角に取られている?
          return selectChoices(f, SIDES); //角を取ると負けるので辺から選ぶ
        }
      }

      if(f[4] == 0) return 4; //中央が空いていたら中央

      index = selectChoices(f, CORNERS); //角
      if(index > 0) return index;

      return selectChoices(f, SIDES); //辺
    }

    private int selectChoices(int f[], int c[]) {
      List<Integer> spaceList = new ArrayList<>();
      for(int i : c) if(f[i] == 0) spaceList.add(i);
      if(spaceList.size() > 0) return spaceList.get(random.nextInt(spaceList.size()));
      return -1;
    }

    private int onlyThisOne(int f[], int v) {
      for(int l[] : LINES) {
        if(f[l[0]] == 0 && f[l[1]] == v && f[l[2]] == v) return l[0];
        if(f[l[0]] == v && f[l[1]] == 0 && f[l[2]] == v) return l[1];
        if(f[l[0]] == v && f[l[1]] == v && f[l[2]] == 0) return l[2];
      }
      return -1;
    }
  }
}