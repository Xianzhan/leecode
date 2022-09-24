package xianzhan.leecode.gc.marksweep;

public class VM {

    public static final int STACK_MAX        = 256;
    public static final int INIT_OBJ_NUM_MAX = 8;

    public Obj[] stack;
    public int   stackSize;

    public Obj firstObj;
    public int numObjs;
    public int maxObjs;

    public static void check(boolean condition, String message) {
        if (condition) {
            System.out.println(message);
            System.exit(1);
        }
    }

    public static VM newVM() {
        VM vm = new VM();
        vm.stack = new Obj[STACK_MAX];
        vm.maxObjs = INIT_OBJ_NUM_MAX;
        return vm;
    }

    private VM() {
    }

    private void push(Obj value) {
        check(stackSize >= STACK_MAX, "Stack overflow!");
        stack[stackSize++] = value;
    }

    public Obj pop() {
        check(stackSize < 0, "Stack underflow!");
        return stack[--stackSize];
    }

    private void mark(Obj obj) {
        if (obj.marked == 1) {
            return;
        }

        obj.marked = 1;

        if (obj.type == ObjType.PAIR) {
            mark(obj.head);
            mark(obj.tail);
        }
    }

    public void markAll() {
        for (int i = 0; i < stackSize; i++) {
            mark(stack[i]);
        }
    }

    public void sweep() {
        Obj obj = firstObj;
        while (obj != null) {
            if (obj.marked != 1) {
                Obj unreached = obj;

                obj = unreached.next;
                free(unreached);

                numObjs--;
            } else {
                obj.marked = 0;
                obj = obj.next;
            }
        }
    }

    public void gc() {
        int numObjects = numObjs;

        markAll();
        sweep();

        maxObjs = numObjs == 0 ? INIT_OBJ_NUM_MAX : numObjs * 2;
        System.out.printf("Collected %d objs, %d remaining. %n", numObjects - numObjs, numObjs);
    }

    private Obj newObj(ObjType type) {
        if (numObjs == maxObjs) {
            gc();
        }

        Obj obj = new Obj();
        obj.type = type;
        obj.next = firstObj;
        firstObj = obj;
        numObjs++;

        return obj;
    }

    public void pushInt(int value) {
        Obj obj = newObj(ObjType.INT);
        obj.value = value;

        push(obj);
    }

    public Obj pushPair() {
        Obj obj = newObj(ObjType.PAIR);
        obj.tail = pop();
        obj.head = pop();

        push(obj);
        return obj;
    }

    public void freeVM() {
        stackSize = 0;
        gc();
    }

    private void free(Obj obj) {
        System.out.println("释放 " + obj);
    }
}
