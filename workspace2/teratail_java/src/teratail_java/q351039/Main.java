package teratail_java.q351039;

import java.time.MonthDay;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try(Scanner scanner = new Scanner(System.in);) {
            System.out.println("**********星座判定**********");
            System.out.println("誕生年を入力してください");
            int year = scanner.nextInt();
            System.out.println("誕生月を入力してください");
            int month = scanner.nextInt();
            System.out.println("誕生日を入力してください");
            int dayOfMonth = scanner.nextInt();

            Constellation cons = Constellation.get(MonthDay.of(month, dayOfMonth));

            System.out.println(year+"年"+month+"月"+dayOfMonth+"日生まれの人の星座は"+cons.getName());
        }
    }
}
abstract class LogicalOp {
    static LogicalOp and = new LogicalOp() { public boolean op(boolean a, boolean b) { return a && b; } };
    static LogicalOp or  = new LogicalOp() { public boolean op(boolean a, boolean b) { return a || b; } };

    abstract boolean op(boolean a, boolean b);
}
enum Constellation {
    Aries("牡羊座", 3, 21, 4, 19),
    Taurus("牡牛座", 4, 20, 5, 20),
    Gemini("双子座", 5, 21, 6, 21),
    Cancer("蟹座", 6, 22, 7,22),
    Leo("獅子座", 7, 23, 8, 22),
    Virgo("乙女座", 8, 23, 9, 22),
    Libra("天秤座", 9, 23, 10, 23),
    Scorpio("蠍座", 10,24, 11, 22),
    Sagittarius("射手座", 11, 23, 12, 21),
    Capricorn("山羊座", 12, 22, 1,19, LogicalOp.and),
    Aquarius("水瓶座", 1, 20, 2, 18),
    Pisces("魚座", 2, 19, 3, 20);

    private String name;
    private MonthDay start, end;
    private LogicalOp lop;

    private Constellation(String name, int startMonth, int startDay, int endMonth, int endDay) {
        this(name, startMonth, startDay, endMonth, endDay, LogicalOp.or);
    }
    private Constellation(String name, int startMonth, int startDay, int endMonth, int endDay, LogicalOp lop) {
        this.name = name;
        start = MonthDay.of(startMonth, startDay);
        end = MonthDay.of(endMonth, endDay);
        this.lop = lop;
    }

    public String getName() { return name; }
    public MonthDay getStart() { return start; }
    public MonthDay getEnd() { return end; }

    public boolean match(MonthDay target) {
        return !lop.op(target.isBefore(start), target.isAfter(end)); //Not 範囲外
    }

    public static Constellation get(MonthDay target) {
        for (Constellation cons : values()) if(cons.match(target)) return cons;
        throw new UnsupportedOperationException("日付がデータ範囲に無い: taret="+target); //Error のほうがいい？
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("[name=").append(name);
        sb.append(",start=").append(start);
        sb.append(",end=").append(end);
        return sb.append("]").toString();
    }
}