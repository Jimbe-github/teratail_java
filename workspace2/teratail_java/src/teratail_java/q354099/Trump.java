package teratail_java.q354099;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Trump {
  static ImageIcon closeImage = new ImageIcon("card_back.png"); //裏イメージ

  enum Suit {
    Club, Diamond, Heart, Spade;
    static int size() { return values().length; }
    static Suit get(int i) { return Suit.values()[i]; }
    String getName() { return name().toLowerCase(); }
  }
  enum State {
    OPEN, //表
    CLOSE; //裏
  }

  private int number;
  //private String name;
  private State state = State.CLOSE;
  private ImageIcon openImage; //表イメージ

  Trump(Suit suit, int number) {
    this.number = number;
    openImage = new ImageIcon(String.format("card_%s_%02d.png",suit.getName(),number));
  }

  State getState() {
    return state;
  }
  int getNumber() {
    return number;
  }
  Icon getIcon() {
    return state == State.OPEN ? openImage : closeImage;
  }

  void open() {
    state = State.OPEN;
  }
  void close() {
    state = State.CLOSE;
  }
}