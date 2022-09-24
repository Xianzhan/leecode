package xianzhan.leecode.gc.marksweep;

public class Obj {

    public ObjType type;
    public int marked;
    public Obj next;

    public int value;
    public Obj head;
    public Obj tail;

    @Override
    public String toString() {
        return switch (type) {
            case INT -> Integer.toString(value);
            case PAIR -> {
                if (head != this && tail != this) {
                    yield "(" + head + ',' + tail + ')';
                }
                var sb = new StringBuilder("(");
                if (head == this) {
                    sb.append(value);
                    sb.append(',');
                } else {
                    sb.append(head);
                }
                if (tail == this) {
                    sb.append(value);
                    sb.append(',');
                } else {
                    sb.append(tail);
                }
                yield sb.toString();
            }
        };
    }
}
