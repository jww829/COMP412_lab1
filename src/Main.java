package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

   private static List<String> flag = new ArrayList<>();
   private static List<String> filename = new ArrayList<>();

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
         case "-x":
            doX();
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
            case "-x":
               flag.set(0, "-x");
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

   private static void doX() throws IOException {
      BufferedReader reader;
      try {
         reader = new BufferedReader(new FileReader(filename.get(0)));
      } catch (IOException e) {
         System.err.println("ERROR: Unable to open the file.");
         return;
      }

      Scanner scanner = new Scanner(reader);
      Parser parser = new Parser(scanner);
      IR ir = parser.parse();
      // IRList.printIRList(ir);
      // System.out.println("--------------------------------");
      Renamer renamer = new Renamer();
      renamer.rename(ir, scanner.maxSR);
      IRList.printILOC(ir);
      return;
   }

}
