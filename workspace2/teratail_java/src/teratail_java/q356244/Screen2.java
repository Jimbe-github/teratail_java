package teratail_java.q356244;
//package screen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Screen2 extends JFrame {
  int i = 1;

  public static void main(String args[]) {
    Screen2 frame = new Screen2();
    frame.setVisible(true);
  }

  public Screen2() {
    setTitle("タイトル");
    setSize(1450, 900);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    JPanel p = new JPanel();
    add(p);
    p.setLayout(null);

    //C
    JButton btn_C = new JButton("C");
    btn_C.addActionListener(new Code(new Code.Callback() {
      @Override
      public void started() {
        i ++; //変数iの変更
      }
    }));
    btn_C.setBounds(300, 600, 100, 150);
    p.add(btn_C);
  }
}

class Code implements ActionListener {
  interface Callback {
    void started();
  }
  private Callback callback;
  Code(Callback callback) {
    this.callback = callback;
  }

  //回答箇所
  public void actionPerformed(ActionEvent ae) {
    try {
      //音を鳴らす
      File soundFile = new File("/*音声ファイル*/");
      AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
      AudioFormat audioFormat = audioInputStream.getFormat();
      DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);
      Clip clip = (Clip) AudioSystem.getLine(info);
      clip.open(audioInputStream);
      clip.start();
      if(callback != null) callback.started();
    } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
      e.printStackTrace();
    }
  }
}