package teratail_java.q367984;

import java.io.*;

//1秒後に停止する
class Worker extends Thread {
  private int count = 0;
  private boolean end = false;
  public void run() {
    try {
      Thread.sleep(1000);
      System.out.println("Enter to End.");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    end = true;
  }
  void countUp() { if(!end) count ++; }
  int getCount() { return count; }
}

class Sample {
  public static void main(String[] args) {
    Worker worker = new Worker();
    try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in));) {
      System.out.print("Enter to Start.");
      br.readLine();

      worker.start();
      System.out.println("Hit Enters.");
      while (worker.isAlive()) {
        br.readLine();
        worker.countUp();
      }
    } catch (IOException ignore) {
    }
    System.out.println("count="+worker.getCount());
  }
}