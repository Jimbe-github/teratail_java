package teratail_java.q_ma9cgl882hfegi;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.border.TitledBorder;

//機械情報入力ダイアログ
class MachineDialog extends JDialog {
  interface MachineManager {
    void put(Machine machine);
  }

  //JOptionPane で作るほうが良いかもしれないので.
  static MachineDialog createInstance(Frame frame, Machine machine, MachineManager manager) {
    return new MachineDialog(frame, machine, manager);
  }

  private MachineDialog(Frame owner, Machine machine, MachineManager manager) {
    super(owner, "機械情報 - 詳細入力", true);
    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    setResizable(false);

    //ESC キーで CANCEL
    InputMap imap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "dispose");
    getRootPane().getActionMap().put("dispose", new AbstractAction() {
      @Override public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });

    InputPanel inputPanel = new InputPanel(machine);
    inputPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    add(inputPanel, BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING, 10, 0));
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,0));

    JButton okButton = new JButton("OK");
    getRootPane().setDefaultButton(okButton); //ENTER キーで OK
    okButton.addActionListener((ae) -> {
      try {
        manager.put(inputPanel.get());
        dispose();
      } catch(InputPanel.VaridateException e) {
      }
    });
    buttonPanel.add(okButton);

    JButton cancelButton = new JButton("キャンセル");
    cancelButton.addActionListener((e) -> dispose());
    buttonPanel.add(cancelButton);

    new SizeMatcher(okButton, cancelButton); //ボタンを同じ大きさにする

    add(buttonPanel, BorderLayout.SOUTH);

    pack();
  }

  private static class InputPanel  extends JPanel {
    static class VaridateException extends Exception {
      VaridateException(Throwable cause) {
        super(cause);
      }
    }

    private String name;
    private ValidatableField outputField;
    private OilTypePanel oilTypePanel;
    private ValidatableField oilAmountField;

    InputPanel(Machine machine) {
      super(new GridBagLayout());

      this.name = machine.name;

      GridBagConstraints gbc = new GridBagConstraints();
      gbc.anchor = GridBagConstraints.NORTH;
      gbc.fill = GridBagConstraints.HORIZONTAL;

      gbc.gridy = 0;
      appendLabelTo(gbc, "名称");
      JTextField nameText = new JTextField(name);
      nameText.setFocusable(false);
      nameText.setEditable(false);
      add(nameText, gbc);

      gbc.gridy ++;
      add(Box.createVerticalStrut(8), gbc);

      gbc.gridy ++;
      JLabel outputLabel = appendLabelTo(gbc, "ワット数");
      JTextField outputText = new JTextField(""+machine.output);
      add(outputText, gbc);
      outputField = new ValidatableField(outputText, outputLabel);

      gbc.gridy ++;
      add(Box.createVerticalStrut(8), gbc);

      gbc.gridy ++;
      gbc.gridwidth = GridBagConstraints.REMAINDER;
      oilTypePanel = new OilTypePanel(machine.oilType);
      oilTypePanel.setBorder(BorderFactory.createTitledBorder(
          BorderFactory.createLineBorder(Color.GRAY, 1, true),
          "油の種類:", TitledBorder.LEADING, TitledBorder.TOP));
      add(oilTypePanel, gbc);

      gbc.gridy ++;
      gbc.gridwidth = 1;
      add(Box.createVerticalStrut(8), gbc);

      gbc.gridy ++;
      JLabel oilAmountLabel = appendLabelTo(gbc, "油の残量");
      JTextField oilAmountText = new JTextField(""+machine.oilAmount);
      add(oilAmountText, gbc);
      oilAmountField = new ValidatableField(oilAmountText, oilAmountLabel);
    }

    private JLabel appendLabelTo(GridBagConstraints gbc, String labelText) {
      JLabel label = new JLabel(labelText + ":");
      label.setHorizontalAlignment(JLabel.RIGHT);
      Insets backup = gbc.insets;
      gbc.insets = new Insets(0,0,0,5);
      add(label, gbc);
      gbc.insets = backup;
      return label;
    }

    Machine get() throws VaridateException {
      int output = outputField.getInt();
      OilType oilType = oilTypePanel.getOilType();
      int oilAmount = oilAmountField.getInt();
      return new Machine(name, output, oilType, oilAmount);
    }

    private static class ValidatableField {
      private JTextField textField;
      private JLabel label;
      private Color normal;
      ValidatableField(JTextField textField, JLabel label) {
        this.textField = textField;
        this.label = label;
        this.normal = label.getForeground();
      }
      private int getInt() throws VaridateException {
        try {
          label.setForeground(normal);
          return Integer.parseInt(textField.getText().trim());
        } catch(NumberFormatException e) {
          textField.requestFocus();
          label.setForeground(Color.RED);
          throw new VaridateException(e);
        }
      }
    }

    private static class OilTypePanel extends JPanel {
      private OilType oilType;

      OilTypePanel(OilType oilType) {
        super(new GridLayout(0, 4)); //cols はテキトウ

        this.oilType = oilType;

        ButtonGroup group = new ButtonGroup();
        for(OilType type : OilType.values()) {
          JRadioButton button = new JRadioButton(type.toString());
          button.addActionListener((e) -> this.oilType = OilType.valueOf(((JRadioButton)e.getSource()).getText()));
          if(type == oilType) button.setSelected(true);
          group.add(button);
          add(button);
        }
      }

      OilType getOilType() {
        return oilType;
      }
    }
  }
}