package src;

public class IR {

    public IR next = nullIR;
    public IR prev = nullIR;
    public int[] operands = new int[12];
    public int lineNum = 0;
    public Opcode opcode;
    
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
    }

    public String ILOC() {
        String res = "";
        if (this.opcode == Opcode.STORE) {
            res += "store" + "\tr";
            res += this.operands[0];
            res += "\t=>\tr";
            res += this.operands[8];
        } else if (this.opcode == Opcode.LOADI) {
            res += "loadI" + "\t";
            res += this.operands[0];
            res += "\t=>\tr";
            res += this.operands[8];
        } else if (this.opcode == Opcode.OUTPUT) {
            res += "output" + "\t";
            res += this.operands[0];
        } else if (this.opcode == Opcode.NOP) {
            res += "nop";
        } else {
            res += this.opcode.toString().toLowerCase() + "\tr" + this.operands[0];
            if (this.operands[4] != -1) { // There's a second operation
                res += ", r";
                res += this.operands[4];
            }
            res += "\t=>\tr";
            res += this.operands[8];
        }
        return res;
    }

    private String printOperands() {
        String s = "";
        for (int i=0; i<operands.length; i++) {
            s += (operands[i] + " ");
        } 
        return s;
    }

}

