package teratail_java.q370701;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Excite翻訳(http://www.excite.co.jp/world/)を利用するクラス
 */
public class ExciteTrans {
  private String direction; // 翻訳する言語設定 "ENJA" or "JAEN"
  /** テスト用main() */
  public static void main(String[] args) {
    ExciteTrans et = new ExciteTrans();
    System.out.println(et.getTransText("Hello!")); // 翻訳対象テキスト
  }
  /** コンストラクタ */
  public ExciteTrans() { direction ="ENJA"; }
  public ExciteTrans(String str) {
    if (str.equals("JAEN") || str.equals("ENJA"))
      direction = str;
    else
      direction ="JAEN";
  }
  /**
   * テキストを翻訳
   * @param before 翻訳前のテキスト
   * @return 翻訳後のテキスト
   */
  public String getTransText(String before) {
    String afterText = null; // 翻訳されたテキスト
    try {
      // URLクラスのインスタンスを生成
      URL exciteURL = new URL("http://www.excite.co.jp/world/");
      // 接続します
      URLConnection con = exciteURL.openConnection();
      // 出力を行うように設定
      con.setDoOutput(true);
      // 出力ストリームを取得
      PrintWriter out = new PrintWriter(con.getOutputStream());
      // クエリー文の生成・送信
      out.print("before={"+ before +"}&before_lang={EN}&after_lang={JA}");
      out.close();
      // 入力ストリームを取得
      BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
      // 一行ずつ読み込む
      String aline;
      // 抽出用の正規表現
      String regex = "<textarea &#91;^>].*after.*>(.*)";
      Pattern pattern = Pattern.compile(regex);
      while ((aline = in.readLine()) != null) {
        System.out.println(aline);
        Matcher mc = pattern.matcher(aline);
        if(mc.matches()) {
          afterText = mc.group(1);
        }
      }
      in.close(); // 入力ストリームを閉じる
    } catch (IOException e) {
      e.printStackTrace();
    }
    return afterText;
  }
}