package teratail_java.q353111;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class Grade implements Comparable<Grade> {
  private int id;
  private double value;
  private int assignmentsTotalScore;
  private double attendanceRate;

  Grade(int id, double value) {
    this.id = id;
    this.value = value;
  }

  //ランク
  String getRank() {
    double record = getRecord();
    if(record >= 90) return "秀";
    if(record >= 80) return "優";
    if(record >= 70) return "良";
    if(record >= 60) return "可";
    if(record >   0) return "不可";
    if(value == 0.0) return "K";
    return " ";
  }
  //レコード?
  double getRecord() {
    return (70 * value) / 100 +
        (25 * assignmentsTotalScore) / 60 +
        (5 * attendanceRate);
  }
  //課題合計点
  void setAssignmentsTotalScore(int score) {
    assignmentsTotalScore = score;
  }
  //出席率
  void setAttendanceRate(double rate) {
    attendanceRate = rate;
  }

  public void print() {
    System.out.printf("%d, %.3f, %d, %.0f, %s%n", id, value,
        assignmentsTotalScore, attendanceRate, getRank());
  }

  @Override
  public int compareTo(Grade o) {
    if(o == null) throw new NullPointerException();
    return id - o.id;
  }
}

class CSVFile {
  interface LineProcessor {
    void process(String[] tokens);
  }

  static void read(String filename, LineProcessor lp) throws IOException {
    try(FileReader reader = new FileReader(new File(filename));
        BufferedReader br = new BufferedReader(reader);) {
      int num = 1;
      for(String line; (line=br.readLine()) != null; num++) {
        String[] tokens = line.split(",");
        try {
          lp.process(tokens);
        } catch(Exception e) {
          throw new IOException("Format Error. "+filename+":"+num, e);
        }
      }
    }
  }
}

public class GradeChecker2 {
  private Map<Integer,Grade> gradeMap = new HashMap<Integer,Grade>();

  GradeChecker2(String filename) throws IOException {
    CSVFile.read(filename, tokens -> {
      int id = Integer.valueOf(tokens[0]);
      Double value = Double.valueOf(tokens[1]);
      gradeMap.put(id, new Grade(id, value));
    });
  }

  //課題の点数
  //id,課題1,課題2,…,課題6
  void readAssignments(String filename) throws IOException {
    CSVFile.read(filename, tokens -> {
      int id = Integer.valueOf(tokens[0]);
      int total = 0;
      for(int i=1; i<tokens.length; i++) {
        if(!tokens[i].isBlank()) total += Integer.valueOf(tokens[i]);
      }
      getGrade(id).setAssignmentsTotalScore(total);
    });
  }

  //出席状況？
  //id,出席1,出席2,…,出席14
  void readMiniexam(String filename) throws IOException {
    CSVFile.read(filename, tokens -> {
      int id = Integer.valueOf(tokens[0]);
      int count = 0;
      for(int i=1; i<tokens.length; i++) {
        if(!tokens[i].isBlank()) count ++; //空でなければ出席
      }
      getGrade(id).setAttendanceRate((double)count / tokens.length);
    });
  }

  void printAll() {
    gradeMap.values().forEach(grade -> grade.print());
  }

  private Grade getGrade(int id) {
    Grade grade = gradeMap.get(id);
    if(grade == null) {
      grade = new Grade(id, 0.0);
      gradeMap.put(id, grade);
    }
    return grade;
  }

  public static void main(String[] args) throws IOException {
    GradeChecker2 gc = new GradeChecker2(args[0]);
    gc.readAssignments(args[1]);
    gc.readMiniexam(args[2]);
    gc.printAll();
  }
}