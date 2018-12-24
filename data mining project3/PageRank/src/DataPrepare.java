import java.io.*;
import java.util.Arrays;

import static java.lang.Integer.parseInt;

public class DataPrepare {
    public static void main(String[] args) {
        String inputpath = "./dataset/IBM_data.txt";
        String outputpath1 = "./dataset/graph_7.txt";
        String outputpath2 = "./dataset/graph_8.txt";
        int  max=0;
        int num=0;

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(inputpath)));
            String line;
            //FileOutputStream out = new FileOutputStream(outputpath1);
            FileOutputStream out = new FileOutputStream(outputpath2);
            PrintStream ps = new PrintStream(out);

            while ((line = br.readLine()) != null) {
                String[] str = line.split(",");
                for (int i=0;i<str.length;i++){
                    if (max <= parseInt(str[i])){
                        max=parseInt(str[i]);
                    }
                    for (int j=i+1;j<str.length;j++){
                        ps.println(str[i]+","+str[j]);
                        ps.println(str[j]+","+str[i]);
                    }
                }
                num = num+str.length;

               // System.out.println("Total nodes are "+num);
               // System.out.println("max is "+max);
            }
            br.close();
            ps.close();
        } catch (IOException e) {
            System.err.println("The input file has error");
        }



    }
}
