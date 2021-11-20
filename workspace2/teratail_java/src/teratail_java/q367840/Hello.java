package teratail_java.q367840;

import java.util.*;

import teratail_java.q367840.VendingMachine.MoneyUnit;
import teratail_java.q367840.VendingMachine.Item;

public class Hello {
  public static void main(String[] args) {
    VendingMachine vendingMachine = new VendingMachine();

    try (Scanner scanner = new Scanner(System.in);) {
      while (true) {
        System.out.println("1:お金を入れる  2:飲み物を買う  3:終了");
        int n = scanner.nextInt();
        if (n == 1) {
          putMoney(vendingMachine, scanner);
        } else if (n == 2) {
          buyDrink(vendingMachine, scanner);
        } else if (n == 3) {
          break;
        }
      }
    }
    getChange(vendingMachine);
  }

  static void putMoney(VendingMachine vendingMachine, Scanner scanner) {
    int owari;
    MoneyUnit[] units = MoneyUnit.values();
    do {
      int num = select("何円入れますか", units, scanner);
      if (0 <= num && num < units.length) {
        System.out.println(units[num] + "を入れました");
        int total = vendingMachine.put(units[num]);
        System.out.println("現在" + total + "円入ってます");
      }
      System.out.println("続けますか？(1 YES 2 NO):");
      owari = scanner.nextInt();
    } while (owari == 1);
  }

  static void buyDrink(VendingMachine vendingMachine, Scanner scanner) {
    Item[] items = vendingMachine.getHandlingItems();
    int num = select("どの飲み物を買いますか", items, scanner);
    if (0 <= num && num < items.length) {
      if (vendingMachine.buy(num)) {
        System.out.println(items[num].name + "を買いました");
        System.out.println("残りは" + vendingMachine.getTotal() + "円です");
      } else {
        System.out.println("お金が足りません");
      }
    }
  }

  static int select(String prompt, Object objects[], Scanner scanner) {
    System.out.println(prompt);
    StringJoiner sj = new StringJoiner("  ");
    for (int i = 0; i < objects.length; i++) {
      sj.add((i + 1) + ":" + objects[i]);
    }
    System.out.println(sj);
    return scanner.nextInt() - 1;
  }

  static void getChange(VendingMachine vendingMachine) {
    Map<MoneyUnit,Integer> change = vendingMachine.change();
    if(change.isEmpty()) {
      System.out.println("おつりはありません");
    } else {
      StringJoiner sj = new StringJoiner(" ", "おつりを出しました (", ")");
      for(Map.Entry<MoneyUnit,Integer> entry : change.entrySet()) {
        sj.add(entry.getKey() + "玉 " + entry.getValue() + "枚");
      }
      System.out.println(sj);
    }
  }
}

class VendingMachine {

  enum MoneyUnit {
    YEN_1000(1000),
    YEN_500(500),
    YEN_100(100),
    YEN_50(50),
    YEN_10(10);

    static MoneyUnit[] coins() {
      return new MoneyUnit[] {YEN_500,YEN_100,YEN_50,YEN_10};
    }

    final int amount;
    MoneyUnit(int amount) {
      this.amount = amount;
    }
    @Override
    public String toString() {
      return amount+"円";
    }
  }

  static class Item {
    final String name;
    final int price;
    Item(String name, int price) {
      this.name = name;
      this.price = price;
    }
    @Override
    public String toString() {
      return name+"("+price+"円)";
    }
  }

  private Item[] items = new Item[] {
      new Item("水", 100),
      new Item("コーラ", 150),
      new Item("コーヒー", 120),
  };

  private int total = 0;

  int getTotal() { return total; }

  int put(MoneyUnit unit) {
    return total += unit.amount;
  }

  boolean buy(int index) {
    if (total < items[index].price) return false;
    total -= items[index].price;
    return true;
  }

  Item[] getHandlingItems() {
    return items.clone();
  }

  Map<MoneyUnit,Integer> change() {
    Map<MoneyUnit,Integer> map = new TreeMap<>();
    for(MoneyUnit unit : MoneyUnit.coins()) {
      int count = total / unit.amount;
      if(count > 0) map.put(unit, count);
      total %= unit.amount;
    }
    total = 0;
    return map;
  }
}