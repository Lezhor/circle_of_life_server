package de.htw_berlin.logging;

import de.htw_berlin.application.App;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to logging what happens to PrintStreams (e.g. System.out)<br>
 * Design is the same as in Androids Logcat-System :)
 */
public final class Log {

    public static final int TAG_LENGTH = 35;
    public static final int MESSAGE_LENGTH = 70;

    private static final List<PrintStream> printers = new ArrayList<>() {
        @Override
        public boolean add(PrintStream printStream) {
            if (this.contains(printStream)) {
                return false;
            }
            return super.add(printStream);
        }
    };

    /**
     * Adds or removes System.out from printers-list based on status
     * @param status whether to add or remove System.out to/from printers
     */
    public static void logToConsole(boolean status) {
        if (status) {
            printers.add(System.out);
        } else {
            printers.remove(System.out);
        }
    }

    private static void print(String tag, String message, Throwable e, char messageType, String messageColor) {
        String printedMessage = getTimestamp() + " "
                + posAtFront(tag + ":", TAG_LENGTH)
                + messageColor
                + posAtFront(messageType + ": ", 3)
                + posAtFront(message, MESSAGE_LENGTH) + " "
                + (e == null ? "" : ConsoleColors.RED + "Exception: " + e.getMessage()) + ConsoleColors.RESET;
        printers.forEach(p -> p.println(printedMessage));
    }

    /**
     * Used for Debug
     * @param tag TAG
     * @param message message
     */
    public static void d(String tag, String message) {
        print(tag, message, null, 'D', ConsoleColors.BLUE);
    }

    /**
     * Used for Info (Highlighting debug)
     * @param tag TAG
     * @param message message
     */
    public static void i(String tag, String message) {
        print(tag, message, null, 'I', ConsoleColors.YELLOW);
    }

    /**
     * Used for Warnings
     * @param tag TAG
     * @param message message
     */
    public static void w(String tag, String message) {
        w(tag, message, null);
    }

    /**
     * Used for Warnings
     * @param tag TAG
     * @param message message
     * @param e throwable (used to print the error message)
     */
    public static void w(String tag, String message, Throwable e) {
        print(tag, message, e, 'W', ConsoleColors.RED);
    }

    /**
     * Returns string representing the timestamp
     * @return timestamp
     */
    static String getTimestamp() {
        LocalDateTime timestamp = LocalDateTime.now(App.SERVER_TIMEZONE);
        return "[" + timestamp.format(DateTimeFormatter.ofPattern("hh:mm:ss:nnnn")).substring(0, 13) + "]";
    }


    /**
     * Appends space to the left anad right of the given string until the length is the given length
     * @param str string
     * @param length length of output string
     * @return input string but with spaces left and right so old string is in the middle
     */
    private static String posInMiddle(String str, int length) {
        int l = Math.floorDiv(length - str.length(), 2);
        int r = Math.ceilDiv(length - str.length(), 2);
        return " ".repeat(l) + str + " ".repeat(r);
    }

    /**
     * Appends one space at the front and enough spaces in the back so that the total length is as given
     * @param str string
     * @param length length of output string
     * @return passed string but with given length
     */
    private static String posAtFront(String str, int length) {
        StringBuilder ret = new StringBuilder()
                .append(' ')
                .append(str);
        while (ret.length() < length) {
            ret.append(' ');
        }
        return ret.toString();
    }

}
