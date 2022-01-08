package teratail_java.q373056;

import java.util.*;

public class Portfolio implements Iterable<Position> {
  private Position[] positions = new Position[0];
  private int length = 0;
  public void addPosition(Position p) {
    Position exists = findPosition(p.getIssue());
    if(exists == null) {
      if(length >= positions.length) expandArray();
      positions[length++] = p;
    } else {
      exists.addAmount(p.getAmount());
    }
  }
  private void expandArray() {
    Position[] newArray = new Position[positions.length + 5];
    System.arraycopy(positions, 0, newArray, 0, length);
    positions = newArray;
  }
  private Position findPosition(Issue issue) {
    for(Position p : this) if(p.getIssue().equals(issue)) return p;
    return null;
  }
  public int getLength() { return length; }
  public Position getPosition(int index) {
    if(index < 0 || length <= index) throw new ArrayIndexOutOfBoundsException(index);
    return positions[index];
  }
  @Override
  public Iterator<Position> iterator() {
    return new Iterator<Position>() {
      private int index = 0;
      @Override
      public Position next() {
        return positions[index++];
      }
      @Override
      public boolean hasNext() {
        return index < length;
      }
    };
  }

  public static void main(String[] args) {
    Position positionStock1 = new Position(new Stock("92010", "A", Stock.Market.TSE), 0.1);
    Position positionStock2 = new Position(new Stock("68610", "B", Stock.Market.OSE), 0.2);
    Position positionStock3 = new Position(new Stock("72030", "C", Stock.Market.NSE), 0.3);
    Position positionBond1 = new Position(new Bond("00611273", "D", 20310907, 0.110), 0.075);
    Position positionBond2 = new Position(new Bond("33070153", "F", 20261120, 0.001), 0.120);
    Position positionBond3 = new Position(new Bond("090650948", "G", 20231120, 0.001), 0.205);

    Portfolio portfolio = new Portfolio();
    portfolio.addPosition(positionStock1);
    portfolio.addPosition(positionStock2);
    portfolio.addPosition(positionStock3);
    portfolio.addPosition(positionBond1);
    portfolio.addPosition(positionBond2);
    portfolio.addPosition(positionBond3);

    for(Position p : portfolio) {
      System.out.println(p);
    }
  }
}

class Issue {
  protected String code;
  protected String name;
  Issue(String code, String name) {
    if(code == null || name == null) throw new NullPointerException();
    this.code = code;
    this.name = name;
  }
  String getCode() { return code; }
  String getName() { return name; }

  @Override
  public int hashCode() {
    return Objects.hash(code, name);
  }
  @Override
  public boolean equals(Object obj) {
    if(this == obj) return true;
    if(obj == null) return false;
    if(getClass() != obj.getClass()) return false;
    Issue other = (Issue) obj;
    return code.equals(other.code) && name.equals(other.name);
  }
}

class Stock extends Issue {
  enum Market {
    TSE, OSE, NSE;
  }
  private Market market;
  Stock(String code, String name, Market market) {
    super(code, name);
    if(market == null) throw new NullPointerException();
    this.market = market;
  }
  @Override
  public String toString() {
    return new StringJoiner(",","Stock [","]")
        .add("code="+code)
        .add("name="+name)
        .add("Market="+market)
        .toString();
  }
}

class Bond extends Issue {
  private int maturity;
  private double coupon;

  Bond(String code, String name, int maturity, double coupon) {
    super(code, name);
    this.maturity = maturity;
    this.coupon = coupon;
  }
  @Override
  public String toString() {
    return new StringJoiner(",","Bond [","]")
        .add("code="+code)
        .add("name="+name)
        .add("maturity="+maturity)
        .add("coupon="+coupon)
        .toString();
  }
}

class Position {
  private Issue issue;
  private double amount;

  Position() {}
  Position(Issue issue, double amount) {
    setIssue(issue);
    setAmount(amount);
  }

  void setIssue(Issue issue) { this.issue = issue; }
  Issue getIssue() { return issue; }

  void setAmount(double amount) { this.amount = amount; }
  void addAmount(double amount) { this.amount += amount; }
  double getAmount() { return amount; }

  @Override
  public String toString() {
    return new StringJoiner(",","Position [","]")
        .add("issue="+issue)
        .add("amount="+amount)
        .toString();
  }
}