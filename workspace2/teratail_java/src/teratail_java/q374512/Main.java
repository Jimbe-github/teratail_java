package teratail_java.q374512;

public class Main {
  public static void main(String[] args) {
    String strBefore = "abbcaab";
    //→表示させる必要あり
    System.out.println(strBefore);

    String strAfter = new Main().removeDuplicates(strBefore);

    System.out.println(strAfter);
  }

  public String removeDuplicates(String strBefore) {
    String result = ""; //返り値用変数
    while(!strBefore.isEmpty()) { //空になるまで
      String c = "" + strBefore.charAt(0); //先頭1文字を(文字列として)取り出す
      result += c; //保存(追加)
      strBefore = strBefore.replaceAll(c, ""); //該当文字を全部消す
    }
    return result;
  }
}