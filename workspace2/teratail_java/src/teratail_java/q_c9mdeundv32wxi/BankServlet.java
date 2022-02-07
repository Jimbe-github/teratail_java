package teratail_java.q_c9mdeundv32wxi;

import java.io.IOException;
import java.io.PrintWriter;

import teratail_java.q_c9mdeundv32wxi.Account.AmountException;
import teratail_java.q_c9mdeundv32wxi.ExtendedBank.AccountException;

public class BankServlet extends HttpServlet {
  private ExtendedBank bank; /* 口座の管理をするオブジェクト */
  public BankServlet() {  /* bankを初期化する */
    bank = new ExtendedBank();
  }

  private void outputSuccessResult(PrintWriter pw, Command command, String... content) {
    outputResult(pw, command.text+"成功", content[0], content.length>=2?content[1]:"");
  }
  private void outputFailResult(PrintWriter pw, Command command, String... content) {
    outputResult(pw, command.text+"失敗", content[0], content.length>=2?content[1]:"");
  }
  private void outputResult(PrintWriter pw, String header, String content, String incidental) {
    content = content.replaceAll("\n", "<br>");
    pw.println("<!DOCTYPE html>"
        +"<html>"
        +"<head><meta charset=\"UTF-8\"></head>"
        +"<body>"
        +"<div class=\"main\"><h1>"+ header +"</h1><h2>"+ content +"</h2>"
        +(incidental == null || incidental.isBlank() ? "" : "<h3>"+incidental+"</h3>")
        +"</div>"
        +"<a class=\"success\" href=\"index.html\">メインメニューに戻る</a>"
        +"</body>"
        +"</html>");
  }

  private enum Command {
    deposit("預金"), withdraw("引出"), balance("残高照会");

    final String text;
    Command(String text) {
      this.text= text;
    }
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String name = request.getParameter("name");
    Command command = Command.valueOf(request.getParameter("command"));
    String amount = request.getParameter("amount");

    response.setContentType("text/html; charset=UTF-8");
    PrintWriter pw = response.getWriter();
    String incidental = "";
    try {
      switch(command) {
      case deposit:
        // 預金処理
        incidental = "口座解約の際は残高を0円にしてから解約手続きを行ってください。";
        bank.deposit(name, amount);
        outputSuccessResult(pw, command, name +"様の口座に"+ amount +"円の預金に成功しました。", incidental);
        break;
      case withdraw:
        //引出処理
        bank.withdraw(name, amount);
        outputSuccessResult(pw, command, name +"様の口座から"+ amount +"円の引出に成功しました。");
        break;
      case balance:
        // 残高照会処理
        outputSuccessResult(pw, command, name +"様の口座残高は"+ bank.showBalance(name));
        break;
      }
    } catch(AccountException e) {
      outputFailResult(pw, command, "口座は存在しません。");
    } catch(AmountException e) {
      String reason =
          e.code == -1 ? "残高を超える金額は"+ command.text +"できません。"
          : e.code == -3 ? "0以下の金額は金額は"+ command.text +"できません。"
          : "整数以外の入力を確認しました。0以上の整数を入力してください。";
      outputFailResult(pw, command, "失敗しました。\n"+ reason, incidental);
    }
  }
}