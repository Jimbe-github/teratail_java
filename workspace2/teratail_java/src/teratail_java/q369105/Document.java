package teratail_java.q369105;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;

//文章
class Document {
  class WordAndCount { //出力用
    final String word;
    final int count;
    WordAndCount(String word, int count) {
      this.word = word;
      this.count = count;
    }
    @Override
    public String toString() {
      return word + "(" + count + ")";
    }
  }

  private class WordInfo {
    int count;
    double termFrequency;
    void calcTermFrequency(int total) {
      termFrequency = (double)count / total;
    }
  }
  private Map<String, WordInfo> wordMap = new HashMap<>();

  Document(File sourceFile, Charset charset) throws IOException {
    parseFile(sourceFile, charset);
  }

  private void parseFile(File sourceFile, Charset charset) throws IOException {
    int total = 0;
    try(BufferedReader br = new BufferedReader(new FileReader(sourceFile, charset));) {
      for(String s; (s=br.readLine()) != null; ) {
        for (String word : s.split(" ")) {
          if (!word.isEmpty()) {
            WordInfo wi = wordMap.get(word);
            if(wi == null) wi = new WordInfo();
            wi.count ++;
            wordMap.put(word, wi);
            total ++;
          }
        }
      }
    }
    for (WordInfo wi : wordMap.values()) wi.calcTermFrequency(total);
  }

  Collection<String> getWords() {
    return Collections.unmodifiableCollection(wordMap.keySet());
  }

  //TF
  double getTermFrequency(String word) {
    if(!wordMap.containsKey(word)) return 0;
    return wordMap.get(word).termFrequency;
  }

  List<WordAndCount> getTermCountList() {
    List<WordAndCount> list = new ArrayList<>();
    for(Entry<String,WordInfo> e : wordMap.entrySet()) {
      list.add(new WordAndCount(e.getKey(), e.getValue().count));
    }
    Collections.sort(list, (o1,o2) -> o2.count - o1.count); //降順
    return list;
  }
}