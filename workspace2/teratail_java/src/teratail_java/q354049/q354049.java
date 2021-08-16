package teratail_java.q354049;

public class q354049 {

  public static int now_gyo = 1;
  public static int now_retu = 2;
  public static int v = 5;

  public static void main(String[] args) {
    next();

    System.out.println("now_gyo="+now_gyo);
    System.out.println("now_retu="+now_retu);
  }

  public static void next() {

    int[][] hyo = new int[][] {
        {  0, -1, -1,  5, -1 },
        { -1,  0, -1,  4,  5 },
        { -1, -1,  0,  2, -1 },
        { -1, -1, -1,  0, -1 },
        { -1, -1, -1, -1,  0 }
    };
//    for (int i = now_retu + 1; i < v; i++) {
//      System.out.println("hyo["+now_gyo+"]["+i+"]="+hyo[now_gyo][i]);
//      if (hyo[now_gyo][i] == -1) {
//        now_retu = i;
//        return;
//      }
//    }
    for (int i = now_gyo; i < v; i++) {
      for (int j = (i==now_gyo?now_retu:i) + 1; j < v; j++) {
        System.out.println("hyo["+i+"]["+j+"]="+hyo[i][j]);
        if (hyo[i][j] == -1) {
          now_gyo = i;
          now_retu = j;
          return;
        }
      }
    }
  }
}