package teratail_java.q_ma9cgl882hfegi;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.AbstractTableModel;

public class MainFrame extends JFrame {
  public static void main(String[] args) {
    new MainFrame().setVisible(true);
  }

  MainFrame() {
    super("表示");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(800, 600);
    setLocationRelativeTo(null);

    MachineModel model = new MachineModel();
    model.put(new Machine("機械1", 100, OilType.ABC, 50)); //テストデータ1
    model.put(new Machine("機械2", 200, OilType.FRE, 10)); //テストデータ2

    JTabbedPane tabbedPane = new JTabbedPane();
    for(int i=0; i<model.size(); i++) {
      MachinePanel panel = new MachinePanel(model.get(i).name, model);
      tabbedPane.addTab(panel.name, panel);
    }
    tabbedPane.addTab("情報", new InfoPanel(model, (selected) -> {
      JDialog dialog = MachineDialog.createInstance(MainFrame.this, selected, (newObject)->model.put(newObject));
      dialog.setLocationRelativeTo(null);
      dialog.setVisible(true);
    }));

    getContentPane().add(tabbedPane);
  }
}
//"機械n"タブ
class MachinePanel extends JPanel {
  final String name;
  private MachineModel model;
  private JLabel label;

  MachinePanel(String name, MachineModel model) {
    super();
    setPreferredSize(new Dimension(720, 240));
    setBorder(new LineBorder(Color.RED, 2, true));

    this.name = name;
    this.model = model;

    model.addChangeListener((e) -> setLabels());

    label = new JLabel();
    add(label);

    setLabels();
  }

  private void setLabels() {
    label.setText(""+model.get(name));
  }
}
//"情報"タブ
class InfoPanel extends JPanel {
  interface MachineSelectedListener {
    void selected(Machine machine);
  }

  public InfoPanel(MachineModel model, MachineSelectedListener listener){
    super(new BorderLayout());

    add(new JScrollPane(createTable(new InfoTableModel(model), listener)), BorderLayout.NORTH);

    add(createSouthPanel(), BorderLayout.SOUTH);
  }

  private JTable createTable(InfoTableModel tableModel, MachineSelectedListener listener) {
    JTable table = new JTable(tableModel);
    table.setRowSelectionAllowed(true);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.setColumnSelectionAllowed(false);
    table.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        int rowIndex = table.getSelectedRow();
        if(rowIndex >= 0) listener.selected(tableModel.get(rowIndex));
      }
    });
    return table;
  }

  private JPanel createSouthPanel() {
    JPanel panel = new JPanel();
    panel.add(new JLabel("テロップ内容"));

    JTextField textfield = new JTextField();
    textfield.setText("残量確認のこと");
    panel.add(textfield);

    return panel;
  }
}
//機械情報テーブルモデル
class InfoTableModel extends AbstractTableModel {
  enum Columns {
    NAME("", String.class) { @Override Object getFieldValue(Machine m) { return m.name; } },
    OUTPUT("ワット数", Integer.class) { @Override Object getFieldValue(Machine m) { return m.output; } },
    OIL_TYPE("油の種類", OilType.class) { @Override Object getFieldValue(Machine m) { return m.oilType; } },
    OIL_AMOUNT("油の残量", Integer.class) { @Override Object getFieldValue(Machine m) { return m.oilAmount; } };

    String name;
    Class<?> clazz;
    Columns(String name, Class<?> clazz) {
      this.name = name;
      this.clazz = clazz;
    }
    Object getFieldValue(Machine m) { return null; }

    static int size() { return values().length; }
    static Columns get(int index) { return values()[index]; };
  }

  private MachineModel model;

  InfoTableModel(MachineModel model) {
    this.model = model;
    model.addChangeListener((e) -> fireTableDataChanged());
  }

  Machine get(int index) {
    return model.get(index);
  }

  @Override
  public String getColumnName(int columnIndex) {
    return Columns.get(columnIndex).name;
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return Columns.get(columnIndex).clazz;
  }

  @Override
  public int getRowCount() {
    return model.size();
  }

  @Override
  public int getColumnCount() {
    return Columns.size();
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    Machine machine = model.get(rowIndex);
    return Columns.get(columnIndex).getFieldValue(machine);
  }
}