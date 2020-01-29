package xianzhan.pascal.backend.compiler;

/**
 * Jasmin instruction label.
 *
 * @author xianzhan
 * @since 2019-07-21
 */
public class Label {

    /**
     * index for generating label strings
     */
    private static int index = 0;

    /**
     * the label string
     */
    private String label;

    private Label() {
        this.label = "L" + String.format("%03d", ++index);
    }

    public static Label newLabel() {
        return new Label();
    }

    @Override
    public String toString() {
        return label;
    }
}
