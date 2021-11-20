package teratail_java.q370179;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

public class OXview extends JPanel {
  private static final int SIZE = 120;

  interface StateChangeListener {
    void onChanged();
  }
  interface Model {
    void setStateChangeListener(StateChangeListener l);
    Cell get(int x, int y);
  }

  interface CellClickListener {
    void onCellClicked(int x, int y);
  }

  private class OXMouseListener implements MouseListener {
    @Override
    public void mouseClicked(MouseEvent e) {
      //System.out.println("mouseClicked: "+e);
      if(listener != null) listener.onCellClicked(e.getX() / SIZE, e.getY() / SIZE);
    }
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
  }

  private CellClickListener listener;
  private JLabel startLabel, resultLabel;
  private Model model;

  OXview(Model model) {
    super(null);
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setPreferredSize(new Dimension(SIZE*3,SIZE*3));
    setMinimumSize(getPreferredSize());
    setBackground(Color.white);
    addMouseListener(new OXMouseListener());

    this.model = model;
    model.setStateChangeListener(() -> repaint());

    startLabel = createLabel("CLICK TO (RE)START !", Color.RED);
    resultLabel = createLabel("", Color.BLACK);

    add(Box.createVerticalGlue());
    add(startLabel);
    add(Box.createVerticalGlue());
    add(resultLabel);
    add(Box.createVerticalGlue());
  }

  private JLabel createLabel(String text, Color color) {
    JLabel label = new JLabel(text);
    label.setMaximumSize(new Dimension(Short.MAX_VALUE, (int)label.getMaximumSize().getHeight()));
    label.setHorizontalAlignment(SwingConstants.CENTER);
    label.setFont(label.getFont().deriveFont(30f));
    label.setForeground(color);
    return label;
  }

  boolean inGame() {
    return !startLabel.isVisible(); //ラベルの表示状態をゲーム中かの判断に流用
  }

  void start() {
    startLabel.setVisible(false);
    resultLabel.setVisible(false);
  }

  void end(Cell winner) {
    startLabel.setVisible(true);
    resultLabel.setText(winner.getResult());
    resultLabel.setVisible(true);
  }

  void setCellClickListener(CellClickListener listener) {
    this.listener = listener;
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    g.setColor(Color.black);
    for(int i=0 ; i<2 ; i++) {
      g.drawLine(0,SIZE+i*SIZE,SIZE*3,SIZE+i*SIZE);
      g.drawLine(SIZE+i*SIZE,0,SIZE+i*SIZE,SIZE*3);
    }

    for(int y=0 ; y<3 ; y++) {
      for(int x=0 ; x<3 ; x++) {
        int bx = x*SIZE, by = y*SIZE;
        model.get(x, y).draw(g, bx+10, by+10, SIZE-20);
      }
    }
  }
}