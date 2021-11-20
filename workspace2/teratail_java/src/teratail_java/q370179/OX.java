package teratail_java.q370179;

import java.awt.BorderLayout;

import javax.swing.*;

public class OX extends JFrame {
  public static void main(String[] args) {
    new OX().setVisible(true);
  }

  private Cell turn;
  private OXmodel model;
  private OXview view;

  OX() {
    super("OX");
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    JPanel backpanel = new JPanel(new BorderLayout());
    backpanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    add(backpanel);

    model = new OXmodel();
    view = new OXview(model);
    view.setCellClickListener((x,y) -> advanceTurn(x,y));

    backpanel.add(view);

    pack();
  }

  private void advanceTurn(int x, int y) {
    if(view.inGame()) { //ゲーム中
      if(model.get(x,y) != Cell.SPACE) return;
      Cell winner = model.set(x,y,turn);
      if(winner == null) {
        turn = turn.next();
      } else {
        view.end(winner);
      }
    } else { //ゲーム開始前 or 決着後
      model.clear();
      turn = Cell.CIRCLE;
      view.start();
    }
  }
}