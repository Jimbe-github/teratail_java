package teratail_java.q372208;

import java.util.*;

public class Main {

  //static final int mod = 1000000007;

  private static class DpPattern {
    private List<int[]> p = new ArrayList<int[]>();

    void add(int v, DpPattern other) {
      if(other == null) {
        p.add(new int[]{v});
      } else {
        for(int[] a : other.p) {
          int[] newArray = Arrays.copyOf(a, a.length+1);
          newArray[a.length] = v;
          p.add(newArray);
        }
      }
    }
    boolean isEmpty() { return p.isEmpty(); }
    @Override
    public String toString() {
      StringJoiner sj1 = new StringJoiner(",","{","}");
      for(int[] a : p) {
        StringJoiner sj2 = new StringJoiner(",","(",")");
        for(int v : a) sj2.add(""+v);
        sj1.add(sj2.toString());
      }
      return sj1.toString();
    }
  }

  static class Data {
    final int n, x;
    final int[] a;
    Data(int x, int[] a) {
      this.n = a.length;
      this.x = x;
      this.a = a;
    }
  }

  public static void main(String[] args) {
    Data data;

    //data = input();
    data = new Data(10, new int[]{3,1,4,2,5}); //テスト
    //data = new Data(12, new int[]{7,5,3,1,8}); //テスト2
    //data = new Data(5, new int[]{1,1,4,1}); //テスト3
    //data = new Data(4, new int[]{2,2,1,1}); //テスト4

    long[] dp = new long[data.x + 1];

    DpPattern[] dpp = new DpPattern[data.x + 1];
    for(int i=0; i<dpp.length; i++) dpp[i] = new DpPattern();

    dp[0] = 1;
    print(dp, dpp, "init");

    for (int i = 0; i < data.n; i++) {
      for (int j = data.x; j >= data.a[i]; j--) {
        dp[j] += dp[j - data.a[i]];

        dpp[j].add(data.a[i], j==data.a[i]?null:dpp[j-data.a[i]]);

        print(dp, dpp, "i="+i+", j="+j);
        // dp[j] %= mod;
      }
    }
    System.out.println("結果:" + dp[data.x] + dpp[data.x]);
  }

  private static Data input() {
    try(Scanner sc = new Scanner(System.in);) {

      int n = sc.nextInt();
      int x = sc.nextInt();

      int a[] = new int[n];
      for (int i = 0; i < n; i++) {
        a[i] = sc.nextInt();
      }
      return new Data(x, a);
    }
  }

  private static void print(long[] dp, DpPattern[] dpp, String header) {
    System.out.print(header+": [");
    for(int i=0; i<dp.length; i++) {
      System.out.print((i==0?"":", ") + dp[i]);
      if(!dpp[i].isEmpty()) System.out.print(dpp[i]);
    }
    System.out.println("]");
  }
}