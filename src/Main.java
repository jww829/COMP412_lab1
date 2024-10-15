package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

   private static List<String> flag = new ArrayList<>();
   private static List<String> filename = new ArrayList<>();
   private static List<Integer> numPR = new ArrayList<>();

   // public static void main(String[] args) throws IOException {
   //    doX();
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

      if (flag.get(0) == "-h") {
         hint();
         return;
      }
      
      if (filename.size() > 1) {
         System.err.println("ERROR: Multiple file names found. Please provide one file name.");
         hint();
         return;
      }
      if (filename.size() == 0) {
         System.err.println("ERROR: No file names found. Please provide one file name.");
         hint();
         return;
      }
      if (flag.get(0) == "-x") {
         if (numPR.size() > 0) {
            System.err.println("ERROR: Unsupported combination of arguments.");
            hint();
            return;
         } else {
            doX();
            return;
         }
      }
      if (numPR.size() > 1) {
         System.err.println("ERROR: Multiple numbers found. Please provide only one number.");
         hint();
         return;
      }
      if (numPR.size() == 0) {
         System.err.println("ERROR: No number found. Please provide one number.");
         hint();
         return;
      }
      if (numPR.get(0) < 3 || numPR.get(0) > 64) {
         System.err.println("ERROR: Invalid number of registers.");
         hint();
         return;
      }
      doK();
   }

   private static void parseArgs(String[] args) {
      flag.add(null);
      for (int i = 0; i < args.length; i++) {
         String arg = args[i];
         switch (arg) {
            case "-h":
               flag.set(0,"-h"); 
               break; 
            case "-x":
               if (flag.get(0) == "-h") break;
               flag.set(0, "-x");
               break;  
            default:
               if (arg.startsWith("-")) { 
                  flag.add(arg);
               } else {
                  try {
                     numPR.add(Integer.parseInt(arg));
                  } catch (NumberFormatException e) {
                     filename.add(arg);
                  }
               }
         }
      }
   }


   private static void hint() {
      String msg = """
      -h: Show hints of avaiable flags
      -x <name>: Read the file specified by <name>, scan it and parse it, and then perform renaming on the code
                 in the input block and print the results to the standard output stream.
      k <name>:  k is the number of registers available to the allocator (3 ≤ k ≤ 64) and <name> is a pathname 
                 to the file containing the input block. If the parameters are valid, 412alloc should scan parse,
                 and allocate the code in the input block so that it uses only registers r0 to rk-1 and print 
                 the resulting code to the standard output stream.
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
      IRList irl = new IRList(parser.parse());
      Renamer renamer = new Renamer();
      renamer.rename(irl, scanner.maxSR);
      irl.printILOC("vr", false);
      return;
   }

   private static void doK() throws IOException {
      BufferedReader reader;
      try {
         reader = new BufferedReader(new FileReader(filename.get(0)));
      } catch (IOException e) {
         System.err.println("ERROR: Unable to open the file.");
         return;
      }

      Scanner scanner = new Scanner(reader);
      Parser parser = new Parser(scanner);
      IRList irl = new IRList(parser.parse());
      Renamer renamer = new Renamer();
      renamer.rename(irl, scanner.maxSR);
      // irl.printILOC("vr", true);
      // System.out.println("-----------------------------------");
      // System.out.println("MaxLive = " + renamer.maxLive);
      // System.out.println("-----------------------------------");
      Allocator allocator = new Allocator(numPR.get(0), renamer.vr, 10, irl);
      allocator.allocate();
      irl.printILOC("pr", false);
      return;
   }

}
