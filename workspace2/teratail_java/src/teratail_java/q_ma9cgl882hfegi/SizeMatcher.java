package teratail_java.q_ma9cgl882hfegi;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/*** 推奨サイズを同じにする */
class SizeMatcher implements AncestorListener {
  private List<JComponent> compList = new ArrayList<>();

  SizeMatcher(JComponent... comps) {
    for(JComponent c : comps) add(c);
  }

  void add(JComponent comp) {
    compList.add(comp);
    comp.addAncestorListener(this);
  }

  @Override
  public void ancestorMoved(AncestorEvent ae) {
    int w=0, h=0;
    for(JComponent comp : compList) {
      w = w < comp.getWidth()  ? comp.getWidth()  : w;
      h = h < comp.getHeight() ? comp.getHeight() : h;
    }
    Dimension d = new Dimension(w, h);
    for(JComponent comp : compList) {
      if(comp.getWidth() < w || comp.getHeight() < h) {
        comp.setPreferredSize(d);
        comp.revalidate();
      }
    }
  }
  @Override
  public void ancestorAdded(AncestorEvent ae) {}
  @Override
  public void ancestorRemoved(AncestorEvent ae) {}
}