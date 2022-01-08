package teratail_java.q373551;

public class PrintThread extends Thread {
  private static String[] last = new String[1];
  private String message;

  public PrintThread(String message) {
    this.message = message;
  }

  public void run() {
    try {
      for(int i=0; i<10; i++) {
        synchronized(last) {
          while(last[0] == message) last.wait();

          System.out.print(message);

          last[0] = message;
          last.notifyAll();
        }
      }
    } catch(InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    PrintThread p1 = new PrintThread("*");
    p1.start();
    PrintThread p2 = new PrintThread("+");
    p2.start();
  }
}
