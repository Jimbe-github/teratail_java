package teratail_java.q356940;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Model {
  /** 時刻. */
  final LocalTime localTime;
  /** 身長 */
  final int height;
  /** 体重. */
  final int weight;

  public Model(LocalTime localTime, int height, int weight) {
    this.localTime = localTime;
    this.height = height;
    this.weight = weight;
  }

  @Override
  public String toString() {
    return new StringBuilder(super.toString())
        .append("[localtime=").append(localTime)
        .append(",height=").append(height)
        .append(",weight=").append(weight)
        .append("]").toString();
  }
}

public class Q356940 {
  public static void main(String[] args) {
    List<Model> list = new ArrayList<>();

    Model a = new Model(LocalTime.of(0, 0), 60, 90);
    Model b = new Model(LocalTime.of(0, 0), 60, 90);
    Model c = new Model(LocalTime.of(12, 0), 65, 90);
    Model d = new Model(LocalTime.of(12, 0), 70, 80);
    Model e = new Model(LocalTime.of(15, 0), 80, 90);
    Model f = new Model(LocalTime.of(15, 0), 80, 90);

    list.add(a);
    list.add(b);
    list.add(c);
    list.add(d);
    list.add(e);
    list.add(f);
    for(Model m : list) System.out.println(m);

    Random random = new Random();
    for (int i = 0; i < list.size(); i++) {
      for (int j = i+1; j < list.size(); j++) {
        //時刻比較(どちらかが消える)
        if (list.get(i).localTime == list.get(j).localTime) {
          //身長比較(小さいほうが消える)
          if (list.get(i).height > list.get(j).height) {
            list.remove(j--); continue;
          } else if (list.get(i).height < list.get(j).height) {
            list.remove(i--); break;
          }
          //体重比較(軽いほうが消える)
          if (list.get(i).weight > list.get(j).weight) {
            list.remove(j--); continue;
          } else if (list.get(i).weight < list.get(j).weight) {
            list.remove(i--); break;
          }
          //どっちでもいい(ランダムに消える)
          if(random.nextInt(2) == 0) {
            list.remove(j--); continue;
          } else {
            list.remove(i--); break;
          }
        }
      }
    }

    System.out.println("----");
    for(Model m : list) System.out.println(m);
  }
}
