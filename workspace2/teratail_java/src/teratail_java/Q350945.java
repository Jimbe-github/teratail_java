package teratail_java;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Q350945 {

    private static class Manager {
        private String samples[];
        private String lines = "";
        Manager(String sample) {
            samples = sample.split(","); //サンプルを分解して"必要な件数"を求める
        }
        String[] getSamples() { return samples; }

        /** 行を溜め込む。必要な件数分溜ったら配列にして返し、溜め込んだデータをクリアする。 */
        String[] put(String line) {
            lines += line; //溜め込み
            String[] values = lines.split(","); //分解してみる
            if(values.length == samples.length) {
                lines = "";
                return values;
            }
            lines += System.lineSeparator();
            return null;
        }
    }

    public static void main(String[] args) {
        try(FileReader r = new FileReader(new File("database.csv"));
                BufferedReader in = new BufferedReader(r);
                FileWriter w = new FileWriter(new File("datashopify.csv"));
                BufferedWriter out = new BufferedWriter(w);) {

            String sample = in.readLine();
            Manager m = new Manager(sample);
            String[] samples = m.getSamples();
            out.write(samples[0]+","+samples[2]+","+samples[1]+",");
            out.newLine();

            for(String line; (line=in.readLine()) != null; ) {
                String[] data = m.put(line);
                if(data != null) {
                    out.write(data[0]+","+data[2]+","+data[1]+",");
                    out.newLine();
                    System.out.println(data[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}