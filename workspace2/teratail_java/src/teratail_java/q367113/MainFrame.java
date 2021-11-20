package teratail_java.q367113;

import java.awt.*;

import javax.swing.*;

public class MainFrame extends JFrame {
  public static void main(String[] args) {
    new MainFrame().setVisible(true);
  }

  MainFrame() {
    super();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JTextField rgbTextField = new JTextField();
    rgbTextField.setEditable(false);

    RGBSliderPanel rgbSliderPanel = new RGBSliderPanel();
    rgbSliderPanel.setChangeListener(new RGBSliderPanel.ChangeListener() {
      @Override
      public void onChange(int red, int green, int blue) {
        rgbTextField.setText("red="+red+", green="+green+", blue="+blue);
      }
    });

    add(rgbTextField, BorderLayout.NORTH);
    add(rgbSliderPanel, BorderLayout.CENTER);

    pack();
  }
}

class RGBSliderPanel extends JPanel {
  interface ChangeListener {
    void onChange(int red, int green, int blue);
  }
  private ChangeListener listener;
  private int red, green, blue;

  RGBSliderPanel() {
    super(new GridBagLayout());
    setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

    JLabel redLabel = new JLabel("R");
    JSlider redSlider = createSlider((e) -> {
      red = ((JSlider)e.getSource()).getValue();
      callChangeListener();
    });

    JLabel greenLabel = new JLabel("G");
    JSlider greenSlider = createSlider((e) -> {
      green = ((JSlider)e.getSource()).getValue();
      callChangeListener();
    });

    JLabel blueLabel = new JLabel("B");
    JSlider blueSlider = createSlider((e) -> {
      blue = ((JSlider)e.getSource()).getValue();
      callChangeListener();
    });

    GridBagConstraints gbc = new GridBagConstraints();
    //y0
    add(redLabel, gbc, 0, 0);
    add(Box.createHorizontalStrut(5), gbc, 1, 0); //ラベルとスライダーの隙間
    add(redSlider, gbc, 2, 0);
    //y1
    add(Box.createVerticalStrut(10), gbc, 0, 1); //R・Gスライダー間の隙間
    //y2
    add(greenLabel, gbc, 0, 2);
    add(greenSlider, gbc, 2, 2);
    //y3
    add(Box.createVerticalStrut(10), gbc, 0, 3); //G・Bスライダー間の隙間
    //y4
    add(blueLabel, gbc, 0, 4);
    add(blueSlider, gbc, 2, 4);
  }

  private JSlider createSlider(javax.swing.event.ChangeListener l) {
    JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 255, 0);
    slider.addChangeListener(l);
    return slider;
  }

  private void add(Component component, GridBagConstraints gbc, int x, int y) {
    gbc.gridx = x;
    gbc.gridy = y;
    add(component, gbc);
  }

  void setChangeListener(ChangeListener listener) {
    this.listener = listener;
    callChangeListener(); //初期表示用
  }

  private void callChangeListener() {
    if(listener!= null) listener.onChange(red, green, blue);
  }
}