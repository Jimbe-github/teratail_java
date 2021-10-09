package teratail_java.q363500;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Q363500Server {
  public static void main(String[] args) throws IOException, InterruptedException {
    byte[] buf = new byte[1024];
    try(ServerSocket ss = new ServerSocket(2001);) {
      System.out.println("accept.");
      try(Socket s = ss.accept();
          BufferedInputStream bis = new BufferedInputStream(s.getInputStream());
          OutputStream os = s.getOutputStream();) {
        System.out.println("connected.");
        for(int i=0; i<3; i++) { //試行回数
          System.out.println("read.");
          int len = bis.read(buf, 0, buf.length);
          System.out.println("len="+len);
          if(len == -1) break;
          for(int j=0; j<len; j++) System.out.print(" buf["+j+"]="+buf[j]); System.out.println();
          //if(i == 2) {
            //Thread.sleep(2000); //2秒後に終わり
            //os.write(new byte[]{1,2}); //2バイトだけ送った所で終わり
            //break;
          //}
          if(len >= 4 && buf[0] == 1 && buf[1] == 2 && buf[2] == 3 && buf[3] == 4) {
            System.out.println("write.");
            os.write(new byte[]{1,2,3,4});
          }
          if(i == 2) {
            Thread.sleep(2000); //2秒後に終わり
            break;
          }
        }
        System.out.println("close.");
      }
    }
  }
}
