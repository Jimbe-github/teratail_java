package teratail_java.q354272;
//package pair_matching;

import javax.swing.Icon;
import javax.swing.JLabel;

public class CardLabel extends JLabel{
  private int number;
  private boolean open = false;
  private Icon open_icon;
  private Icon reverse_icon;

  // コンストラクタの作成
  public CardLabel(Icon open_icon, Icon revers_icon, int number) {
    this.open_icon = open_icon;
    this.reverse_icon = revers_icon;
    this.number = number;

    setText("["+number+"]");
    // 裏面のセット
    //setIcon(revers_icon);
  }

  public int getNumber() {
    return number;
  }

  public boolean isOpen() {
    return open;
  }

  public void setOpen(boolean open) {
    if(open == true) {
      //this.setIcon(open_icon);
      setText("-"+number+"-");
    }else {
      //this.setIcon(reverse_icon);
      setText("["+number+"]");
    }
    this.open = open;
  }
}