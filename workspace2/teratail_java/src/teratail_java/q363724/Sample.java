package teratail_java.q363724;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class Sample {
  public static void main(String[] args) throws IOException {
    Sample sample = new Sample("名前","国語","外国語","数学");
    File file = new File("Sample1.txt");
    try(BufferedReader br = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8); ) {
      for(String line; (line=br.readLine()) != null; ) {
        String[] array = line.split(",");
        sample.put(array[0], array[1], Integer.parseInt(array[2]));
      }
      sample.drawSheet();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private List<String> headerList;
  private int nameIndex;
  private int[] maxWidth;
  private Map<String,Student> studentMap = new HashMap<>();

  private static class Student {
    final int number;
    final String name;
    private final Map<String,Integer> scoreMap = new HashMap<>();
    Student(int number, String name) {
      this.number = number;
      this.name = name;
    }
    void putScore(String subject, int score) {
      scoreMap.put(subject, score);
    }
    boolean hasScore(String subject) {
      return scoreMap.containsKey(subject);
    }
    int getScore(String subject) {
      return scoreMap.get(subject);
    }
  }

  Sample(String... header) {
    headerList = Arrays.asList(header);

    nameIndex = headerList.indexOf("名前");
    if(nameIndex < 0) throw new IllegalArgumentException("'名前' がありません");

    maxWidth = new int[headerList.size()];
    for(int i=0; i<headerList.size(); i++) {
      maxWidth[i] = count(headerList.get(i));
    }
  }

  void put(String name, String subject, int score) {
    Student s = studentMap.get(name);
    if(s == null) {
      s = new Student(studentMap.size()+1, name); //number は出現順にしておく
      studentMap.put(name, s);
      maxWidth[nameIndex] = Math.max(maxWidth[nameIndex], count(s.name));
    }
    s.putScore(subject, score);
    int i = headerList.indexOf(subject);
    if(i >= 0 && i != nameIndex) maxWidth[i] = Math.max(maxWidth[i], count(""+score));
  }

  void drawSheet() {
    drawHeader();
    drawRowSeparator();
    studentMap.values().stream()
      .sorted(Comparator.comparingInt(s -> s.number)) //number順に並び替え
      .forEach(s -> { drawStudent(s); drawRowSeparator(); });
  }

  private void drawHeader() {
    StringJoiner sj = new StringJoiner("|","|","|");
    for(int i=0; i<headerList.size(); i++) {
      sj.add(getCenteringString(headerList.get(i), maxWidth[i]));
    }
    System.out.println(sj.toString());
  }

  private String getCenteringString(String str, int width) {
    int space = width - count(str);
    int pre = space / 2;
    int post = space - pre;
    return " ".repeat(pre)+str+" ".repeat(post);
  }

  private String rowSeparator;
  private void drawRowSeparator() {
    if(rowSeparator == null) {
      StringJoiner sj = new StringJoiner("-","+","+");
      for(int i=0; i<maxWidth.length; i++) {
        sj.add("-".repeat(maxWidth[i]));
      }
      rowSeparator = sj.toString();
    }
    System.out.println(rowSeparator);
  }

  private void drawStudent(Student s) {
    StringJoiner sj = new StringJoiner("|","|","|");
    for(int i=0; i<maxWidth.length; i++) {
      if(i == nameIndex) {
        sj.add(s.name + " ".repeat(maxWidth[i]-count(s.name))); //名前=左詰め
      } else if(s.hasScore(headerList.get(i))) {
        sj.add(String.format("%"+maxWidth[i]+"d", s.getScore(headerList.get(i)))); //得点=右詰め
      } else {
        sj.add(" ".repeat(maxWidth[i]));
      }
    }
    System.out.println(sj.toString());
  }

  //英数字、カタカナ、ひらがな、漢字の文字コードを分類する
  public static int count(String str) {
    int count = 0;
    for(char c : str.toCharArray()) {
      count += Character.UnicodeBlock.of(c) == Character.UnicodeBlock.BASIC_LATIN ? 1 : 2;
    }
    return count;
  }
}