package teratail_java.q354099;

import javax.swing.JFrame;

public class NEFrame extends JFrame {
  /**
   * ここからはじまります。*/
  public static void main(String[] args) {
    // フレームを表示
    new NEFrame().setVisible(true);
  }

  /**
   * コンストラクタ
   */
  public NEFrame() {

    // フレームを生成
    super("neurasthenia");

    // ×ボタンが押されたら、終了する
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // パネルを生成・フレームにパネルを設定
    setContentPane(new NEPanel());

    // サイズを最適化する
    pack();
  }
}