package teratail_java.q_ma9cgl882hfegi;

import java.awt.*;
import java.awt.event.*;

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

    InputPanel inputPanel = new InputPanel(machine);
    inputPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    add(inputPanel, BorderLayout.CENTER);

    JPanel buttonPanel = new ButtonPanel(inputPanel, manager);
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,0));
    add(buttonPanel, BorderLayout.SOUTH);

    pack();
  }

  private static class InputPanel  extends JPanel {
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
      addLabel(gbc, "名称");
      JTextField nameText = new JTextField(name);
      nameText.setFocusable(false);
      nameText.setEditable(false);
      add(nameText, gbc);

      gbc.gridy ++;
      add(Box.createVerticalStrut(8), gbc);

      gbc.gridy ++;
      JLabel outputLabel = addLabel(gbc, "ワット数");
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
      JLabel oilAmountLabel = addLabel(gbc, "油の残量");
      JTextField oilAmountText = new JTextField(""+machine.oilAmount);
      add(oilAmountText, gbc);
      oilAmountField = new ValidatableField(oilAmountText, oilAmountLabel);
    }

    private JLabel addLabel(GridBagConstraints gbc, String labelText) {
      JLabel label = new JLabel(labelText + ":");
      label.setHorizontalAlignment(JLabel.RIGHT);
      Insets backup = gbc.insets;
      gbc.insets = new Insets(0,0,0,5);
      add(label, gbc);
      gbc.insets = backup;
      return label;
    }

    Machine getNewObject() {
      int output = outputField.getInt();
      OilType oilType = oilTypePanel.getOilType();
      int oilAmount = oilAmountField.getInt();
      if(!outputField.isValid() || !oilAmountField.isValid()) return null;
      return new Machine(name, output, oilType, oilAmount);
    }
  }

  private static class ValidatableField {
    private JTextField textField;
    private JLabel label;
    private Color normal;
    private boolean valid;

    ValidatableField(JTextField textField, JLabel label) {
      this.textField = textField;
      this.label = label;
      this.normal = label.getForeground();
    }

    int getInt() {
      try {
        valid = true;
        label.setForeground(normal);
        return Integer.parseInt(textField.getText().trim());
      } catch(Exception e) {
        //No process
      }
      valid = false;
      textField.requestFocus();
      label.setForeground(Color.RED);
      return 0;
    }

    boolean isValid() {
      return valid;
    }
  }

  private static class OilTypePanel extends JPanel {
    private OilType oilType;

    OilTypePanel(OilType oilType) {
      super(new GridLayout(0, 4)); //cols はテキトウ

      this.oilType = oilType;

      ActionListener oilTypeActionListener = (e) -> {
        String buttonText = ((JRadioButton)e.getSource()).getText();
        this.oilType = OilType.valueOf(buttonText);
      };

      ButtonGroup group = new ButtonGroup();
      for(OilType type : OilType.values()) {
        JRadioButton button = new JRadioButton(type.toString());
        button.addActionListener(oilTypeActionListener);
        if(type == oilType) button.setSelected(true);
        group.add(button);
        add(button);
      }
    }

    OilType getOilType() {
      return oilType;
    }
  }

  private class ButtonPanel extends JPanel {
    ButtonPanel(InputPanel inputPanel, MachineManager manager) {
      super(new FlowLayout(FlowLayout.TRAILING, 10, 0));

      JButton okButton = new JButton("OK");
      okButton.addActionListener((ae) -> {
        Machine machine = inputPanel.getNewObject();
        if(machine == null) return; //入力に問題があった
        manager.put(machine);
        dispose();
      });
      add(okButton);

      Action cancelAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
          dispose();
        }
      };

      JButton cancelButton = new JButton("キャンセル");
      cancelButton.addActionListener(cancelAction);
      add(cancelButton);

      new SizeMatcher(okButton, cancelButton); //ボタンを同じ大きさにする

      //フォーカスに関係無く動作するキーを設定
      setDefaultKey(okButton, cancelAction);
    }

    private void setDefaultKey(JButton ok, Action cancel) {
      JRootPane dialogRootPane = MachineDialog.this.getRootPane();

      //ENTER キーで OK
      dialogRootPane.setDefaultButton(ok);

      //ESC キーで CANCEL
      InputMap imap = dialogRootPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
      imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel");
      dialogRootPane.getActionMap().put("cancel", cancel);
    }
  }
}