package teratail_java.q375087;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
  public static void main(String[] args) {
    //try(Scanner sc = new Scanner(System.in);) {
    //  String str = sc.next();
    //  System.out.println(expansion(str));
    //}

    String result = expansion("6(iztn2(4i))");
    System.out.println(result);
    if(result.equals("iztniiiiiiiiiztniiiiiiiiiztniiiiiiiiiztniiiiiiiiiztniiiiiiiiiztniiiiiiii")) {
      System.out.println("OK.");
    } else {
      System.out.println("NG.");
    }
  }

  private static final Pattern REPEAT_PATTERN = Pattern.compile("([1-9][0-9]*)(?:([a-zA-Z])|\\(([a-zA-Z]+)\\))");

  private static String expansion(String str) {
    for(Matcher m; (m=REPEAT_PATTERN.matcher(str)).find(); ) {
      str = reconstruction(str, m.start(), m.end(),
          Integer.parseInt(m.group(1)), m.group(2)!=null?m.group(2):m.group(3));
    }
    return str;
  }
  /**
   * src の start から end-1 の間を "repeatString を repeatCount 回繰り返した文字列" に置き換えた文字列を返す
   * @param src 元の文字列
   * @param start 置き換え開始位置
   * @param end 置き換え終了位置+1
   * @param repeatCount 繰り返し回数
   * @param repeatString 繰り返し文字列
   * @return 置き換えた文字列
   */
  private static String reconstruction(String src, int start, int end, int repeatCount, String repeatString) {
    StringBuilder sb = new StringBuilder(src.substring(0, start));
    for(int i=0; i<repeatCount; i++) sb.append(repeatString);
    return sb.append(src.substring(end)).toString();
  }
}