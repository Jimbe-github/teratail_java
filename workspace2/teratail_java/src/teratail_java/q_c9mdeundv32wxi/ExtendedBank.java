package teratail_java.q_c9mdeundv32wxi;

import java.util.HashMap;

import teratail_java.q_c9mdeundv32wxi.Account.AmountException;

public class ExtendedBank {
  /** 口座例外 */
  public static class AccountException extends Exception {
    public final int code;
    public AccountException(String message, int code) {
      super(message+" (code="+code+")");
      this.code = code;
    }
  }

  /** 口座マップ */
  private HashMap<String,Account> customer = new HashMap<>();

  /**
   * 口座オブジェクトを取り出す
   * @param name 口座名
   * @return Account オブジェクト
   * @throws AccountException　口座が無かった場合
   */
  private Account getAccount(String name) throws AccountException {
    Account account = customer.get(name);
    if(account == null) throw new AccountException(name+" NOT exists.", -7);
    return account;
  }

  /**
   * 金額文字列数値化
   * @param amount 金額
   * @throws AmountException 金額が処理出来ない場合
   */
  private int parseAmount(String amount) throws AmountException {
    try {
      return Integer.parseInt(amount);
    } catch(NumberFormatException e) {
      throw new AmountException(amount, -4, e);
    }
  }

  /**
   * 口座開設
   * @param name 口座名
   * @throws AccountException 指定口座が既に有った場合
   */
  public void open(String name) throws AccountException {
    if(customer.containsKey(name)) throw new AccountException(name+" exists.", -7);
    customer.put(name, new Account(name));
  }

  /**
   * 口座解約
   * @param name 口座名
   * @throws AccountException 指定口座が無かった又は残高が残っている場合
   */
  public void close(String name) throws AccountException {
    Account account = getAccount(name);
    if(account.showBalance() != 0) throw new AccountException(name+" has balance.", -1);
    customer.remove(name);
  }

  /**
   * 預金
   * @param name 口座名
   * @param amount 金額
   * @throws AccountException 指定口座が無かった場合
   * @throws AmountException 金額が処理出来ない場合
   */
  public void deposit(String name, String amount) throws AccountException, AmountException {
    getAccount(name).deposit(parseAmount(amount));
  }

  /**
   * 預金
   * @param name 口座名
   * @param amount 金額
   * @throws AccountException 指定口座が無かった場合
   * @throws AmountException 金額が処理出来ない場合
   */
  public void deposit(String name, int amount) throws AccountException, AmountException {
    getAccount(name).deposit(amount);
  }

  /**
   * 引き出し
   * @param name 口座名
   * @param amount 金額
   * @throws AccountException 指定口座が無かった場合
   * @throws AmountException 金額が処理出来ない場合
   */
  public void withdraw(String name, String amount) throws AccountException, AmountException {
    getAccount(name).withdraw(parseAmount(amount));
  }

  /**
   * 引き出し
   * @param name 口座名
   * @param amount 金額
   * @throws AccountException 指定口座が無かった場合
   * @throws AmountException 金額が処理出来ない場合
   */
  public void withdraw(String name, int amount) throws AccountException, AmountException {
    getAccount(name).withdraw(amount);
  }

  /**
   * 残高照会
   * @param name 口座名
   * @return 預金残高
   * @throws AccountException 指定口座が無かった場合
   */
  public int showBalance(String name) throws AccountException {
    return getAccount(name).showBalance();
  }
}