package teratail_java.q373120;

public class Fight {
  public static void main(String args[]) {
    RPGCharacter c1 = new RPGCharacter("Jay", 10);
    RPGCharacter c2 = new RPGCharacter("Rock", 10);
    c1.setTarget(c2);
    c2.setTarget(c1);
    c1.start();
    c2.start();
  }
}

class RPGCharacter extends Thread {
  private static Object[] lock = new Object[0];

  private String name;
  private int hp;
  private RPGCharacter target;

  RPGCharacter(String name, int hp) {
    this.name = name;
    this.hp = hp;
  }

  void setTarget(RPGCharacter target) {
    this.target = target;
  }

  void attack() {
      while(!interrupted()) {
        try {
          Thread.sleep((long) (10 * Math.random()));
        } catch(InterruptedException ie) {
          break; //sleep 中に倒された
        }

        synchronized(lock) {
          if(interrupted()) break; //lock 獲得待ち中に倒された
          boolean down = target.wasAttacked();
          System.out.println(name + " attacked " + target.name + " , " + this + " , " + target);
          if(down) break; //倒した
        }
      }

      if(hp <= 0) {
        System.out.println(name + " lost.");
      }
  }

  @Override
  public void run() {
    attack();
  }

  boolean wasAttacked() {
    if(--hp > 0) return false; //まだ倒れない
    interrupt();
    return true; //倒れた
  }

  @Override
  public String toString() {
    return name + ":" + hp;
  }
}