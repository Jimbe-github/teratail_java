package teratail_java.q358570;

import java.io.*;
import java.util.ArrayList;

public class Main {

  public static void main(String[] args) {
    String data;
    try {
      ArrayList <String> list = new ArrayList<String>();
      FileInputStream fi  = new FileInputStream(args[0]);
      InputStreamReader in = new InputStreamReader(fi,"UTF-8");
      BufferedReader fw = new BufferedReader(in);
      while((data = fw.readLine()) != null) {
        list.add(data.trim()); //".trim()" 追加
      }
      for(String m:list) {
        System.out.print(m+"\n");
      }
      for(int i=list.size()-1;i>=0;i--) {
        System.out.print(list.get(i)+"\n");
      }

      System.out.println((list.indexOf("さしす")+1)+"番目"); //行追加

      fw.close();
    } catch(IOException e) {
      System.out.print("IOEエラー");
    }finally {
    }
  }
}
