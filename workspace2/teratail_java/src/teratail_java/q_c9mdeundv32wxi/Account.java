package teratail_java.q_c9mdeundv32wxi;

public class Account {
  /** 金額例外 */
  public static class AmountException extends Exception {
    public final int code;
    public AmountException(int amount, int code) {
      super("amount="+amount+" (code="+code+")");
      this.code = code;
    }
    public AmountException(String amount, int code, Throwable cause) {
      super("amount='"+amount+"' (code="+code+")", cause);
      this.code = code;
    }
  }

  /** 口座名 */
  public final String name;
  /** 残高 */
  private int balance;

  /**
   * 口座
   * @param name 口座名
   */
  public Account(String name) {
    this.name = name;
    balance = 0;
  }

  /**
   * 入金
   * @param amount 金額
   * @throws AmountException 金額が処理できない値の場合
   */
  public void deposit(int amount) throws AmountException {
    if(amount <= 0) throw new AmountException(amount, -3);
    balance += amount;
  }

  /**
   * 出金
   * @param amount 金額
   * @throws AmountException 金額が処理できない値の場合
   */
  public void withdraw(int amount) throws AmountException {
    if(amount <= 0 || balance < amount) throw new AmountException(amount, amount<=0?-3:-1);
    balance -= amount;
  }

  /**
   * 残高参照
   * @return 残高
   */
  public int showBalance() {
    return balance;
  }
}