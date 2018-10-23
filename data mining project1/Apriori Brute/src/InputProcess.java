import java.io.*;

public class InputProcess {

    public static void main(String[] args){
        String in = "IBM-Quest-data.data";
        String ou = "IBM.data";
        //        String in = "IBM-Quest-data.data";
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(in)));
            String line;
            FileOutputStream out = new FileOutputStream(ou);
            PrintStream ps = new PrintStream(out);
            int number=1;
            int flag = 0;
            while ((line = br.readLine()) != null) {
                if (!"".equals(line.trim())) {
                    line = line.replaceAll(" +"," ");
                    System.out.println("line "+line);
                    String[] str = line.split(" ");
                    if (Integer.parseInt(str[1]) == number)
                        if (flag == 0 ){
                            ps.print(str[3]);
                            flag++;
                        }else{
                            ps.print("," + str[3]);
                        }
                    else{
                        ps.println();
                        number ++;
                        ps.print(str[3]);
                        flag=1;
                    }
                }
            }
            br.close();
            ps.close();
        } catch (IOException e) {
            System.err.println("The input file has error");
        }


    }
}

