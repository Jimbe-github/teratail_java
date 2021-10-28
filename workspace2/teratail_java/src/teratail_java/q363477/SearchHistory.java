package teratail_java.q363477;

import java.util.StringJoiner;

public class SearchHistory {
  private String[] array; // 0 が最後に入れたモノ

  public SearchHistory(int maxSize) {
    array = new String[maxSize];
  }

  /**
   * word を追加する。null,空文字の場合は追加しない。<br>
   * すでに入っていた文字は入れ直す。<br>
   * maxSize を超えた分古いデータは削除
   * @param word 文字列
   * @return 追加した場合 true
   */
  public boolean add(String word) {
    if(word == null || word.isEmpty()) return false;

    int i = indexOf(word);
    if(i < 0) i = array.length-1;
    for(int j=i; j>0; j--) array[j] = array[j-1]; // 0 を空けるようにずらす(ついでに要らないのを消す)
    array[0] = word;
    return true;
  }

  private int indexOf(String word) {
    for(int i=0; i<size(); i++) {
      if(array[i].equals(word)) return i;
    }
    return -1;
  }

  public int size() {
    int i = 0;
    while(i < array.length && array[i] != null) i++;
    return i;
  }

  public String get(int index) {
    if(index < 0 || index >= size()) return null;
    return array[index];
  }

  public void clear() {
    for(int i=0; i<array.length; i++) array[i] = null;
  }

  @Override
  public String toString() {
    StringJoiner sj = new StringJoiner(",", "[", "]");
    for(int i=0; i<size(); i++) sj.add(get(i));
    return sj.toString();
  }

  public static void main(String[] args) {
    SearchHistory history = new SearchHistory(3);
    String[] orders = new String[]{"Java", "Python", "", "", "JavaScript", "Go", "Swift", "Go", "Rust"};
    for (String s : orders) {
      boolean added = history.add(s);
      System.out.println("add '" + s + "'="+added);
      System.out.println(" => " + history);
    }
  }
}