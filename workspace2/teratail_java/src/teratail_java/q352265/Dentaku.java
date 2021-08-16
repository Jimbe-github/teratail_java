package teratail_java.q352265;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayDeque;
import java.util.Deque;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Dentaku extends JFrame{
  public static void main(String[] args) {
    new Dentaku().setVisible(true);
  }

  enum Operation {
    Pls("＋",0) { int calc(int a,int b){return a+b;} },
    Min("－",0) { int calc(int a,int b){return a-b;} },
    Mul("×",1) { int calc(int a,int b){return a*b;} },
    Div("÷",1) { int calc(int a,int b){return a/b;} };

    private String text;
    private int priority;
    Operation(String text, int priority) {
      this.text = text;
      this.priority = priority;
    }
    String getText() { return text; }
    int compare(Operation other) { return priority - other.priority; }
    abstract int calc(int a, int b);
  }

  private int dispNum = 0;
  private Deque<Integer> vstack = new ArrayDeque<Integer>();
  private Deque<Operation> opstack = new ArrayDeque<Operation>();

  Dentaku(){
    super();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(280, 320);
    setResizable(false);

    JTextField result = new JTextField("");
    result.setFont(new Font("Arial", Font.PLAIN, 40));
    result.setHorizontalAlignment(JTextField.RIGHT);

    ActionListener numActionListener = (e -> {
      int num = Integer.valueOf(((JButton)e.getSource()).getText());
      dispNum = dispNum * 10 + num;
      result.setText(String.valueOf(dispNum));
    });

    ActionListener opeActionListener = (e -> {
      Operation op = (Operation)((JButton)e.getSource()).getClientProperty("op");
      vstack.addFirst(dispNum);
      while(true) {
        Operation before = opstack.peekFirst();
        if(before == null || op.compare(before) > 0) {
          opstack.addFirst(op);
          break;
        }
        calc();
      }
      result.setText(String.valueOf(dispNum));
      dispNum = 0;
    });

    ActionListener clrActionListener = (e -> {
      vstack.clear();
      opstack.clear();
      result.setText("");
      dispNum = 0;
    });

    ActionListener equActionListener = (e -> {
      vstack.addFirst(dispNum);
      while(!opstack.isEmpty()) calc();
      result.setText(String.valueOf(vstack.removeFirst()));
      vstack.clear();
      opstack.clear();
      dispNum = 0;
    });

    setLayout(new GridLayout(2,1));
    add(result);

    JPanel buttonarea = new JPanel(new GridLayout(4,4));
    buttonarea.add(createButton("7", numActionListener));
    buttonarea.add(createButton("8", numActionListener));
    buttonarea.add(createButton("9", numActionListener));
    buttonarea.add(createButton(Operation.Div, opeActionListener));
    buttonarea.add(createButton("4", numActionListener));
    buttonarea.add(createButton("5", numActionListener));
    buttonarea.add(createButton("6", numActionListener));
    buttonarea.add(createButton(Operation.Mul, opeActionListener));
    buttonarea.add(createButton("1", numActionListener));
    buttonarea.add(createButton("2", numActionListener));
    buttonarea.add(createButton("3", numActionListener));
    buttonarea.add(createButton(Operation.Min, opeActionListener));
    buttonarea.add(createButton("C", clrActionListener));
    buttonarea.add(createButton("0", numActionListener));
    buttonarea.add(createButton(Operation.Pls, opeActionListener));
    buttonarea.add(createButton("=", equActionListener));
    add(buttonarea);
  }

  private JButton createButton(Operation op, ActionListener l) {
    JButton button = createButton(op.getText(), l);
    button.putClientProperty("op", op);
    return button;
  }
  private JButton createButton(String text, ActionListener l) {
    JButton button = new JButton(text);
    button.addActionListener(l);
    return button;
  }

  private void calc() {
    int b = vstack.removeFirst();
    int a = vstack.removeFirst();
    Operation op = opstack.removeFirst();
    vstack.addFirst(op.calc(a, b));
  }
}
