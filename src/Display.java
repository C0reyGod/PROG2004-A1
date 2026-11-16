/**
 * Utility class providing shared console formatting helpers for aligned
 * key-value fields, section headings and string array display.
 */
public final class Display {

    public static final String LINE =
            "--------------------------------------------------------------------------------";
    public static final String DOUBLE_LINE =
            "================================================================================";

    // Private constructor prevents instantiation of this utility class
    private Display() {
    }

    // Formats one labelled field on its own line with consistent padding
    public static String field(String label, Object value) {
        return String.format("  %-22s: %s%n", label, value);
    }

    /*
     * Prints a major section heading framed above and below by double
     * horizontal separator lines.
     */
    public static void header(String title) {
        System.out.println(DOUBLE_LINE);
        System.out.println("  " + title);
        System.out.println(DOUBLE_LINE);
    }

    // Prints a subsection heading framed by single separator lines
    public static void subHeader(String title) {
        System.out.println(LINE);
        System.out.println("  " + title);
        System.out.println(LINE);
    }

    /*
     * Formats a String array into "[item1, item2]" text using a basic loop
     * so working days print uniformly without extra utility imports.
     */
    public static String formatStringArray(String[] items) {
        if (items == null || items.length == 0) {
            return "[]";
        }
        String result = "[";
        for (int i = 0; i < items.length; i++) {
            if (i > 0) {
                result = result + ", ";
            }
            result = result + items[i];
        }
        return result + "]";
    }
}
