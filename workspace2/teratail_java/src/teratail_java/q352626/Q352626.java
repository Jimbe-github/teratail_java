package teratail_java.q352626;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Q352626 {
  public static void main(String args[]) {
    List<Hoge> hoge = Arrays.asList(
    new Hoge("営業部","tanaka@kaisya.com:satou@kaisya.com:yamaguchi@kaisya.com"),
    null,
    new Hoge("総務部","sasaki@kaisya.com"),
    new Hoge("第一企画部","yoshida@kaisya.com:kikuchi@kaista.com:watanabe@kaisya.com:kawai@kaisya.com")
    );

    List<Hoge> hoge2 = hoge.stream().filter(a -> a != null)
        .flatMap(a -> Arrays.stream(a.get2().split(":")).map(b -> new Hoge(a.get1(), b)))
        .collect(Collectors.toList());
    System.out.println("hoge2="+hoge2);

    List<Hoge> hoge3 = new ArrayList<>();
    for(Hoge a : hoge) {
      if(a == null) continue;
      for(String b : a.get2().split(":")) {
        hoge3.add(new Hoge(a.get1(), b));
      }
    }
    System.out.println("hoge3="+hoge3);
  }

  static class Hoge {
    private String a;
    private String b;
    Hoge(String a, String b) {
      this.a = a;
      this.b = b;
    }
    String get1() { return a; }
    String get2() { return b; }
    public String toString() { return "['"+a+"','"+b+"']"; }
  }
}
