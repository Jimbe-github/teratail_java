package teratail_java;

import java.util.Scanner;

public class Mondai {
    public static void main(String[] args) {
        String str, target, replacement;

        // sc :キーボードから行入力するためのオブジェクト
        try(Scanner sc = new Scanner(System.in);) {

            //文字列を入力
            System.out.println("文字列を入力してください");
            str = sc.nextLine();

            //置き換え元の文字列の入力
            System.out.println("置換元の文字列を入力してください");
            target = sc.nextLine();

            //置き換え先の文字列を入力
            System.out.println("置換先の文字列を入力してください");
            replacement = sc.nextLine();

        } // sc はもう使わないので閉じる

        //置き換え
        char[] strChars = str.toCharArray(); //これで str の全文字が char 型になります
        char targetChar = target.charAt(0); //置換元文字列の最初の文字
        char replacementChar = replacement.charAt(0); //置換先文字列の最初の文字
        for(int i=0; i<strChars.length; i++) {
            if(strChars[i] == targetChar) strChars[i] = replacementChar;
        }
        str = new String(strChars);

        //結果を出力
        System.out.println("置換結果です");
        System.out.println(str);
    }
}
