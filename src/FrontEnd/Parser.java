package src.FrontEnd;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

public class Parser {

    public int opNum;
    private Scanner scanner;
    private Map<String, Opcode> lexemeToOp = new HashMap<>();
    private Token t = Token.nullToken;
    

    public Parser(Scanner scanner) {
        this.scanner = scanner;
        lexemeToOp.put("load", Opcode.LOAD);
        lexemeToOp.put("store", Opcode.STORE);
        lexemeToOp.put("loadI", Opcode.LOADI);
        lexemeToOp.put("add", Opcode.ADD);
        lexemeToOp.put("sub", Opcode.SUB);
        lexemeToOp.put("mult", Opcode.MULT);
        lexemeToOp.put("lshift", Opcode.LSHIFT);
        lexemeToOp.put("rshift", Opcode.RSHIFT);
        lexemeToOp.put("output", Opcode.OUTPUT);
        lexemeToOp.put("nop", Opcode.NOP);
    }

    public IR parse() throws IOException {
        IR dummy = IR.nullIR;
        IR cur = dummy;
        IR ir = IR.nullIR;
        while (true) {
            t = this.scanner.nextToken();
            if (t.getPoS() == "EOF") {
                break;
            }
            // System.out.printf("Processing %s\n", t);
            switch (t.getPoS()) {
                case "MEMOP": 
                    ir = finishMEMOP();
                    break;
                case "LOADI":
                    ir = finishLOADI();
                    break;
                case "ARITHOP":
                    ir = finishARITHOP();
                    break;
                case "OUTPUT":
                    ir = finishOUTPUT();
                    break;
                case "NOP":
                    ir = finishNOP();
                    break;
                default: 
                    ir = IR.nullIR;
            }
            if (ir.equals(IR.nullIR)) {
                skip(); // Skip this line
                // System.out.printf("Skipping line %d\n", t.getLineNum());
                continue;
            }

            // Build IR list
            // System.out.printf("Successfully build ir: %s\n", ir);
            opNum++;
            cur.next = ir;
            ir.prev = cur;
            cur = ir;
        }
        return dummy.next;
    }

    // Skip to next line
    private void skip() throws IOException {
        while (!t.getPoS().equals("EOL")) {
            t = this.scanner.nextToken();
        }
    }

    private IR finishMEMOP() throws IOException {
        Token copy = t;
        t = this.scanner.nextToken();
        if (t.getPoS() != "REG") {
            reportError(t, "Unexpected Token " + t.getLexeme() + ". Expect a REG token.");
            return IR.nullIR;
        }
        int reg1 = Integer.parseInt(t.getLexeme().substring(1));
        t = this.scanner.nextToken();
        if (t.getPoS() != "INTO") {
            reportError(t, "Unexpected Token " + t.getLexeme() + ". Expect a INTO token.");
            return IR.nullIR;
        }
        t = this.scanner.nextToken();
        if (t.getPoS() != "REG") {
            reportError(t, "Unexpected Token " + t.getLexeme() + ". Expect a REG token.");
            return IR.nullIR;
        }
        int reg2 = Integer.parseInt(t.getLexeme().substring(1));
        t = this.scanner.nextToken();
        if (t.getPoS() != "EOL" && t.getPoS() != "COMMENT") {
            reportError(t, "Unexpected Token " + t.getLexeme());
            return IR.nullIR;
        }
        IR ir = new IR(t.getLineNum(), lexemeToOp.get(copy.getLexeme()));
        ir.operands[0] = reg1;
        ir.operands[8] = reg2;
        return ir;
    }

