package FrontEnd;

public class Token {

   private int PoS;
   private String lexeme;
   private int lineNum;
   private static String[] PoSSet = new String[]{"MEMOP", "LOADI", "ARITHOP", "OUTPUT", "NOP", "CONST", "REG", "COMMA", "INTO", "COMMENT", "INVALID", "EOL", "EOF"};

   public Token(int PoS, String lexeme, int lineNum) {
      this.PoS = PoS;
      this.lexeme = lexeme;
      this.lineNum = lineNum;
   }

   public String getPoS() {
      return PoSSet[this.PoS];
   }

   public String toString() {
      return String.format("%d: <%s, %s>", this.lineNum, PoSSet[this.PoS], this.lexeme);
   }

   public static Token EOF(int lineNum) {
      return new Token(12, "EOF", lineNum);
   }
}
