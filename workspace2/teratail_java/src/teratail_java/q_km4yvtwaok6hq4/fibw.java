package teratail_java.q_km4yvtwaok6hq4;

public class fibw {
  public static void main(String[] args){
    String[] f = new String[] { "A", "B" };
    for(int i=1, j=0; i<=10; i++, j^=1, f[j]=f[j^1]+f[j]) {
      System.out.println("Fib"+i+"= "+f[j]);
    }
  }
}
