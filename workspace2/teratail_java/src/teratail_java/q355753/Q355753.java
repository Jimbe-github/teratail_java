package teratail_java.q355753;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Q355753 {

  public static void main(String[] args) throws IOException {

//    File folder = new File("I:\\test");
//    File file = new File("I:\\test\\zzz.txt");
//    System.out.println(file.exists());
//    if(!file.exists()) {
//      folder.mkdirs();
//      file.createNewFile();
//    }
//    try (FileOutputStream fileOutputStream = new FileOutputStream(file);) {
//
//      fileOutputStream.write("12345".getBytes());
//    }
    List<String[]> list = new ArrayList<String[]>();

    String[] a = { "1", "Monday" };
    String[] b = { "3", "Wednesday" };
    String[] c = { "2", "Tuesday" };

    list.add(a);
    list.add(b);
    list.add(c);


    Collections.sort(list, new Comparator<String[]>(){
      public int compare(String[] a,  String[] b) {
        return Integer.parseInt(a[0]) - Integer.parseInt(b[0]);
      }
    });

    for (String[] str : list) {
      System.out.println(str[0] + str[1]);
    }

    //    for (String FilePath : FilePaths) {
//      File folder = new File(FilePath);
//      File file = new File(FilePath + "\\" + "12345" + ".wav");
//      try (FileOutputStream fileOutputStream = new FileOutputStream(file);) {
//        if (!file.exists()) {
//          folder.mkdirs();
//          file.createNewFile();
//        }
//        fileOutputStream.write(bytes);
//      } catch (Exception e) {
//        e.printStackTrace();
//      }
//    }
  }
}
