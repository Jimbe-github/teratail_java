package teratail_java.q376076;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int syojikin = 1000; //所持金の定義
    int kakekin = 0;

    try(Socket sock = new Socket("localhost", 10000);
        DataInputStream in = new DataInputStream(sock.getInputStream());
        DataOutputStream out = new DataOutputStream(sock.getOutputStream());) {

      System.out.println("サーバに接続しました。");

      //名前の入力
      System.out.print("名前：");
      String playerName = sc.next();

      out.writeUTF(playerName);
      out.writeInt(syojikin);

      System.out.println("ハイカードを開始します。");

      while(true) {

        if(kakekin <= 0) {
          System.out.println("かけ金を入力して下さい。現在の所持金：" + syojikin);
          //掛け金の入力
          System.out.print("かけ金：");
          kakekin = sc.nextInt();
        } else {
          System.out.println("かけ金：" + kakekin);
        }
        out.writeInt(kakekin);

        System.out.println("1回目の乱数を受信待ちです。");
        int rnd1 = in.readInt();
        System.out.println("1回目の乱数は" + rnd1 + "でした。");

        System.out.println("2回目の乱数は1回目よりも大きい？小さい？");
        System.out.println("小さいと思うなら1を、大きいと思うなら99を入力してね。");
        //予想をキーボードから入力「1」or「99」
        int yosou = sc.nextInt();
        //予想を送信
        out.writeInt(yosou);
        System.out.println("予想を送信しました。");

        //二回目
        int rnd2 = in.readInt();
        System.out.println("2回目の乱数は" + rnd2 + "でした。");

        kakekin = in.readInt();
        syojikin = in.readInt();

        //所持金の表示
        System.out.println("所持金は" + syojikin);

        if(kakekin > 0) {
          System.out.print("次のかけ金は自動的に" + kakekin + "となります。");
        }
        if(syojikin < kakekin) {
          System.out.print("所持金が足りないため、ゲームを終了します。");
          break;
        }

        System.out.println("続けますか? (yes=1/no=0):");
        int yn = sc.nextInt();
        if(yn != 1) break;
      }

    } catch(IOException e) {
      e.printStackTrace();
    }
  }
}
