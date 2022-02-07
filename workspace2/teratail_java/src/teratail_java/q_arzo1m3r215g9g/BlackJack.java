package teratail_java.q_arzo1m3r215g9g;

import java.util.*;
import java.util.function.UnaryOperator;

class Card {
  private static final String[] RANK_STRINGS = { "A","2","3","4","5","6","7","8","9","10","J","Q","K" };
  private static final String[] SUIT_STRINGS = { "クラブ","ダイヤ","ハート","スペード" };

  final int rank;
  final int suit;
  Card(int num) {
    if(num < 1 || 52 < num) throw new IllegalArgumentException();
    this.rank = (num - 1) % 13;
    this.suit = (num - 1) / 13;
  }

  int getPoint() {
    return rank >= 10 ? 10 : rank+1;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(SUIT_STRINGS[suit]).append("の").append(RANK_STRINGS[rank]);
    return sb.toString();
  }
}

class Deck {
  private List<Card> deck;
  Deck() {
    deck = shuffledDeck();
  }

  Card get() {
    return deck.remove(0);
  }

  private List<Card> shuffledDeck() {
    List<Card> deck = new ArrayList<>(52);
    for(int i=1; i<=52; i++) deck.add(new Card(i));
    Collections.shuffle(deck);
    return deck;
  }
}

class Player {
  interface Drawer {
    boolean draw(Player player, Deck deck);
  }

  final String name;
  private Drawer drawer;
  private List<Card> cardList = new ArrayList<>();

  Player(String name, Drawer drawer) {
    this.name = name;
    this.drawer = drawer;
  }

  void add(Card card) {
    add(card, false);
  }
  void add(Card card, boolean secret) {
    cardList.add(card);
    System.out.println(name+"の"+cardList.size()+"枚目のカードは" + (secret ? "秘密です" : ""+card));
  }

  boolean draw(Deck deck) {
    return drawer.draw(this, deck);
  }

  int getPoint() {
    int sum = 0;
    for(Card card : cardList) {
      sum += card.getPoint();
    }
    return sum;
  }

  boolean isBusted() {
    return getPoint() > 21;
  }

  void openHands() {
    System.out.println(name + "の手札を表示します。" );
    for(int i=0; i<cardList.size(); i++) {
      System.out.println(name + "の" + (i + 1) + "枚目のカードは" + cardList.get(i) + "です。" );
    }
  }
}

public class BlackJack {
  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);

    System.out.println("ゲームを開始します");

    do {
      new BlackJack(scan).play();
    } while(inputYesNo(scan, "もう一度遊びますか？",()->System.out.println("ゲームを再開します"), null));

    System.out.println("ゲームを終了します");
  }

  @FunctionalInterface
  interface Action { void action(); }

  private static boolean inputYesNo(Scanner scan, String prePrompt, Action yesAction, UnaryOperator<String> errString) {
    String str;
    while(true) {
      System.out.print(prePrompt + "Yes:y or No:n");
      str = scan.next();
      if(str.length() == 1 && "yn".indexOf(str) >= 0) break;
      System.out.println((errString != null ? errString.apply(str) : "") + "y か n を入力してください。");
    }
    boolean result = "y".equals(str);
    if(result && yesAction != null) yesAction.action();
    return result;
  }

  private Scanner scan;

  BlackJack(Scanner scan) {
    this.scan = scan;
  }

  private void play() {

    Player player = new Player("あなた", (p, deck) -> {
      while(inputYesNo(scan, "カードを引きますか？", null, (str)->p.name+"の入力は"+str+" です。")) {
        p.add(deck.get());
        System.out.println("現在の合計は" + p.getPoint());
        if(p.isBusted()) return true;
      }
      return false;
    });

    Player dealer = new Player("ディーラー", (p, deck) -> {
      while(p.getPoint() < 17) p.add(deck.get());
      return p.isBusted();
    });

    Deck deck = new Deck();

    player.add(deck.get());
    dealer.add(deck.get());
    player.add(deck.get());
    dealer.add(deck.get(), true);
    System.out.println("あなたの現在のポイントは" + player.getPoint() + "です。" );

    if(player.draw(deck)) {
      System.out.println("残念、バーストしてしまいました。");
      dealer.openHands();
      System.out.println("ディーラーのポイントは"+ dealer.getPoint());
      return;
    }

    if(dealer.draw(deck)) {
      System.out.println("ディーラーがバーストしました。あなたの勝ちです！");
      dealer.openHands();
      System.out.println("ディーラーのポイントは"+ dealer.getPoint());
      return;
    }

    System.out.println("あなたのポイントは" + player.getPoint());
    System.out.println("ディーラーのポイントは"+ dealer.getPoint());

    if(player.getPoint() == dealer.getPoint()) {
      System.out.println("引き分けです。");
    } else if(player.getPoint() > dealer.getPoint()) {
      System.out.println("勝ちました！");
    } else {
      System.out.println("負けました・・・");
    }
    dealer.openHands();
  }
}