package FrontEnd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

   public Main() {}

   public static void main(String[] args) throws IOException {
      
      System.out.println("\0");

      BufferedReader reader;
      try {
         reader = new BufferedReader(new FileReader("FrontEnd/test_inputs/t2.i"));
      } catch (Exception var8) {
         System.err.println("ERROR: Unable to open the file.");
         return;
      }

      Scanner scanner = new Scanner(reader);
      Token t = new Token(0, "0", 0);
      while (t.getPoS() != "EOF") {
         t = scanner.nextToken();
         System.out.println(t);
      }
   }

   private static void print() {
      String msg = "Required arguments:\n\t filename  is the pathname (absolute or relative) to the input file\n\nOptional flags:\n\t -h \t prints this messageAt most one of the following three flags:\n\t -x k";
      System.out.println(msg);
   }
}
