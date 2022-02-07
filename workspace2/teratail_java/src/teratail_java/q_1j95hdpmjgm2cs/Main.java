package teratail_java.q_1j95hdpmjgm2cs;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

class Information {
  static final String NULL_NUMBER_STRING = "未入力";

  final String code;
  final String name;
  final BigDecimal number;

  Information(String code, String name, String number) {
    this.code = code;
    this.name = name;
    this.number = toBigDecimal(number);
  }

  private BigDecimal toBigDecimal(String number) {
    if(number == null) return null;
    try {
      return new BigDecimal(number);
    } catch(NumberFormatException ignore) {
      //無視
    }
    return null;
  }

  String getNumberString() {
    return number == null ? NULL_NUMBER_STRING : ""+number;
  }

  @Override
  public String toString() {
    return "code="+code+", name="+name+", number="+getNumberString();
  }
}

class StockData {
  private String filename;
  private String charsetName;
  private Map<String, Information> map = new HashMap<>();

  StockData(String filename, String charsetName) throws IOException {
    this.filename = filename;
    this.charsetName = charsetName;
    loadFile();
  }

  Set<Information> getValues() {
    return new HashSet<Information>(map.values());
  }

  void put(Information info) throws IOException {
    map.put(info.code, info);
    saveFile();
  }

  void showAll() {
    for(Information info : map.values()) {
      System.out.println(info);
    }
  }

  private void loadFile() throws IOException {
    map.clear();
    try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), charsetName))) {
      for(String line; (line = br.readLine()) != null; ) {
        String[] st = line.split(",", -1);
        if(st.length == 3) {
          Information information = new Information(st[0], st[1], st[2]);
          map.put(information.code, information);
        }
      }
    } catch(FileNotFoundException ignore) {
      //無視
    }
  }

  private void saveFile() throws IOException {
    try(PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filename), charsetName))) {
      for(Information info : map.values()) {
        pw.println(info.code+","+info.name+","+info.getNumberString());
      }
    }
  }
}

public class Main {
  public static void main(String[] args) throws IOException {

    StockData stockData = new StockData("StockData.csv", "UTF-8");

    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    for(boolean end=false; !end; ) {
      try {
        System.out.println();
        System.out.println("<<メニュー>>");
        System.out.print("1:code,name入力\n2:number入力\n3:表示\n4:終了\n>");
        int choice = Integer.parseInt(input.readLine());
        System.out.println();
        switch(choice) {
        case 1:
          enter(stockData, input);
          break;

        case 2:
          input(stockData, input);
          break;

        case 3:
          stockData.showAll();
          break;

        case 4:
          end = true;
          break;

        default:
          System.out.println("指定された値を入力してください。");
        }
      } catch(NumberFormatException e) {
        System.out.println("数字を入力してください。");
      } catch(IOException e) {
        e.printStackTrace();
      }
    }
  }

  static void enter(StockData stockData, BufferedReader input) throws IOException {
    System.out.print("codeを入力してください\n>");
    String code = input.readLine();
    System.out.print("nameを入力してください\n>");
    String name = input.readLine();

    stockData.put(new Information(code, name, null));
  }

  static void input(StockData stockData, BufferedReader input) throws IOException {
    for(Information info : stockData.getValues()) {
      System.out.println("code：" + info.code);
      System.out.println("name：" + info.name);
      System.out.print("■numberを入力してください\n>");
      String number = input.readLine();

      stockData.put(new Information(info.code, info.name, number));
    }
  }
}
