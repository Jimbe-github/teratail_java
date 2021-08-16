package teratail_java.q354272;

import javax.swing.JFrame;

public class MainFrame extends JFrame {

  public static void main(String[] args) {
    new MainFrame().setVisible(true);
  }

  MainFrame() {
    super();
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    add(new CardList());
    pack();
  }
}
