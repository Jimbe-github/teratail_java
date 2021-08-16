package teratail_java.q354272;
//package pair_matching;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Cardclick_Listener extends MouseAdapter {
  private CardLabel card_one, card_two;

  @Override
  public void mouseClicked(MouseEvent e) {
    CardLabel card_label = (CardLabel)e.getSource();

    // カードの表裏チェック
    if (card_label.isOpen() == true) {
      return; // 裏面にしない
    }

    card_label.setOpen(true);

    if (card_one == null) {
      card_one = card_label;
    } else {
      card_two = card_label;
    }

    if (card_one.getNumber() == card_two.getNumber()) {
      ; // 一旦処理終了
    } else {
      // カードを裏面に戻す
      card_one.setOpen(false);
      card_one = null;
      card_two.setOpen(false);
      card_two = null;
    }
  }
}