import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Parses and formats the dates Noms accepts in deadline and event commands.
 *
 * Input is the ISO format {@code yyyy-MM-dd} (e.g. {@code 2019-10-15}) so
 * that {@link LocalDate#parse(CharSequence)} can be used directly. Output
 * for display uses a friendlier pattern (e.g. {@code Oct 15 2019}).
 */
public final class DateUtil {
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy");

    private DateUtil() {} // prevents instantiation

    /**
     * Parses user input as an ISO-format date ({@code yyyy-MM-dd}).
     *
     * @param raw the date text typed by the user
     * @return the parsed date
     * @throws InvalidDateException if the text is not a valid ISO date
     */
    public static LocalDate parse(String raw) throws InvalidDateException {
        try {
            return LocalDate.parse(raw);
        } catch (DateTimeParseException e) {
            throw new InvalidDateException(raw);
        }
    }

    /**
     * Formats a date for display in task listings.
     *
     * @param date the date to format
     * @return the date rendered in the pattern {@code MMM dd yyyy}
     */
    public static String format(LocalDate date) {
        return date.format(DISPLAY_FORMAT);
    }
}
