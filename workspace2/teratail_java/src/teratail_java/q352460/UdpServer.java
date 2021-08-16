package teratail_java.q352460;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UdpServer {
  public static void main(String[] args) throws SocketException, IOException {
    byte[] buf = new byte[1024];

    DatagramPacket packet = new DatagramPacket(buf, buf.length);
    try(DatagramSocket socket = new DatagramSocket(55554);) {
      System.out.println("受信");
      socket.receive(packet);
      int len = packet.getLength();
      System.out.println(new String(buf, 0, len));
      System.out.println("終了");
    }
  }
}
