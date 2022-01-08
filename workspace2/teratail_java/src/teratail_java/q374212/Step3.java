package teratail_java.q374212;

import java.util.*;
import java.util.regex.Pattern;


class FormulaBinaryTree {
  public interface Node {
    public static enum Priority {
      LOW, HIGH, NUMBER;
      boolean over(Priority other) { return ordinal() > other.ordinal(); }
    }
    String getWord();
    boolean isNumber();
    Priority getPriority();
  }
  private static class MutableNode implements Node {
    private static final Pattern NUM_PATTERN = Pattern.compile("[0123456789]+");

    final String word;
    MutableNode right, left;
    final boolean isNumber;
    private Priority priority;

    MutableNode(String word) {
      this.word = word;
      this.left = null;
      this.right = null;
      isNumber = NUM_PATTERN.matcher(word).matches();
      priority = isNumber ? Priority.NUMBER
              : word.equals("*") || word.equals("/") ? Priority.HIGH
              : Priority.LOW;
    }

    @Override
    public String getWord() { return word; }
    @Override
    public boolean isNumber() { return isNumber; }
    @Override
    public Priority getPriority() { return priority; }

    void setPriority(Priority priority) { this.priority = priority; }
    /**
     * 自分のほうが優先順位が高いと true
     */
    boolean isPrioritize(MutableNode other) { return priority.over(other.priority); }

    void print() { print(""); }
    void print(String indent) {
      System.out.println("'"+word+"'");
      printLink(indent, " R:", right);
      printLink(indent, " L:", left);
    }
    private void printLink(String indent, String label, MutableNode link) {
      System.out.print(indent+label);
      if(link == null) System.out.println("null");
      else link.print(indent+"  ");
    }
  }

  private MutableNode root = null;

  FormulaBinaryTree(String[] tokens) {
    Deque<MutableNode> stack = new LinkedList<>(); //null が入れられる必要がある為 ArrayDeque は不可
    for(String token : tokens) {
      if(doBrackets(token, stack)) continue;

      MutableNode node = new MutableNode(token);
      if(node.isNumber) {
        root = setNumberNode(root, node);
      } else if(node.isPrioritize(root)) { //演算子優先順位による入れ替え
        node.left = root.right;
        root.right = node;
      } else {
        node.left = root;
        root = node;
      }
    }
  }

  private MutableNode setNumberNode(MutableNode root, MutableNode node) {
    if(root == null) return node; //(1つ目は数字のはず)
    //右端へ
    MutableNode t = root;
    while(t.right != null) t = t.right;
    t.right = node;
    return root;
  }
  /**
   * 括弧処理
   * こっそり括弧内を別ツリーとして生成を続けさせ、閉じ括弧で元のツリーに合成する
   */
  private boolean doBrackets(String token, Deque<MutableNode> stack) {
    if(token.equals("(")) {
      stack.push(root);
      root = null;
      return true;
    }
    if(token.equals(")")) {
      root.setPriority(MutableNode.Priority.NUMBER); //括弧部分は数値の優先度
      root = setNumberNode(stack.pop(), root);
      return true;
    }
    return false;
  }

  Node[] getPostOrder() {
    
    return null;
  }
  
  //構造確認用
  void print() {
    System.out.print("root:");
    root.print();
  }
}

public class Step3 {
  public static void main(String[] args){
    String[] textm = input();//命令文入力

    //中置記法を二分木にする
    FormulaBinaryTree fbt = new FormulaBinaryTree(textm);

    //確認
    fbt.print();
  }

  private static String[] input() {
    System.out.print("命令文入力:");
    try(Scanner scan = new Scanner(System.in);) { //標準入力読み込み
      String line = scan.nextLine();
      return line.split(" ");//空白区切り
    }
  }
}
