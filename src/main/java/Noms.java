import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Noms {
    private static final String TODO_FORMAT = "todo <description>";
    private static final String DEADLINE_FORMAT =
            "deadline <description> /by yyyy-mm-dd";
    private static final String EVENT_FORMAT =
            "event <description> /from yyyy-mm-dd /to yyyy-mm-dd";
    private static final String SAVE_DIRECTORY = "data";
    private static final String SAVE_FILE_NAME = "noms.txt";

    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();

        Storage storage = new Storage(SAVE_DIRECTORY, SAVE_FILE_NAME);
        List<Task> tasks;
        try {
            tasks = storage.load();
        } catch (IOException e) {
            tasks = new ArrayList<>();
            ui.showError("Noms couldn't load the saved menu, starting with an empty plate: " + e.getMessage());
        }

        while (ui.hasNextCommand()) {
            String command = ui.readCommand();

            CommandType commandType;
            try {
                commandType = getCommandType(command);
            } catch (NomsException e) {
                ui.showError(e.getMessage());
                continue;
            }

            if (commandType == CommandType.BYE) {
                ui.showGoodbye();
                break;
            } else if (commandType == CommandType.LIST) {
                ui.showTaskList(tasks);
            } else if (commandType == CommandType.MARK) {
                try {
                    int taskNumber = parseTaskNumber(command, "mark", tasks.size());
                    Task task = tasks.get(taskNumber - 1);
                    task.markAsDone();
                    saveTasks(storage, tasks, ui);
                    ui.showTaskMarked(task);
                } catch (NomsException e) {
                    ui.showError(e.getMessage());
                }
            } else if (commandType == CommandType.UNMARK) {
                try {
                    int taskNumber = parseTaskNumber(command, "unmark", tasks.size());
                    Task task = tasks.get(taskNumber - 1);
                    task.markAsNotDone();
                    saveTasks(storage, tasks, ui);
                    ui.showTaskUnmarked(task);
                } catch (NomsException e) {
                    ui.showError(e.getMessage());
                }
            } else if (commandType == CommandType.DELETE) {
                try {
                    int taskNumber = parseTaskNumber(command, "delete", tasks.size());
                    Task deletedTask = tasks.remove(taskNumber - 1);
                    saveTasks(storage, tasks, ui);
                    ui.showTaskDeleted(deletedTask, tasks.size());
                } catch (NomsException e) {
                    ui.showError(e.getMessage());
                }
            } else if (commandType == CommandType.ON) {
                try {
                    LocalDate date = parseOnDate(command);
                    ui.showTasksOn(date, tasksOn(tasks, date));
                } catch (NomsException e) {
                    ui.showError(e.getMessage());
                }
            } else {
                Task task;
                try {
                    task = parseTask(command);
                } catch (NomsException e) {
                    ui.showError(e.getMessage());
                    continue;
                }
                tasks.add(task);
                saveTasks(storage, tasks, ui);
                ui.showTaskAdded(task, tasks.size());
            }
        }

        ui.close();
    }

    private static Task parseTask(String command) throws NomsException {
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

    private static CommandType getCommandType(String command) throws EmptyCommandException {
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

    private static int parseTaskNumber(String command, String action, int taskCount)
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
     * Extracts and parses the date from an {@code on <date>} command,
     * using the same rules used when adding tasks so that an invalid date
     * surfaces the same friendly error message.
     */
    private static LocalDate parseOnDate(String command) throws NomsException {
        String[] parts = command.trim().split("\\s+", 2);
        if (parts.length < 2 || parts[1].isBlank()) {
            throw new InvalidDateException("");
        }
        return DateUtil.parse(parts[1].trim());
    }

    /**
     * Returns the deadlines and events that occur on the given date, in
     * their original list order.
     */
    private static List<Task> tasksOn(List<Task> tasks, LocalDate date) {
        List<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            boolean occurs = task instanceof Deadline d && d.occursOn(date)
                    || task instanceof Event e && e.occursOn(date);
            if (occurs) {
                matches.add(task);
            }
        }
        return matches;
    }

    /**
     * Saves the current task list to disk, reporting a Noms-style error
     * if the save fails instead of crashing the program.
     */
    private static void saveTasks(Storage storage, List<Task> tasks, Ui ui) {
        try {
            storage.save(tasks);
        } catch (IOException e) {
            ui.showError("Noms couldn't save the menu to disk: " + e.getMessage());
        }
    }
}
