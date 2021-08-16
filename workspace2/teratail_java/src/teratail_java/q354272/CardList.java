package teratail_java.q354272;
//package pair_matching;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

//~ 中略 ~

public class CardList extends JPanel{
  private ArrayList<CardLabel> cards = new ArrayList<>();

  enum Suit {
    Club, Diamond, Heart, Spade;
    static int size() { return values().length; }
    static Suit get(int i) { return Suit.values()[i]; }
    String getName() { return name().toLowerCase(); }
  }
  static final int NUM_MAX = 13;

  public CardList() {
    super(new GridLayout(Suit.size(), NUM_MAX));
    setPreferredSize(new Dimension(1150, 550));

    Cardclick_Listener listener = new Cardclick_Listener();
    Icon card_back = new ImageIcon("image/card_back.png"); // 裏面

    for(int i = 0; i < Suit.size()*NUM_MAX; i++) {
      int num = i%NUM_MAX + 1;
      Icon card_front = createIcon(Suit.get(i/NUM_MAX), num); // 表面
      CardLabel label = new CardLabel(card_front, card_back, num);
      label.addMouseListener(listener);
      cards.add(label);
    }

    Collections.shuffle(cards);
    for(CardLabel card : cards) add(card);
  }

  private ImageIcon createIcon(Suit suit, int num) {
    String filename = String.format("image/card_%s_%02d.png", suit.getName(), num);
    return new ImageIcon(filename);
  }
}