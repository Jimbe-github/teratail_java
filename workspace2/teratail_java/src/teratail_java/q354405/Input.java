package teratail_java.q354405;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Input {

  public static void main(String[] args) {
    List<String> list = new ArrayList<>();

    //入力
    try (Scanner scanner = new Scanner(System.in);) {

      System.out.println("数字aを入力してください");
      list.add(scanner.next());

      System.out.println("数字bを入力してください");
      list.add(scanner.next());

      System.out.println("数字cを入力してください");
      list.add(scanner.next());

      for(String s : list) {
        if (!isInteger(s)) {
          System.out.println(Integer.MAX_VALUE + "以下の数字で入力してください。");
          System.exit(0);
        }
      }

    } catch (Exception e) {
      System.out.println("数字を入力してください。");
      System.exit(0);
    }

    //昇順にソート
    Collections.sort(list, (o1,o2)->Long.signum(Long.valueOf(o1)-Long.valueOf(o2)));
    System.out.println("3つの値:" + list);
  }

  //number of digit
  private static int MAX_NOD = String.valueOf(Integer.MAX_VALUE).length();
  private static int MIN_NOD = String.valueOf(Integer.MIN_VALUE).length();

  private static Pattern DIGIT_PATTERN = Pattern.compile("[\\+-]?[0-9]+");

  // long で扱えない桁数の数字列でも例外を発生しない ( false を返す)
  static boolean isInteger(String s) {
    Matcher matcher = DIGIT_PATTERN.matcher(s);
    if(!matcher.matches()) throw new NumberFormatException();
    return (s.length() <= MAX_NOD+(s.startsWith("+")?1:0) && Long.valueOf(s) <= Integer.MAX_VALUE) || //正数
        (s.startsWith("-") && s.length() <= MIN_NOD && Long.valueOf(s) >= Integer.MIN_VALUE); //負数
  }
}