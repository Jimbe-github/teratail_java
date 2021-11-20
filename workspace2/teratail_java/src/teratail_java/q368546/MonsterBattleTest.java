package teratail_java.q368546;

class Power{
  //パワーの最小値、最大値、デフォルト値[馬力]
  final static int MIN = 0;
  final static int MAX = 1000;
  final static int DEFAULT = 100;
  int value;    //パワー

  //コンストラクタ(デフォルトに設定)
  Power(){
    this(DEFAULT);
  }
  //コンストラクタ(指定した値に設定)
  Power(int value) {
    if(value < MIN || MAX < value) {
      throw new IllegalArgumentException("value");
    }
    this.value = value;
  }
}

//(パワーの)消費モード
enum ConsumptionMode {
  MostSaving("Most Saving", 1/4),
  Saving("Saving", 1/2),
  Normal("Normal", 1),
  Powerful("Powerful", 2),
  MostPowerful("Most Powerful", 4);

  static ConsumptionMode getDefault() {
    return Normal;
  }

  private String text; //表示用文字列
  private double correctionValue; //補正値
  ConsumptionMode(String text, double correctionValue) {
    this.text = text;
    this.correctionValue = correctionValue;
  }
  int correct(int value) {
    return (int)(value * correctionValue);
  }
  @Override
  public String toString() {
    return text;
  }
}

class Monster{
  //持っているパワー
  Power power;

  //1回で発揮できるパワーの最小値、最大値。デフォルト値[馬力/回]
  final static int MIN_POWERRATE = 5;
  final static int MAX_POWERRATE = 100;
  final static int DEFALT_POWERRATE = 10;
  int powerRate;

  //パワーの消費モード
  ConsumptionMode attackMode;        //攻撃する時のパワーの消費モード
  ConsumptionMode beAttackedMode;    //攻撃される時のパワーの消費モード

  //コンストラクタ(デフォルト設定)
  Monster(){
    this(new Power(),DEFALT_POWERRATE,ConsumptionMode.getDefault(),ConsumptionMode.getDefault());
  }
  //コンストラクタ(指定した値に設定)
  Monster(Power power,int powerRate,ConsumptionMode attackMode,ConsumptionMode beAttackedMode) {
    this.power = power;
    if(powerRate < MIN_POWERRATE || MAX_POWERRATE < powerRate) {
      throw new IllegalArgumentException("powerRate");
    }
    this.powerRate = powerRate;
    this.attackMode = attackMode;
    this.beAttackedMode = beAttackedMode;
  }
  //メソッド(攻撃してパワーを消費する)
  void attack() {
    power.value -= attackMode.correct(powerRate);
  }
  //メソッド(攻撃を受けてパワーを消費する)
  void beAttacked() {
    power.value -= beAttackedMode.correct(powerRate);
  }
}
public class MonsterBattleTest {
  public static void main(String[] args){
    System.out.println("************バトル前************");

    try {
      Monster mon0 = new Monster();
      Monster mon1 = new Monster(new Power(195), 20, ConsumptionMode.MostPowerful, ConsumptionMode.Powerful);
      System.out.println("\t■モンスター0のパワー残量:" + mon0.power.value);
      System.out.println("\t■モンスター0のパワー消費モード:" + mon0.attackMode);
      System.out.println("\t□モンスター1のパワー残量:" + mon1.power.value);
      System.out.println("\t□モンスター1のパワー消費モード:" + mon1.attackMode);
    }catch(Exception e) {
      System.out.println("モンスターのパワーの数値が不適切です。");
    }
  }
}
