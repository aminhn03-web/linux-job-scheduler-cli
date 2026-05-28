/**
 * Provides consistent terminal output formatting for the application.
 *
 * AppLogger centralizes informational, success, warning, and error messages
 * so the CLI output stays clean and consistent across all classes.
 */
public class AppLogger {

    /**
     * Prints a general informational message.
     *
     * @param message message to display
     */
    public static void info(String message) {
        System.out.println("[INFO] " + message);
    }

    /**
     * Prints a success message.
     *
     * @param message message to display
     */
    public static void success(String message) {
        System.out.println("[SUCCESS] " + message);
    }

    /**
     * Prints a warning message.
     *
     * @param message message to display
     */
    public static void warning(String message) {
        System.out.println("[WARNING] " + message);
    }

    /**
     * Prints an error message.
     *
     * @param message message to display
     */
    public static void error(String message) {
        System.out.println("[ERROR] " + message);
    }

    /**
     * Prints a blank line for spacing in terminal output.
     */
    public static void blankLine() {
        System.out.println();
    }
}