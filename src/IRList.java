package src;

public class IRList {

    public IR head;
    public IR tail;
    public int size;
    
    public IRList(IR head) {
        this.head = head;
        IR ir = this.head;
        int size = 1;
        while (ir.next != IR.nullIR) {
            ir = ir.next;
            size++;
        }
        this.tail = ir;
        this.size = size;
    }

    public void printIRList() {
        IR current = this.head;
        int line = 1;
        while (current != IR.nullIR) {
            System.out.println("Line "+line+" : "+current);
            current = current.next;
            line++;
        }
    }

    public void printILOC(String r, boolean showLine) {
        IR current = this.head;
        int line = 1;
        while (current != IR.nullIR) {
            if (showLine) {System.out.printf("Line %d: ", line);}
            System.out.println(current.ILOC(r));
            current = current.next;
            line++;
        }
    }

    public static void insertAfter(IR cur, IR afterCur) {
        afterCur.next = cur.next;
        afterCur.prev = cur;
        cur.next.prev = afterCur;
        cur.next = afterCur;
    }

    public static void insertBefore(IR cur, IR beforeCur) {
        beforeCur.next = cur;
        beforeCur.prev = cur.prev;
        cur.prev.next = beforeCur;
        cur.prev = beforeCur;
    }

    // Find the tail of this IR linked list
    // public static IR tail(IR ir) {
    //     while (ir.next != IR.nullIR) {
    //         ir = ir.next;
    //     }
    //     return ir;
    // }

}
