package teratail_java.q365774;

import java.io.*;
import java.util.*;

public class Search{
  public static void main(String[] args){
    try(Scanner scan = new Scanner(System.in);){
      System.out.println("一覧を取得するディレクトリ名を入力してください。");
      String searchFile = scan.nextLine();

      System.out.println("出力ファイル名を入力してください。");
      String outputFile = scan.nextLine();
      outputFile += ".txt";

      try(PrintWriter pw = new PrintWriter(new FileWriter(outputFile));){
        Queue<File> queue = new ArrayDeque<>();
        queue.add(new File(searchFile));
        dumpFile(queue, pw);
      }catch(IOException e){
        e.printStackTrace();
      }
    }
  }

  //検索し値を表示
  private static void dumpFile(Queue<File> queue, PrintWriter pw){
    for(File file=null; (file=queue.poll()) != null; ){
      pw.println("Directory: " + file.getName());

      File[] files = file.listFiles(); // ファイル一覧取得
      if(files.length == 0){ //ディレクトリが空か判定
        pw.println("空です");
      }
      for (File tmpFile : files){
        if(tmpFile.isDirectory()){
          queue.add(tmpFile); //ディレクトリだった場合保存
          pw.println(tmpFile.getName() + "はディレクトリです。");
        }else{
          pw.println(tmpFile.getName() + "はファイルです。");
        }
      }

      pw.println(); //見やすさのため
    }
  }
}