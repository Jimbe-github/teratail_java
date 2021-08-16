package teratail_java.q353621;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Q353621 {
  public static void main(String[] args) {
    int n = 6; //要素の和

    int buf[] = new int[n];
    buf[0] = n;
    Set<String> done = new HashSet<>();
    while(true) {
      //既存チェック・表示
      String s = Arrays.toString(Arrays.stream(buf).filter(v->v!=0).sorted().toArray());
      if(done.add(s)) System.out.println(s);
      //終了チェック
      if(buf[n-1] == 1) break;
      //次パターン生成
      int i = 0;
      while(buf[i] == 1) i++;
      buf[i] --;
      buf[i+1] ++;
      if(i > 0) {
        buf[0] += buf[i] - 1;
        buf[i] = 1;
      }
    }
  }
}