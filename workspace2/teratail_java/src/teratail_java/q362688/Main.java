package teratail_java.q362688;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {
  public static void main(String[] args) throws IOException {
    PrefecturesFile file = new PrefecturesFile("revenge.txt");
    //file.printAll();

    try(Scanner sc = new Scanner(System.in);) {
      while(true) {
        String line = sc.next();
        Prefecture pref = file.getPrefecture(line);
        if(pref == null) break;
        System.out.println(pref);
      }
    }
  }
}

class PrefecturesFile {
  private Map<String,Prefecture> map = new HashMap<>();

  PrefecturesFile(String filename) throws IOException {
    Path readPath = Paths.get(filename);

    try(Scanner file = new Scanner(readPath);) {
      for(int lineno=1; file.hasNextLine(); lineno++) {
        String line = file.nextLine();
        String[] array = line.split(",");
        if(array.length != 3) throw new IOException(filename+":"+lineno+": データエラー");

        try {
          String name = array[0];
          double area = Double.parseDouble(array[1]);
          double population = Double.parseDouble(array[2]);
          map.put(name, new Prefecture(name, area, population));
        } catch(Exception e) {
          throw new IOException(filename+":"+lineno+": データエラー", e);
        }
      }
    }
  }

  void printAll() {
    for(Prefecture p : map.values()) System.out.println(p);
  }

  Prefecture getPrefecture(String name) {
    return map.get(name);
  }
}

class Prefecture {
  final String name;
  final double area;
  final double population;
  final double populationDensity;

  Prefecture(String name, double area, double population) {
    if(name == null || name.isEmpty() || area <= 0 || population <= 0) throw new IllegalArgumentException();

    this.name = name;
    this.area = area;
    this.population = population;
    populationDensity = population / area;
  }

  @Override
  public String toString() {
    return String.format("%s : %.0f : %.0f : %.0f", name, area, population, populationDensity);
  }
}
