package src.FrontEnd;

public class IR {

    public IR next = nullIR;
    public IR prev = nullIR;
    public int[] operands = new int[12];
    private int lineNum = 0;
    private Opcode opcode;
    
    public static final IR nullIR = new IR(-1, Opcode.NULL) {

        public String toString() {
            return "This is a null IR";
        }
    };

    public IR(int lineNum, Opcode opcode) {
        this.lineNum = lineNum;
        this.opcode = opcode;
        for (int i=0; i<operands.length; i++) {
            operands[i] = -1;
        }
    }

    public String toString() {
        return String.format("%d: %s %s", this.lineNum, this.opcode, printOperands());
        // return String.format("%d: %s %s\n", this.lineNum, this.opcode, printOperands());
    }

    private String printOperands() {
        String s = "";
        for (int i=0; i<operands.length; i++) {
            s += (operands[i] + " ");
        } 
        return s;
    }

}

