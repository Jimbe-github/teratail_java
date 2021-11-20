package teratail_java.q369105;
//package p7;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class WordCount {
  public static void main(String[] args) throws IOException {
    if (args.length == 3) {
      String type = args[0];
      String inputDirPath = args[1];
      String sourceName = args[2];
      Charset charset = Charset.forName("Shift-JIS");//Charset.defaultCharset();

      switch (type) {
      case "tfidf":
        DocBinder binder = new DocBinder();
        for (File file : new File(inputDirPath).listFiles()) {
          binder.add(file.getName(), new Document(file, charset));
        }
        List<DocBinder.WordAndTFIDF> tfidfList = binder.getTFIDFList(sourceName);
        writeList(tfidfList, 5, "Top 5 words in TF-IDF", new File(type + "_" + sourceName), charset);
        return;
      case "freq":
        Document doc = new Document(new File(inputDirPath, sourceName), charset);
        List<Document.WordAndCount> termList = doc.getTermCountList();
        writeList(termList, 5, "Top 5 words in frequency", new File(type + "_" + sourceName), charset);
        return;
      }
    }
    System.err.println("Usage: java WordCount type input-dir-path source-name");
    System.err.println("type=test1|test2|test3|test4|test5|freq|tfidf");
    System.exit(-1);
  }

  static void writeList(List<? extends Object> list, int n, String header, File outputFile, Charset charset) {
    try(PrintWriter pw = new PrintWriter(new FileWriter(outputFile, charset));) {
      pw.println(header);
      for (int i=0; i<n && i<list.size(); i++) {
        pw.println((i + 1) + ":" + list.get(i));
      }
      pw.println();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
