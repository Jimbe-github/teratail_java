package teratail_java.q364117;

public class Primes {
  static void printIfPrime(int n) {
    //if (Prime.isPrime(n)) { //Prime が無いのでここはエラーになる.
      System.out.print(n + " ");
    //}
  }

  public static void main(String[] args) {
    long t1 = System.currentTimeMillis();
    int n = 100;//Integer.parseInt(args[0]);

    for (int i = 1; i <= n; i++) {
      final int v = i;
      new Thread(() -> printIfPrime(v)).start();
    }

    long t2 = System.currentTimeMillis();
    System.out.println();
    System.out.println((t2 - t1) + "ms");
  }
}
