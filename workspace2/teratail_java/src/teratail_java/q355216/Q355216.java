package teratail_java.q355216;

import java.io.File;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Q355216 {

  public static void main(String[] args) {
    File target = new File("c:\\windows");
    File[] list = target.listFiles();

    Arrays.sort(list, (a,b)->{
      if(a.isDirectory() && !b.isDirectory()) return -1;
      if(!a.isDirectory() && b.isDirectory()) return 1;
      if(a.isFile() && b.isFile()) {
        int ext = getExtension(a).compareTo(getExtension(b));
        if(ext != 0) return ext;
      }
      return a.getName().compareTo(b.getName());
    });

    for(File file : list) System.out.println(file.toString()+(file.isDirectory()?"\\":""));
  }
  private static Pattern extensionPattern = Pattern.compile(".*\\.([0-9a-zA-Z]{1,3})$");
  private static String getExtension(File file) {
    Matcher matcher = extensionPattern.matcher(file.getName());
    return matcher.matches() ? matcher.group(1) : "";
  }
}
