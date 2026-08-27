import java.time.LocalDate;

/**
 * Makes sense of the raw command lines the user types: it works out which
 * command was given and turns the arguments into the values the rest of the
 * program needs (a {@link Task}, a task number, or a date).
 *
 * The methods are static because parsing depends only on the input string,
 * not on any stored state; grouping them here keeps all command-format
 * knowledge in one place. An instance-based parser would work too, but adds
 * no value while there is nothing to remember between calls.
 */
public class Parser {
    private static final String TODO_FORMAT = "todo <description>";
    private static final String DEADLINE_FORMAT =
            "deadline <description> /by yyyy-mm-dd";
    private static final String EVENT_FORMAT =
            "event <description> /from yyyy-mm-dd /to yyyy-mm-dd";

    /**
     * Returns the command type named by the first word of the command, or
     * {@code null} if that word is not a recognised command.
     *
     * @param command the raw line typed by the user
     * @throws EmptyCommandException if the command is blank
     */
    public static CommandType getCommandType(String command) throws EmptyCommandException {
        String trimmedCommand = command.trim();
        if (trimmedCommand.isEmpty()) {
            throw new EmptyCommandException();
        }

        String commandWord = trimmedCommand.split("\\s+")[0];
        try {
            return CommandType.valueOf(commandWord.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Parses a task-adding command (todo, deadline, or event) into the
     * corresponding {@link Task}.
     *
     * @param command the raw line typed by the user
     * @return the task described by the command
     * @throws NomsException if the command is unknown or missing required parts
     */
    public static Task parseTask(String command) throws NomsException {
        CommandType commandType = getCommandType(command);

        if (commandType == CommandType.TODO) {
            String description = command.substring(4).trim();
            if (description.isEmpty()) {
                throw new EmptyDescriptionException("todo", TODO_FORMAT);
            }
            return new ToDo(description);
        }

        if (commandType == CommandType.DEADLINE) {
            if (command.equals("deadline")) {
                throw new EmptyDescriptionException("deadline", DEADLINE_FORMAT);
            }
            String remainder = command.substring(9);
            int byIndex = remainder.indexOf(" /by ");
            if (byIndex < 1) {
                throw new InvalidDeadlineException(DEADLINE_FORMAT);
            }
            String description = remainder.substring(0, byIndex).trim();
            String by = remainder.substring(byIndex + 5).trim();
            if (description.isEmpty()) {
                throw new EmptyDescriptionException("deadline", DEADLINE_FORMAT);
            }
            if (by.isEmpty()) {
                throw new InvalidDeadlineException(DEADLINE_FORMAT);
            }
            return new Deadline(description, DateUtil.parse(by));
        }

        if (commandType == CommandType.EVENT) {
            if (command.equals("event")) {
                throw new EmptyDescriptionException("event", EVENT_FORMAT);
            }
            String remainder = command.substring(6);
            int fromIndex = remainder.indexOf(" /from ");
            if (fromIndex < 1) {
                throw new InvalidEventException(EVENT_FORMAT);
            }
            int toIndex = remainder.indexOf(" /to ", fromIndex + 7);
            if (toIndex < 0) {
                throw new InvalidEventException(EVENT_FORMAT);
            }
            String description = remainder.substring(0, fromIndex).trim();
            String from = remainder.substring(fromIndex + 7, toIndex).trim();
            String to = remainder.substring(toIndex + 5).trim();
            if (description.isEmpty()) {
                throw new EmptyDescriptionException("event", EVENT_FORMAT);
            }
            if (from.isEmpty() || to.isEmpty()) {
                throw new InvalidEventException(EVENT_FORMAT);
            }
            return new Event(description, DateUtil.parse(from), DateUtil.parse(to));
        }

        throw new UnknownCommandException();
    }

    /**
     * Parses and validates the task number argument of a mark, unmark, or
     * delete command, returning it as a 1-based number within range.
     *
     * @param command the raw line typed by the user
     * @param action the command word, used in the error messages
     * @param taskCount the current number of tasks, used for the range check
     * @return the validated 1-based task number
     * @throws InvalidTaskNumberException if the number is missing, extra,
     *         non-numeric, or out of range
     */
    public static int parseTaskNumber(String command, String action, int taskCount)
            throws InvalidTaskNumberException {
        String[] parts = command.trim().split("\\s+");

        if (taskCount == 0) {
            throw new InvalidTaskNumberException(
                    "Noms has no tasks to " + action + " yet.\n"
                            + "Add a task first, then try again.");
        }

        if (parts.length == 1) {
            throw new InvalidTaskNumberException(
                    "Noms needs to know which task to " + action + ".\n"
                            + "Try: " + action + " <task number>");
        }

        if (parts.length > 2) {
            throw new InvalidTaskNumberException(
                    "Noms can only " + action + " one task at a time.\n"
                            + "Try: " + action + " <task number>");
        }

        String taskNumberText = parts[1];
        if (!taskNumberText.matches("-?\\d+")) {
            throw new InvalidTaskNumberException(
                    "The task number must be a whole number.\nTry: " + action + " 1");
        }

        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            if (taskNumber < 1 || taskNumber > taskCount) {
                throw new InvalidTaskNumberException(
                        "Task number " + taskNumber + " is out of range.\n"
                                + "Choose a task number from 1 to " + taskCount + ".");
            }
            return taskNumber;
        } catch (NumberFormatException e) {
            throw new InvalidTaskNumberException(
                    "That task number is too large for Noms.\n"
                            + "Choose a task number from 1 to " + taskCount + ".");
        }
    }

    /**
     * Extracts and parses the date from an {@code on <date>} command, using
     * the same rules used when adding tasks so that an invalid date surfaces
     * the same friendly error message.
     *
     * @param command the raw line typed by the user
     * @return the date named after {@code on}
     * @throws NomsException if the date is missing or unparseable
     */
    public static LocalDate parseOnDate(String command) throws NomsException {
        String[] parts = command.trim().split("\\s+", 2);
        if (parts.length < 2 || parts[1].isBlank()) {
            throw new InvalidDateException("");
        }
        return DateUtil.parse(parts[1].trim());
    }
}
