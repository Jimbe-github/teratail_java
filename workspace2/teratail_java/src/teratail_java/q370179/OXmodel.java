package teratail_java.q370179;

public class OXmodel implements OXview.Model {
  private OXview.StateChangeListener listener;
  private Cell brd[][] = new Cell[3][3];

  OXmodel() {
    clear();
  }

  void clear() {
    for(int y=0 ; y<3 ; y++) {
      for(int x=0 ; x<3 ; x++) {
        brd[y][x] = Cell.SPACE;
      }
    }
    notifyStateChange();
  }

  private void notifyStateChange() {
    if(listener != null) listener.onChanged();
  }

  @Override
  public void setStateChangeListener(OXview.StateChangeListener listener) {
    this.listener = listener;
  }

  @Override
  public Cell get(int x, int y) {
    return brd[y][x];
  }

  Cell set(int x, int y, Cell turn) {
    brd[y][x] = turn;
    notifyStateChange();
    return judge();
  }

  private Cell judge() {
    for(int i=0 ; i<3 ; i++) {
      if(brd[i][0]!=Cell.SPACE && brd[i][0]==brd[i][1] && brd[i][1]==brd[i][2]) {
        return brd[i][0];
      }
      if(brd[0][i]!=Cell.SPACE && brd[0][i]==brd[1][i] && brd[1][i]==brd[2][i]) {
        return brd[0][i];
      }
    }

    if(brd[1][1]!=Cell.SPACE) {
      if(brd[0][0]==brd[1][1] && brd[2][2]==brd[1][1]) {
        return brd[1][1];
      }
      if(brd[2][0]==brd[1][1] && brd[0][2]==brd[1][1]) {
        return brd[1][1];
      }
    }

    for(int y=0 ; y<3 ; y++) {
      for(int x=0 ; x<3 ; x++) {
        if(brd[y][x] == Cell.SPACE) return null; //勝負はまだついていない
      }
    }
    return Cell.SPACE; //引き分け
  }
}