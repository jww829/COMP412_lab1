package src;

public class IR {

    public IR next = nullIR;
    public IR prev = nullIR;
    public int[] operands = new int[12];
    // public int lineNum = 0;
    public Opcode opcode;
    
    public static final IR nullIR = new IR(Opcode.NULL) {

        public String toString() {
            return "This is a null IR";
        }
    };

    public IR(Opcode opcode) {
        // this.lineNum = lineNum;
        this.opcode = opcode;
        for (int i=0; i<operands.length; i++) {
            operands[i] = -1;
        }
    }

    public String toString() {
        return String.format("%s %s", this.opcode, printOperands());
    }

    public String ILOC(String r) { // Default = sr
        String res = "";
        int x = 0, y = 4, z = 8;
        if (r.equals("vr")) {
            x++; y++; z++;
        } else if (r.equals("pr")) {
            x+=2; y+=2; z+=2;
        }
        if (this.opcode == Opcode.STORE) {
            res += "store" + "\tr";
            res += this.operands[x];
            res += "\t=>\tr";
            res += this.operands[z];
        } else if (this.opcode == Opcode.LOADI) {
            res += "loadI" + "\t";
            res += this.operands[0];
            res += "\t=>\tr";
            res += this.operands[z];
        } else if (this.opcode == Opcode.OUTPUT) {
            res += "output" + "\t";
            res += this.operands[0];
        } else if (this.opcode == Opcode.NOP) {
            res += "nop";
        } else {
            res += this.opcode.toString().toLowerCase() + "\tr" + this.operands[x];
            if (this.operands[y] != -1) {
                res += ", r";
                res += this.operands[y];
            }
            res += "\t=>\tr";
            res += this.operands[z];
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

