package teratail_java.q369105;

import java.util.*;

//文章の集まり
class DocBinder {
  class WordAndTFIDF { //出力用
    final String word;
    final double tfidf;
    WordAndTFIDF(String word, double tfidf) {
      this.word = word;
      this.tfidf = tfidf;
    }
    @Override
    public String toString() {
      return word + "(" + tfidf + ")";
    }
  }
  private Map<String, Document> documents = new HashMap<>();
  private Map<String, Integer> documentsWithWord = new HashMap<>();

  void add(String name, Document doc) {
    if(name == null || doc == null) throw new NullPointerException();
    if(documents.containsKey(name) || documents.containsValue(doc)) return;

    documents.put(name, doc);
    for(String word : doc.getWords()) {
      documentsWithWord.put(word, documentsWithWord.getOrDefault(word, 0) + 1);
    }
  }

  Document getDocument(String name) { return documents.get(name); }

  //IDF
  double getInverseDocumentFrequency(String word) {
    if(!documentsWithWord.containsKey(word)) return 0;
    return Math.log(documents.size() / documentsWithWord.get(word));
  }

  List<WordAndTFIDF> getTFIDFList(String name) {
    Document doc = documents.get(name);
    if(doc == null) throw new IllegalArgumentException("name");

    List<WordAndTFIDF> list = new ArrayList<>();
    for (String word : doc.getWords()) {
      double tf = doc.getTermFrequency(word);
      double idf = getInverseDocumentFrequency(word);
      double tfidf = Math.round(tf * idf * 1000) / 1000.0;
      list.add(new WordAndTFIDF(word, tfidf));
    }
    Collections.sort(list, (o1,o2) -> (int)Math.signum(o2.tfidf - o1.tfidf)); //降順
    return list;
  }
}