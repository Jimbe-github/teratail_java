package teratail_java.q367700;

import java.io.UnsupportedEncodingException;

public class Q367700 {
  public static void main(String[] args) throws UnsupportedEncodingException {
    final String data = "あいaうg\uD867\uDE3Dえhおjかkきlくlけこhdさjしuすuせそ";
    System.out.println("'"+data+"'");

    for(int i=0; i<20; i++) {
      int v = countChar(data, i, 0);
      System.out.print("i="+i+", v="+v);
      String s = data.substring(0, v);
      System.out.println(": '"+s+"'="+s.getBytes("UTF8").length);
    }

    System.out.println("------------");

    for(int start=0, v=0; (v=countChar(data,13,start)) > 0; start+=v) {
      System.out.print("v="+v);
      String s = data.substring(start, start+v);
      System.out.println(": '"+s+"'="+s.getBytes("UTF8").length);
    }
  }

  //UTF8 にした時に nbyte に収まる文字数を返す.
  private static int countChar(String src, int nbyte, int start) throws UnsupportedEncodingException {
    int last = start;
    for(int i=0; last<src.length(); ) {
      char c = src.charAt(last);
      i += getUTF8Length(c);
      if(i > nbyte) break;
      last += getUTF16Length(c);
    }
    return last - start;
  }

  private static int getUTF16Length(char c) throws UnsupportedEncodingException {
    int v = c & 0xffff;
    if(isHighSurrogate(v)) return 2;
    if(isLowSurrogate(v)) throw new UnsupportedEncodingException();
    return 1;
  }

  private static int getUTF8Length(char c) throws UnsupportedEncodingException {
    int v = c & 0xffff;
    if(isHighSurrogate(v)) return 4;
    if(isLowSurrogate(v)) throw new UnsupportedEncodingException();
    if(v < 128) return 1;
    if(v < 2048) return 2;
    return 3;
  }

  private static boolean isHighSurrogate(int c) {
    return 0xd800 <= c && c < 0xdc00;
  }

  private static boolean isLowSurrogate(int c) {
    return 0xdc00 <= c && c < 0xe000;
  }
}
