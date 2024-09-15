package src.FrontEnd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class Main {

   private static List<String> flag = new ArrayList<>();
   private static List<String> filename = new ArrayList<>();

   public Main() {}

   // public static void main(String[] args) throws IOException {

   //    BufferedReader reader;
   //    try {
   //       reader = new BufferedReader(new FileReader("FrontEnd/test_inputs/t7.i"));
   //    } catch (Exception var8) {
   //       System.err.println("ERROR: Unable to open the file.");
   //       return;
   //    }

   //    Scanner scanner = new Scanner(reader);
   //    Token t = new Token(0, null, 0);
   //    // while (!t.getPoS().equals("EOF")) {
   //    //    t = scanner.nextToken();
   //    //    System.out.println(t);
   //    // }
   //    Parser parser = new Parser(scanner);
   //    IR ir = parser.parse();
   //    printList(ir);
   //    return;
      
   // }

   public static void main(String[] args) throws IOException {
      parseArgs(args);
      // System.out.println(flag);
      // for (int i=0; i<filename.size(); i++) {
      //    System.out.println(filename.get(i));
      // }
      if (flag.size() > 1) {
         System.err.print("ERROR: Unrecognized flag ");
         for (int i=1; i<flag.size(); i++) {
            System.err.print(flag.get(i) + " ");
         }
         System.err.println();
         hint();
         return;
      }

      if (flag.get(0) == null) {
         flag.set(0, "-p");
      }
      
      if (filename.size() > 1 && flag.get(0) != "-h") {
         System.err.println("ERROR: Multiple file names found. Please provide one file name.");
         return;
      }
      if (filename.size() == 0 && flag.get(0) != "-h") {
         System.err.println("ERROR: No file names found. Please provide one file name.");
         return;
      }
      switch (flag.get(0)) {
         case "-h":
            hint();
            break;
         case "-r":
            doR();
            break;
         case "-p":
            doP();
            break;
         case "-s":
            doS();
            break;
         default:
            System.err.println("ERROR: Invalid execution state.");
            return;
      }
   }

   private static void parseArgs(String[] args) {
      flag.add(null);
      for (int i = 0; i < args.length; i++) {
         String arg = args[i];
         switch (arg) {
            case "-h":
               flag.set(0,"-h"); 
               break; // Ignore all other flags
            case "-s":
            case "-p":
            case "-r":
               flag.set(0, flag.get(0) == null ? arg : comparePriority(flag.get(0), arg));
               break;
            default:
               if (arg.startsWith("-")) { 
                  flag.add(arg);
               } else {
                  filename.add(arg);
               }
         }
      }
   }

   // Helper to compare the priority of flags
   private static String comparePriority(String oldFlag, String newFlag) {
         if (oldFlag.equals("-h")) {
            return oldFlag;
         } else if (oldFlag.equals("-r")) {
            if (newFlag.equals("-h")) return newFlag;
            return oldFlag;
         } else if (oldFlag.equals("-p")) {
            if (newFlag.equals("-h") || newFlag.equals("-r")) return newFlag;
            return oldFlag;
         } else if (oldFlag.equals("-s")) {
            return newFlag;
         }
         return oldFlag;
   }

   private static void hint() {
      String msg = """
      -h: Show hints of avaiable flags
      -s <name>: Read the file specified by <name> and print a list of the tokens that the scanner found.
      -p <name>: Read the file specified by <name>, scan it and parse it, build the intermediate representation,
                 and report either success or report all the errors that it finds in the input file.
      -r <name>: Read the file specified by <name>, scan it and parse it, build the intermediate representation,
                 and print out the information in the intermediate representation.
      When no flag is specified, the default action is -p.
      """;
      System.out.println(msg);
   }

   private static void doP() throws IOException {

      BufferedReader reader;
      try {
         reader = new BufferedReader(new FileReader(filename.get(0)));
      } catch (Exception var8) {
         System.err.println("ERROR: Unable to open the file.");
         return;
      }
      
      Scanner scanner = new Scanner(reader);
      Parser parser = new Parser(scanner);
      IR ir = parser.parse();
      if (ir != null && !ir.equals(IR.nullIR)) {
         System.out.printf("Parse succeeded. Processed %d operations.\n", parser.opNum);
      } else {
         System.err.println("Parse found errors.");
      }
   }

   private static void doS() throws IOException {

      BufferedReader reader;
      try {
         reader = new BufferedReader(new FileReader(filename.get(0)));
      } catch (Exception var8) {
         System.err.println("ERROR: Unable to open the file.");
         return;
      }
      Scanner scanner = new Scanner(reader);
      Token t = new Token(0, "null", 0);
      while (!t.getPoS().equals("EOF")) {
         t = scanner.nextToken();
         System.out.println(t);
      }
   }

   private static void doR() throws IOException {

      BufferedReader reader;
      try {
         reader = new BufferedReader(new FileReader(filename.get(0)));
      } catch (Exception var8) {
         System.err.println("ERROR: Unable to open the file.");
         return;
      }

      Scanner scanner = new Scanner(reader);
      Parser parser = new Parser(scanner);
      IR ir = parser.parse();
      while (ir != null && !ir.equals(IR.nullIR)) {
         System.out.println(ir);
         ir = ir.next;
      }
   }

}
