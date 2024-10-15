package src;

public class Renamer {

    public int vr = 0;
    public int maxLive = 0;
    
    public Renamer() {}

    public void rename(IRList irl, int numSR) {
        IR ir = irl.tail;
        int l = irl.size;
        int live = 0; 
        int[] SR2VR = new int[numSR + 1];
        int[] LU = new int[numSR+ 1]; // Last Use


        for (int i=0; i<=numSR; i++) {
            SR2VR[i] = -1;
            LU[i] = -1;
        }

        while (ir != IR.nullIR) {
            for (int i=8; i>=0; i-=4) {
                int sr = ir.operands[i];
                if (sr != -1 && i/4==2 && ir.opcode != Opcode.STORE) { // def
                    if (SR2VR[sr] == -1) {
                        SR2VR[sr] = vr++;
                        live++; 
                        maxLive = Math.max(maxLive, live); 
                    }
                    ir.operands[i+1] = SR2VR[sr];
                    ir.operands[i+3] = LU[sr];
                    SR2VR[sr] = -1;
                    LU[sr] = -1;
                    live--; 
                } else if (sr != -1 && ir.opcode != Opcode.LOADI && ir.opcode != Opcode.OUTPUT) { // use
                    if (SR2VR[sr] == -1) {
                        SR2VR[sr] = vr++;
                        live++; 
                        maxLive = Math.max(maxLive, live); 
                    }
                    ir.operands[i+1] = SR2VR[sr];
                    ir.operands[i+3] = LU[sr];
                }
            }
            for (int i=8; i>=0; i-=4) {
                int sr = ir.operands[i];
                if (sr != -1 && ir.opcode != Opcode.LOADI && ir.opcode != Opcode.OUTPUT) {
                    LU[sr] = l;
                }
            }
            l--;
            ir = ir.prev;
        }

    }



}



