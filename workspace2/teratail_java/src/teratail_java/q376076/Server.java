package teratail_java.q376076;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server implements Runnable {
  public static void main(String[] args) {
    System.out.println("ハイカードを開始します。");
    Manager manager = new Manager();
    try(ServerSocket svSock = new ServerSocket(10000);) {
      while(true) {
        //System.out.println("プレイヤーの接続を待っています。");
        Socket sock = svSock.accept();
        try {
          Server server = new Server(sock, manager);
          if(server.getPlayerName().equals("terminater")) {
            server.close();
            break;
          }
          new Thread(server).start();
        } catch(IOException e) {
          e.printStackTrace();
        }
      }
    } catch(IOException e) {
      e.printStackTrace();
    }
    System.out.println("ハイカードを終了します。");
  }

  private static class Manager {
    private Set<Server> playerSet = new HashSet<>();
    synchronized void add(Server server) {
      playerSet.add(server);
    }
    synchronized void remove(Server server) {
      playerSet.remove(server);
    }
  }

  private Manager manager;
  private DataInputStream in;
  private DataOutputStream out;

  private String playerName;
  private int syojikin;

  Server(Socket sock, Manager manager) throws IOException {
    in = new DataInputStream(sock.getInputStream());
    out = new DataOutputStream(sock.getOutputStream());
    this.manager = manager;

    playerName = in.readUTF();
    //System.out.println(playerName + "を受信しました。");
    syojikin = in.readInt();
  }

  String getPlayerName() { return playerName; }
  void join() throws InterruptedException {
    Thread.currentThread().join();
  }

  @Override
  public void run() {
    manager.add(this);
    try {
      Random rnd = new Random();

      while(true) {
        int kakekin = in.readInt();
        if(kakekin <= 0) break; //ゲーム終了
        //System.out.println(kakekin + "を受信しました。");

        int rnd1 = rnd.nextInt(13) + 1;
        //System.out.println("1回目の乱数は" + rnd1 + "です。");
        out.writeInt(rnd1);

        int playerYosou = in.readInt();
        //System.out.println("プレイヤーは" + playerYosou + "と予想しました。");

        int rnd2 = rnd.nextInt(13) + 1;
        //System.out.println("二回目は" + rnd2);
        out.writeInt(rnd2);

        boolean win = playerYosou == 99 ? rnd1 <= rnd2 : rnd1 > rnd2;
        if(win) {
          syojikin = syojikin + kakekin * 2;
          kakekin = kakekin + 100;
        } else {
          syojikin = syojikin - kakekin;
          kakekin = 0;
        }
        //System.out.println("所持金は" + syojikin);
        out.writeInt(kakekin);
        out.writeInt(syojikin);
      }

    } catch(IOException e) {
      e.printStackTrace();
    } finally {
      close();
    }
    manager.remove(this);
  }

  void close() {
    try { out.close(); } catch(Exception ignore) {}
    out = null;
    try { in.close();  } catch(Exception ignore) {}
    in = null;
  }
}
