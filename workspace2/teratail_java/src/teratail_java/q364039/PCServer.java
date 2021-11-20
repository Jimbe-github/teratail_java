package teratail_java.q364039;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.Enumeration;

public class PCServer {
  public static void main(String[] args) throws UnsupportedEncodingException {
    new Thread(new TCPReceiver(50001)).start();
  }

  interface Receiver extends Runnable {
    void close();
  }

  private static class TCPReceiver implements Receiver {
    private String TAG;
    private int port;
    //private Handler handler;
    //private TextView textView;
    private ServerSocket serverSocket;

    TCPReceiver(int port) {
      this.port = port;
      //this.handler = handler;
      //this.textView = textView;
      TAG = "TCPReceiver("+port+")";
    }

    @Override
    public void close() {
      if(serverSocket != null && !serverSocket.isClosed()) try { serverSocket.close(); } catch(IOException ignore) {}
    }

    @Override
    public void run() {
      close(); //念の為
      try {
        serverSocket = new ServerSocket(port, 1, InetAddress.getByName("192.168.1.6")/*getLocalIpAddress()*/);
        Log.d(TAG, "accept start.");
        try(Socket socket = serverSocket.accept();
            BufferedReader r = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF8")))) {
          Log.d(TAG, "accept return.");
          String str = r.readLine();
          Log.d(TAG, "readLine. str='" + str + "'");
          //handler.post(() -> textView.setText(str));
        }
      } catch(IOException e) {
        e.printStackTrace();
      } finally {
        Log.d(TAG, "close.");
        close();
      }
      serverSocket = null;
    }

    static class Log {
      static void d(String tag, String msg) {
        System.out.println(tag+": "+msg);
      }
    }

    private InetAddress getLocalIpAddress() throws SocketException {
      InetAddress resultIpv6 = null;
      InetAddress resultIpv4 = null;

      for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {

        NetworkInterface intf = en.nextElement();
        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {

          InetAddress inetAddress = enumIpAddr.nextElement();
          Log.d(TAG, "inetAddress="+inetAddress.getHostAddress());
          if(!inetAddress.isLoopbackAddress()){
            if (inetAddress instanceof Inet4Address) {
              resultIpv4 = inetAddress;
            } else if (inetAddress instanceof Inet6Address) {
              resultIpv6 = inetAddress;
            }
          }
        }
      }
      if(resultIpv4 != null) Log.d(TAG, "resultIpv4="+resultIpv4.getHostAddress());
      if(resultIpv6 != null) Log.d(TAG, "resultIpv6="+resultIpv6.getHostAddress());
      return resultIpv4 != null ? resultIpv4 : resultIpv6;
    }
  }
}
