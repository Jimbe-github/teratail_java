package teratail_java.q375746;

import java.io.IOException;
import java.util.*;

public class Kanri {
  private static ManagedObjectFactory<?>[] FACTORIES = new ManagedObjectFactory[]{
      new CompactDisc.Factory(),
      new Magazine.Factory(),
      //new Video.Factory(),
      //new Book.Factory(),
  };

  public static void main(String[] args) throws IOException {
    ArrayList<ManagedObject> stock = new ArrayList<>();

    Scanner scanner = new Scanner(System.in);
    for(int num=0; num!=3; ) {
      System.out.println("登録:1 一覧:2 終了:3");
      switch(num = Integer.parseInt(scanner.nextLine())) {
      case 1: regist(stock, scanner); break;
      case 2: printAll(stock); break;
      }
    }
    System.out.println("終了.");
  }

  private static void regist(ArrayList<ManagedObject> stock, Scanner scanner) {
    //選択肢表示
    StringJoiner sj = new StringJoiner(" ");
    for(int i=0; i<FACTORIES.length; i++) sj.add(FACTORIES[i].getTypeName() + ":" + (i+1));
    System.out.println(sj);
    //選択
    int bunrui = Integer.parseInt(scanner.nextLine());
    if(bunrui < 1 || FACTORIES.length < bunrui) {
      System.out.println("未実装");
      return;
    }
    //生成・登録
    ManagedObject obj = createManagedObject(FACTORIES[bunrui-1], scanner);
    stock.add(obj);
    System.out.println(obj.toString("\n"));
  }

  private static ManagedObject createManagedObject(ManagedObjectFactory<?> factory, Scanner scanner) {
    //プロパティ入力
    for(String propertyName : factory.getPropertyNames()) {
      System.out.println(propertyName + ":");
      factory.setProperty(propertyName, scanner.nextLine());
    }
    //生成
    return factory.create();
  }

  private static void printAll(ArrayList<ManagedObject> stock) {
    if(stock.isEmpty()) {
      System.out.println("データがありません");
    } else {
      for(ManagedObject obj : stock) System.out.println(obj.toString("\n"));
    }
  }
}

//管理対象物
interface ManagedObject {
  String toString(CharSequence delimiter);
}
//管理対象物生成
abstract class ManagedObjectFactory<E extends ManagedObject> {
  private final String typeName;
  private final String[] propertyNames;
  private final Map<String,String> propertyValueMap = new HashMap<>();

  ManagedObjectFactory(String typeName, String[] propertyNames) {
    this.typeName = typeName;
    this.propertyNames = propertyNames;
  }
  String getTypeName() { return typeName; }
  String[] getPropertyNames() { return Arrays.copyOf(propertyNames, propertyNames.length); }
  void setProperty(String name, String value) { propertyValueMap.put(name, value); }
  String getProperty(int i) { return propertyValueMap.get(propertyNames[i]); }

  abstract E create();
}