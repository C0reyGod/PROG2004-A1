/**
 * Shared console formatting helpers, so every class prints fields and
 * separator lines in exactly the same layout.
 *
 * @author kwu
 * @version 3.0
 */
public final class Display {

    public static final String LINE =
            "--------------------------------------------------------------------------------";
    public static final String DOUBLE_LINE =
            "================================================================================";

    private Display() {
        // Utility class: no objects needed
    }

    /**
     * Formats one labelled field on its own line.
     *
     * @param label the field label
     * @param value the field value
     * @return the formatted line, ending with a line break
     */
    public static String field(String label, Object value) {
        return String.format("  %-22s: %s%n", label, value);
    }

    /**
     * Prints a title between two double separator lines.
     *
     * @param title the heading text
     */
    public static void header(String title) {
        System.out.println(DOUBLE_LINE);
        System.out.println("  " + title);
        System.out.println(DOUBLE_LINE);
    }
}
