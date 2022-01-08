package teratail_java.q374936;

import java.util.Arrays;

class Player {
  private String[] entry = new String[0];

  void addEntry(String s) {
    entry = Arrays.copyOf(entry, entry.length+1);
    entry[entry.length-1] = s;
  }
  String getEntry(int i) {
    return entry[i];
  }
}

public class Main {
  public static void main(String[] args) {
    Player p = new Player();
    p.addEntry("日本代表");
    System.out.println(p.getEntry(0));
  }
}