    private IR finishLOADI() throws IOException {
        Token copy = t;
        t = this.scanner.nextToken();
        if (t.getPoS() != "CONST") {
            reportError(t, "Unexpected Token " + t.getLexeme() + ". Expect a CONST token.");
            return IR.nullIR;
        }
        int constant = Integer.parseInt(t.getLexeme());
        t = this.scanner.nextToken();
        if (t.getPoS() != "INTO") {
            reportError(t, "Unexpected Token " + t.getLexeme() + ". Expect a INTO token.");
            return IR.nullIR;
        }
        t = this.scanner.nextToken();
        if (t.getPoS() != "REG") {
            reportError(t, "Unexpected Token " + t.getLexeme() + ". Expect a REG token.");
            return IR.nullIR;
        }
        int reg1 = Integer.parseInt(t.getLexeme().substring(1));
        t = this.scanner.nextToken();
        if (t.getPoS() != "EOL" && t.getPoS() != "COMMENT") {
            reportError(t, "Unexpected Token " + t.getLexeme());
            return IR.nullIR;
        }
        IR ir = new IR(t.getLineNum(), lexemeToOp.get(copy.getLexeme()));
        ir.operands[0] = constant;
        ir.operands[8] = reg1;
        // System.out.println("In finishLOADI: " +ir);
        return ir;
    }

    private IR finishARITHOP() throws IOException {
        Token copy = t;
        t = this.scanner.nextToken();
        if (t.getPoS() != "REG") {
            reportError(t, "Unexpected Token " + t.getLexeme() + ". Expect a REG token.");
            return IR.nullIR;
        }
        int reg1 = Integer.parseInt(t.getLexeme().substring(1));
        t = this.scanner.nextToken();
        if (t.getPoS() != "COMMA") {
            reportError(t, "Unexpected Token " + t.getLexeme() + ". Expect a COMMA token.");
            return IR.nullIR;
        }
        t = this.scanner.nextToken();
        if (t.getPoS() != "REG") {
            reportError(t, "Unexpected Token " + t.getLexeme() + ". Expect a REG token.");
            return IR.nullIR;
        }
        int reg2 = Integer.parseInt(t.getLexeme().substring(1));
        t = this.scanner.nextToken();
        if (t.getPoS() != "INTO") {
            reportError(t, "Unexpected Token " + t.getLexeme() + ". Expect a INTO token.");
            return IR.nullIR;
        }
        t = this.scanner.nextToken();
        if (t.getPoS() != "REG") {
            reportError(t, "Unexpected Token " + t.getLexeme() + ". Expect a REG token.");
            return IR.nullIR;
        }
        int reg3 = Integer.parseInt(t.getLexeme().substring(1));
        t = this.scanner.nextToken();
        if (t.getPoS() != "EOL" && t.getPoS() != "COMMENT") {
            reportError(t, "Unexpected Token " + t.getLexeme());
            return IR.nullIR;
        }
        IR ir = new IR(t.getLineNum(), lexemeToOp.get(copy.getLexeme()));
        ir.operands[0] = reg1;
        ir.operands[4] = reg2;
        ir.operands[8] = reg3;
        return ir;
    }

    private IR finishOUTPUT() throws IOException {
        t = this.scanner.nextToken();
        if (t.getPoS() != "CONST") {
            reportError(t, "Unexpected Token " + t.getLexeme() + ". Expect a CONST token.");
            return IR.nullIR;
        }
        int constant = Integer.parseInt(t.getLexeme());
        t = this.scanner.nextToken();
        if (t.getPoS() != "EOL" && t.getPoS() != "COMMENT") {
            reportError(t, "Unexpected Token " + t.getLexeme());
            return IR.nullIR;
        }
        IR ir = new IR(t.getLineNum(), Opcode.OUTPUT);
        ir.operands[0] = constant;
        return ir;
    }

    private IR finishNOP() throws IOException {
        t = this.scanner.nextToken();
        if (t.getPoS() != "EOL" && t.getPoS() != "COMMENT") {
            reportError(t, "Unexpected Token " + t.getLexeme());
            return IR.nullIR;
        }
        return new IR(t.getLineNum(), Opcode.NOP);
    }

    private void reportError(Token t, String msg) {
        System.err.printf("ERROR %d: %s \n", t.getLineNum(), msg);
    }

}

