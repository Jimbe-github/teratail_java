package teratail_java.q_ma9cgl882hfegi;

//機械
class Machine {
  final String name;
  final int output; //ワット
  final OilType oilType;
  final int oilAmount;

  Machine(String name, int output, OilType oilType, int oilAmount) {
    this.name = name;
    this.output = output;
    this.oilType = oilType;
    this.oilAmount = oilAmount;
  }

  @Override
  public String toString() {
    return "name="+name+", output="+output+", oilType="+oilType+", oilAmount="+oilAmount;
  }
}