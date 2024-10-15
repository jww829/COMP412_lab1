package src;

import java.util.Stack;

public class Allocator {

    private int numPR;
    private int numVR;
    private int maxLive;
    private int spillLoc = 32768;
    private int reserved;
    private int[] VRToPR;
    private int[] PRToVR;
    private int[] VRToSpillLoc;
    private int[] PRToNU;
    private int[] PRUsed;
    private IR ir;
    private Stack<Integer> freePR;


    public Allocator(int numPR, int numVR, int maxLive, IRList irl) {
        this.numPR = numPR;
        this.numVR = numVR;
        this.maxLive = maxLive;
        this.reserved = numPR-1;
        this.ir = irl.head;
        this.VRToPR = new int[numVR];
        this.VRToSpillLoc = new int[numVR];
        this.PRToVR = new int[numPR];
        this.PRUsed = new int[numPR];
        this.PRToNU = new int[numPR];

        for (int i=0; i<numVR; i++) {
            VRToPR[i] = -1;
            VRToSpillLoc[i] = -1;
        }
        for (int i=0; i<numPR; i++) {
            PRToVR[i] = -1;
            PRToNU[i] = -1;
        }

        freePR = new Stack<>();
        for (int i = numPR-2; i>=0; i--) {
            freePR.push(i);
        }
    }

    public void allocate() {
        while (ir != IR.nullIR) {
            int j = 0; // For debug

            // Use
            for (int i=0; i<11; i+=4) {
                if (ir.operands[i] < 0 || ir.opcode == Opcode.LOADI || ir.opcode == Opcode.OUTPUT) {
                    continue;
                }
                if (i >= 8 && ir.opcode != Opcode.STORE) {
                    continue;
                }
                int vr = ir.operands[i+1];
                int pr = VRToPR[ir.operands[i+1]];
                if (pr == -1) { 
                    pr = getPR(); 
                    if (VRToSpillLoc[vr] != -1) {
                        restore(vr, pr);
                    }               
                    VRToPR[vr] = pr; 
                    PRToVR[pr] = vr;
                } 
                ir.operands[i+2] = pr;
                PRToNU[pr] = ir.operands[i+3];
                PRUsed[pr] = 1;
            }

            // Find last use
            for (int i=0; i<11; i+=4) {
                if (ir.operands[i] < 0 || ir.opcode == Opcode.LOADI || ir.opcode == Opcode.OUTPUT) {
                    continue;
                }
                if (i >= 8 && ir.opcode != Opcode.STORE) {
                    continue;
                }
                if (ir.operands[i+3] < 0) { // Free pr
                    int vr = ir.operands[i+1];
                    int pr = ir.operands[i+2];
                    PRToNU[pr] = -1;
                    PRToVR[pr] = -1;
                    VRToPR[vr] = -1;
                    PRUsed[pr] = 0;
                    freePR.push(pr);
                }
            }

            // Reset PRUsed
            for (int i=0; i<numPR; i++) {
                PRUsed[i] = 0;
            }

            // Def
            if (ir.operands[9] >= 0 && ir.opcode != Opcode.STORE) {
                int vr = ir.operands[9];
                int pr = getPR();
                VRToPR[vr] = pr;
                PRToVR[pr] = vr;
                PRToNU[pr] = ir.operands[11];
                ir.operands[10] = pr;
            }

            // System.out.println(ir);
            ir = ir.next;
        }
    }

    private int getPR() {
        if (!freePR.isEmpty()) {
            return freePR.pop();
        }

        // Find farthest next use
        int nu = -1, pr = -1;
        for (int i=0; i<PRToNU.length; i++) {
            if (PRToNU[i] >= nu && PRUsed[i] == 0) { // Ignore pr that has been used
                nu = PRToNU[i];
                pr = i;
            }
        }
        spill(pr);
        return pr;
    }

    private void spill(int pr) {
        int vr = PRToVR[pr];

        IR loadI = new IR(Opcode.LOADI);
        loadI.operands[0] = this.spillLoc;
        loadI.operands[10] = this.reserved;
        IRList.insertBefore(ir, loadI);

        IR store = new IR(Opcode.STORE);
        store.operands[2] = pr;
        store.operands[10] = this.reserved;
        IRList.insertBefore(ir, store);

        VRToSpillLoc[vr] = this.spillLoc;
        VRToPR[vr] = -1;
        spillLoc += 4;

    }

    private void restore(int vr, int pr) {
        IR loadI = new IR(Opcode.LOADI);
        loadI.operands[0] = VRToSpillLoc[vr];
        loadI.operands[10] = reserved;
        IRList.insertBefore(ir, loadI);

        IR load = new IR(Opcode.LOAD);
        load.operands[2] = this.reserved;
        load.operands[10] = pr;
        IRList.insertBefore(ir, load);

        VRToSpillLoc[vr] = -1;
    }


}
