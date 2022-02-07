package teratail_java.q_ma9cgl882hfegi;

import java.util.*;

//機械モデル
class MachineModel {
  interface DataChangeListener extends EventListener {
    public void dataChanged(DataChangeEvent e);
  }

  static class DataChangeEvent extends EventObject {
    public DataChangeEvent(Object source) {
      super(source);
    }
  }

  private List<Machine> machineList = new ArrayList<>();
  private final Set<DataChangeListener> listenerList = new HashSet<>();

  int size() {
    return machineList.size();
  }

  Machine get(int index) {
    return machineList.get(index);
  }

  Machine get(String name) {
    for(Machine m : machineList) if(m.name.equals(name)) return m;
    return null;
  }

  public void put(Machine machine) {
    for(int i=0; i<machineList.size(); i++) {
      if(machineList.get(i).name.equals(machine.name)) {
        machineList.set(i, machine);
        fireDataChangeEvent();
        return;
      }
    }
    machineList.add(machine);
    fireDataChangeEvent();
  }

  synchronized void addChangeListener(DataChangeListener l) {
    listenerList.add(l);
  }

  synchronized void removeChangeListener(DataChangeListener l) {
    listenerList.remove(l);
  }

  synchronized protected void fireDataChangeEvent() {
    DataChangeEvent evt = new DataChangeEvent(this);
    for(DataChangeListener listener : listenerList) listener.dataChanged(evt);
  }
}