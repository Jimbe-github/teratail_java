package teratail_java.q366141;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.*;

public class Q366141a extends JFrame {
  public static void main(String[] args) {
    new Q366141a().setVisible(true);
  }

  Q366141a() {
    super("割り勘");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

    ItemTableModel tableModel = new ItemTableModel();
    JTable table = new JTable(tableModel);
    //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    TableColumnModel tcm = table.getColumnModel();
    tcm.getColumn(0).setPreferredWidth(50);
    tcm.getColumn(1).setPreferredWidth(350);
    tcm.getColumn(2).setPreferredWidth(150);
    mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

    NameRegistPanel nameRegistPanel = new NameRegistPanel(tableModel);
    nameRegistPanel.setBorder(BorderFactory.createEmptyBorder(0,0,5,5));
    mainPanel.add(nameRegistPanel, BorderLayout.NORTH);

    SplitBillPanel splitBillPanel = new SplitBillPanel(tableModel);
    splitBillPanel.setBorder(BorderFactory.createEmptyBorder(0,5,5,0));
    mainPanel.add(splitBillPanel, BorderLayout.EAST);

    add(mainPanel);

    pack();
    setLocationRelativeTo(null);
  }
}

class NameRegistPanel extends JPanel {
  NameRegistPanel(ItemTableModel tableModel) {
    super(new GridBagLayout());

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridy = 0;

    JLabel label = new JLabel("名前：");
    add(label, gbc);

    JTextField nameField = new JTextField(30);
    add(nameField, gbc);

    JButton registButton = new JButton("登録");
    registButton.setEnabled(false);
    registButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        tableModel.add(nameField.getText());
        nameField.setText("");
      }
    });
    gbc.weightx = 1.0;
    gbc.anchor = GridBagConstraints.WEST;
    add(registButton, gbc);

    nameField.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void removeUpdate(DocumentEvent e) { update(e.getDocument()); }
      @Override
      public void insertUpdate(DocumentEvent e) { update(e.getDocument()); }
      @Override
      public void changedUpdate(DocumentEvent e) { update(e.getDocument()); }
      private void update(Document doc) {
        registButton.setEnabled(doc.getLength() > 0);
      }
    });
  }
}

class SplitBillPanel extends JPanel {
  private JTextField billField;
  private JLabel numberOfSelectedLabel;
  private JLabel eachAmountLabel;
  private JButton addButton;
  private int bill = 0;
  private int numberOfSelected = 0;
  private int eachAmount = 0;

  SplitBillPanel(ItemTableModel tableModel) {
    super(new GridBagLayout());

    tableModel.setSelectListener(new ItemTableModel.SelectListener() {
      @Override
      public void changeSelected(ItemTableModel model) {
        numberOfSelected = model.getSelectedCount();
        setDiplay();
      }
    });

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;

    JLabel label = new JLabel("金額：");
    gbc.anchor = GridBagConstraints.WEST;
    add(label, gbc);

    billField = new JTextField(10);
    IntDocument document = new IntDocument();
    document.addDocumentListener(new DocumentListener() {
      @Override
      public void removeUpdate(DocumentEvent e) { update(e.getDocument()); }
      @Override
      public void insertUpdate(DocumentEvent e) { update(e.getDocument()); }
      @Override
      public void changedUpdate(DocumentEvent e) { update(e.getDocument()); }
      private void update(Document doc) {
        try {
          String s = doc.getText(0, doc.getLength());
          bill = Integer.parseInt(s);
        } catch (BadLocationException|NumberFormatException e) {
          bill = 0;
        }
        setDiplay();
      }
    });
    billField.setDocument(document);
    billField.setHorizontalAlignment(JTextField.RIGHT);
    gbc.anchor = GridBagConstraints.CENTER;
    add(billField, gbc);

    numberOfSelectedLabel = new JLabel("");
    gbc.anchor = GridBagConstraints.EAST;
    add(numberOfSelectedLabel, gbc);

    eachAmountLabel = new JLabel("");
    add(eachAmountLabel, gbc);

    add(Box.createVerticalStrut(10), gbc);

    addButton = new JButton("合計に加算");
    addButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        tableModel.addToSelectedItems(eachAmount);
        clearBill();
      }
    });
    gbc.weighty = 1.0;
    gbc.anchor = GridBagConstraints.NORTHEAST;
    add(addButton, gbc);

    setDiplay();
  }

  private void clearBill() {
    billField.setText("");
    setDiplay();
  }
  private void setDiplay() {
    numberOfSelectedLabel.setText("対象 "+numberOfSelected+" 人");
    eachAmount = 0;
    if(bill > 0 && numberOfSelected > 0) {
      eachAmount = bill / numberOfSelected;
    }
    addButton.setEnabled(eachAmount > 0);
    eachAmountLabel.setText("各 "+eachAmount+" 円");
  }
}

class IntDocument extends PlainDocument {
  @Override
  public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
    if(str == null) return;
    String oldString = getText(0, getLength());
    String newString = oldString.substring(0, offs) + str + oldString.substring(offs);
    try {
      Integer.parseInt(newString + "0");
      super.insertString(offs, str, a);
    } catch (NumberFormatException e) {
      //ignore
    }
  }
}

class ListItem {
  boolean selected;
  final Item item;
  ListItem(String name) {
    this.item = new Item(name);
    selected = false;
  }
}

class Item {
  final String name;
  int total;
  Item(String name) {
    this.name = name;
    total = 0;
  }
}

class ItemTableModel extends AbstractTableModel {
  interface SelectListener {
    void changeSelected(ItemTableModel model);
  }
  private SelectListener selectListener;
  private List<ListItem> list = new ArrayList<ListItem>();

  void setSelectListener(SelectListener listener) {
    this.selectListener = listener;
  }
  public void addToSelectedItems(int eachAmount) {
    for(int rowIndex=0; rowIndex<list.size(); rowIndex++) {
      ListItem listItem = list.get(rowIndex);
      if(listItem.selected) {
        listItem.item.total += eachAmount;
        fireTableRowsUpdated(rowIndex, rowIndex);
      }
    }
  }
  void add(String name) {
    list.add(new ListItem(name));
    fireTableRowsInserted(list.size()-1, list.size()-1);
  }
  int getSelectedCount() {
    int count = 0;
    for(ListItem listItem : list) if(listItem.selected) count++;
    return count;
  }

  @Override
  public int getRowCount() {
    return list.size();
  }
  @Override
  public int getColumnCount() {
    return 3;
  }
  @Override
  public String getColumnName(int columnIndex) {
    switch(columnIndex) {
    case 0: return "対象";
    case 1: return "名前";
    case 2: return "合計";
    }
    return super.getColumnName(columnIndex);
  }
  @Override
  public Class<?> getColumnClass(int columnIndex) {
    switch(columnIndex) {
    case 0: return Boolean.class;
    case 1: return String.class;
    case 2: return Integer.class;
    }
    return super.getColumnClass(columnIndex);
  }
  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return columnIndex == 0;
  }
  @Override
  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    if(columnIndex == 0) {
      list.get(rowIndex).selected = (Boolean)aValue;
      fireTableCellUpdated(rowIndex, columnIndex);
      if(selectListener != null) selectListener.changeSelected(this);
    }
  }
  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    ListItem listItem = list.get(rowIndex);
    switch(columnIndex) {
    case 0: return listItem.selected;
    case 1: return listItem.item.name;
    case 2: return listItem.item.total;
    }
    return null;
  }
}
