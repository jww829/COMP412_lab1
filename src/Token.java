package src;

public class Token {

   private int PoS;
   private String lexeme;
   private int lineNum;
   private static String[] PoSSet = new String[]{"MEMOP", "LOADI", "ARITHOP", "OUTPUT", "NOP", "CONST", "REG", "COMMA", "INTO", "COMMENT", "INVALID", "EOL", "EOF"};

   public static Token nullToken = new Token(-1, "NULL", -1) {
      public String getPoS() {
         return "NULL";
      }

      public String toString() {
         return "<NULL>";
      }
   };

   public Token(int PoS, String lexeme, int lineNum) {
      this.PoS = PoS;
      this.lexeme = lexeme;
      this.lineNum = lineNum;
   }

   public int getLineNum() {
      return this.lineNum;
   }

   public String getPoS() {
      return PoSSet[this.PoS];
   }

   public String getLexeme() {
      return this.lexeme;
   }

   public String toString() {
      String l = this.lexeme;
      if (this.PoS == 11) {
         l = "\\n";
      }
      return String.format("%d: <%s, %s>", this.lineNum, PoSSet[this.PoS], l);
   }

   public static Token EOF(int lineNum) {
      return new Token(12, "EOF", lineNum);
   }

}
