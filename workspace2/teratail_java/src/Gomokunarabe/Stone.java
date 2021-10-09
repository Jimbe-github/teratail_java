package Gomokunarabe;

enum Stone {
  //空白
  EMPTY("＊"),
  //黒い石
  BLACK("●", "黒") { Stone nextTurn() { return WHITE; } },
  //白い石
  WHITE("〇", "白") { Stone nextTurn() { return BLACK; } };

  private String visual;
  private String name;
  private boolean empty;
  Stone(String visual) {
    this(visual, "", true);
  }
  Stone(String visual, String name) {
    this(visual, name, false);
  }
  private Stone(String visual, String name, boolean empty) {
    this.visual = visual;
    this.name = name;
    this.empty = empty;
  }
  String getName() {
    return name;
  }
  boolean isEmpty() {
    return empty;
  }
  Stone nextTurn() {
    return null;
  }
  public String toString() {
    return visual;
  }
}
