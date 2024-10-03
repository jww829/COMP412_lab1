package src;

// Util class for IR list
public class IRList {
    
    public IRList() {}

    // Find the tail of this IR linked list
    public static IR tail(IR ir) {
        while (ir.next != IR.nullIR) {
            ir = ir.next;
        }
        return ir;
    }

    public static void printIRList(IR ir) {
        IR current = ir;
        while (current != IR.nullIR) {
            System.out.println(current);
            current = current.next;
        }
    }

    public static void printILOC(IR ir) {
        IR current = ir;
        while (current != IR.nullIR) {
            System.out.println(current.ILOC());
            current = current.next;
        }
    }

}
