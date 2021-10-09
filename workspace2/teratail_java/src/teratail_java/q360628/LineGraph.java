package teratail_java.q360628;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class LineGraph extends JFrame {

  public static void main(String args[]) {
    new LineGraph().setVisible(true);
  }

  private static final int DATASIZE = 6;

  LineGraph() {
    super("LineGraph");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    LineGraphPanel graphPanel = new LineGraphPanel(DATASIZE);
    DataPanel dataPanel = new DataPanel(DATASIZE, new DataPanel.ChangeListener() {
      @Override
      public void changed(int index, int value) {
        graphPanel.setData(index, value);
      }
    });

    add(graphPanel, BorderLayout.CENTER);
    add(dataPanel, BorderLayout.NORTH);
    pack();
  }

  private static class LineGraphPanel extends JPanel {
    private int data[];

    LineGraphPanel(int datasize) {
      setPreferredSize(new Dimension(300, 300));
      setMinimumSize(getPreferredSize());

      data = new int[datasize];
    }

    void setData(int index, int value) {
      data[index] = value;
      repaint();
    }

    public void paint(Graphics g) {
      super.paint(g);

      //Xスケール
      g.drawLine( 50, 250, 250, 250);
      for (int i=0; i<data.length; i++) {
        String s = String.format("%3d", i);
        g.drawString(s, i * 20 + 40, 270);
      }

      //Yスケール
      g.drawLine( 50, 40, 50, 250);
      for (int i=0; i<=10; i++) {
        String s = String.format("%3d", i * 10);
        g.drawString(s, 28, 250 - i * 20);
      }

      //グラフ
      for (int i=0; i<data.length-1; i++) {
        int x1 = i * 20 + 50;
        int x2 = (i + 1) * 20 + 50;
        int y1 = 250 - data[i] * 2;
        int y2 = 250 - data[i+1] * 2;
        g.drawLine( x1, y1, x2, y2);
      }
    }
  }

  private static class DataPanel extends JPanel {
    interface ChangeListener {
      void changed(int index, int value);
    }

    private JLabel messageLabel = new JLabel("数値を入力してください");
    private ChangeListener listener;

    DataPanel(int size, ChangeListener listener) {
      super(new BorderLayout());
      this.listener = listener;

      add(messageLabel, BorderLayout.NORTH);

      JPanel inputPanel = new JPanel(new FlowLayout());
      for(int i=0; i<size; i++) {
        inputPanel.add(new LabelAndInputField(i, 0));
      }
      add(inputPanel, BorderLayout.CENTER);

      setNormalState();
    }

    private void setNormalState() {
      messageLabel.setForeground(Color.BLACK);
    }

    private void setErrorState() {
      messageLabel.setForeground(Color.RED);
    }

    private class LabelAndInputField extends JPanel implements ActionListener, DocumentListener, FocusListener {
      private int number;
      private JLabel label;
      private JTextField field;

      LabelAndInputField(int number, int initialValue) {
        super(new BorderLayout());
        this.number = number;

        field = new JTextField(""+initialValue, 3);
        field.addActionListener(this);
        field.getDocument().addDocumentListener(this);
        field.addFocusListener(this);

        label = new JLabel(number+":");

        add(label, BorderLayout.WEST);
        add(field, BorderLayout.CENTER);

        setNormalState();
      }

      private void setNormalState() {
        label.setForeground(Color.BLACK);
        DataPanel.this.setNormalState();
      }

      private void setErrorState() {
        label.setForeground(Color.RED);
        DataPanel.this.setErrorState();
      }

      @Override
      public void actionPerformed(ActionEvent ae) {
        try {
          int data = Integer.parseInt(field.getText());
          listener.changed(number, data);
        } catch(NumberFormatException e) {
          setErrorState();
          field.requestFocus();
        }
      }

      @Override
      public void removeUpdate(DocumentEvent e) { setNormalState(); }
      @Override
      public void insertUpdate(DocumentEvent e) { setNormalState(); }
      @Override
      public void changedUpdate(DocumentEvent e) { setNormalState(); }

      @Override
      public void focusLost(FocusEvent e) { actionPerformed(null); }
      @Override
      public void focusGained(FocusEvent e) {} //ignore
    }
  }
}