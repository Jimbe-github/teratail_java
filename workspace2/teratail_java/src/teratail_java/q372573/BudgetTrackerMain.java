package teratail_java.q372573;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//チェック用ダミー
class BudgetTrackerDao {
  public List<BudgetTrackerDto> selectAll() { return null; }
  public void insertIntoTable(BudgetTrackerDto budgetTrackerDto) {}
}

class BudgetTrackerDtoFactory {
  private int id, price;
  private Date date;
  private String storeName, productName, productType;

  static BudgetTrackerDtoFactory getInstance() { return new BudgetTrackerDtoFactory(); }

  BudgetTrackerDto create() {
    return new BudgetTrackerDto(id, date, storeName, productName, productType, price);
  }

  BudgetTrackerDtoFactory setId(int id) {
    this.id = id;
    return this;
  }
  BudgetTrackerDtoFactory setDate(Date date) {
    this.date = date;
    return this;
  }
  BudgetTrackerDtoFactory setStoreName(String storeName) {
    this.storeName = storeName;
    return this;
  }
  BudgetTrackerDtoFactory setProductName(String productName) {
    this.productName = productName;
    return this;
  }
  BudgetTrackerDtoFactory setProductType(String productType) {
    this.productType = productType;
    return this;
  }
  BudgetTrackerDtoFactory setPrice(int price) {
    this.price = price;
    return this;
  }
}

class BudgetTrackerDto {
  final private int id, price;
  final private Date date;
  final private String storeName, productName, productType;

  public BudgetTrackerDto(int id, Date date, String storeName, String productName, String productType, int price) {
    this.id = id;
    this.date = date;
    this.storeName = storeName;
    this.productName = productName;
    this.productType = productType;
    this.price = price;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ").add(""+id).add(""+date).add(storeName).add(productName).add(productType).add(""+price).toString();
  }
}

public class BudgetTrackerMain {
  private static int selectMenu(Scanner scanner, Map<Integer,? extends Object> menu) {
    int num = 0;
    do {
      for (Map.Entry<Integer, ? extends Object> entry : menu.entrySet()) {
        System.out.println(entry.getKey() + ":" + entry.getValue());
      }
      System.out.print("Select a number: ");
      String str = scanner.next();
      num = Integer.parseInt(str);
    } while (!menu.containsKey(num));
    return num;
  }

  interface Action {
    void doAction(Scanner scanner, BudgetTrackerDao budgetTrackerDao);
  }

  private static class SelectAction implements Action {
    private Map<Integer,String> menu = new HashMap<>();

    SelectAction() {
      menu.put(1, "Select All");
      menu.put(2, "Select by Date");
      menu.put(3, "Select by Store");
      menu.put(4, "Select by Product");
      menu.put(5, "Select by Type");
      menu.put(6, "Select by Price");
    }
    @Override
    public void doAction(Scanner scanner, BudgetTrackerDao budgetTrackerDao) {
      int selectScannerNumInt = selectMenu(scanner, menu);
      if (selectScannerNumInt == 1) {
        System.out.println("Select All----------");
        for (BudgetTrackerDto btd : budgetTrackerDao.selectAll()) System.out.println(btd);
      } else {
        System.out.println("未実装");
      }
    }
    @Override
    public String toString() { return "Select"; }
  }

  private static class InputAction implements Action {
    private BudgetTrackerDtoFactory factory;
    InputAction(BudgetTrackerDtoFactory factory ) {
      this.factory = factory;
    }
    @Override
    public void doAction(Scanner scanner, BudgetTrackerDao budgetTrackerDao) {

      System.out.print("Input an ID: ");
      int id = scanner.nextInt();
      System.out.println(id);
      factory.setId(id);

      System.out.print("Input Date (yyyy-MM-dd): ");
      String str = scanner.next();
      try {
        Date date = (Date) new SimpleDateFormat("yyyy-MM-dd").parse(str);
        factory.setDate(date);
      } catch (ParseException e) {
        e.printStackTrace();
      }

      System.out.print("Input a store name: ");
      String storeName = scanner.next();
      factory.setStoreName(storeName);

      System.out.print("Input a product name: ");
      String productName = scanner.next();
      factory.setProductName(productName);

      System.out.print("Input a product type: ");
      String productType = scanner.next();
      factory.setProductType(productType);

      System.out.print("Input price: ");
      int price = scanner.nextInt();
      System.out.println(price);
      factory.setPrice(price);

      budgetTrackerDao.insertIntoTable(factory.create());
    }
    @Override
    public String toString() { return "Insert"; }
  }

  public static void main(String[] args) {
    BudgetTrackerDao budgetTrackerDao = new BudgetTrackerDao();
    BudgetTrackerDtoFactory factory = BudgetTrackerDtoFactory.getInstance();

    Scanner scanner = new Scanner(System.in);

    Map<Integer,Object> menu = new TreeMap<>();
    menu.put(1, new SelectAction());
    menu.put(2, new InputAction(factory));
    menu.put(3, "Update");
    menu.put(4, "Delete");
    menu.put(5, "Test");

    while(true) {
      int num = selectMenu(scanner, menu);
      Object obj = menu.get(num);
      System.out.println("You chose " + obj);

      if(obj instanceof Action) {
        ((Action)obj).doAction(scanner, budgetTrackerDao);
      } else {
        System.out.println("未実装");
      }
    }
  }
}