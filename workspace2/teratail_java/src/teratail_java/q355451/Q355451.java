package teratail_java.q355451;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Q355451 {

  public static void main(String[] args) throws IOException {
    try (ServerSocket ss = new ServerSocket(0);) {
      int port = ss.getLocalPort();
      System.out.println("port=" + port);

      Socket s = ss.accept();
      BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
      while (true) {
        System.out.println(br.readLine());
      }
    }
  }
}
