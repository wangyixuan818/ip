import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Noms {
    private static final String TODO_FORMAT = "todo <description>";
    private static final String DEADLINE_FORMAT =
            "deadline <description> /by yyyy-mm-dd";
    private static final String EVENT_FORMAT =
            "event <description> /from yyyy-mm-dd /to yyyy-mm-dd";
    private static final String SAVE_DIRECTORY = "data";
    private static final String SAVE_FILE_NAME = "noms.txt";

    public static void main(String[] args) {
        String banner = "____________________________________________________________\n"
                + " _   _  ___  __  __  ____\n"
                + "| \\ | |/ _ \\|  \\/  |/ ___|\n"
                + "|  \\| | | | | |\\/| | \\___ \\\n"
                + "| |\\  | |_| | |  | |  ___) |\n"
                + "|_| \\_|\\___/|_|  |_| |____/\n"
                + "____________________________________________________________";
        System.out.println(banner);
        System.out.println("Hello! I'm Noms.");
        System.out.println("NomNom, have you eaten? What can I do for you?");
        System.out.println("____________________________________________________________");

        Scanner scanner = new Scanner(System.in);
        Storage storage = new Storage(SAVE_DIRECTORY, SAVE_FILE_NAME);
        List<Task> tasks;
        try {
            tasks = storage.load();
        } catch (IOException e) {
            tasks = new ArrayList<>();
            printError("Noms couldn't load the saved menu, starting with an empty plate: " + e.getMessage());
        }

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();

            CommandType commandType;
            try {
                commandType = getCommandType(command);
            } catch (NomsException e) {
                printError(e.getMessage());
                continue;
            }

            if (commandType == CommandType.BYE) {
                System.out.println("Bye~ Hope to see you again soon!");
                System.out.println("____________________________________________________________");
                break;
            } else if (commandType == CommandType.LIST) {
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println(" " + (i + 1) + "." + tasks.get(i));
                }
                System.out.println("____________________________________________________________");
            } else if (commandType == CommandType.MARK) {
                try {
                    int taskNumber = parseTaskNumber(command, "mark", tasks.size());
                    int taskIndex = taskNumber - 1;
                    tasks.get(taskIndex).markAsDone();
                    saveTasks(storage, tasks);
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("   " + tasks.get(taskIndex));
                    System.out.println("____________________________________________________________");
                } catch (NomsException e) {
                    printError(e.getMessage());
                }
            } else if (commandType == CommandType.UNMARK) {
                try {
                    int taskNumber = parseTaskNumber(command, "unmark", tasks.size());
                    int taskIndex = taskNumber - 1;
                    tasks.get(taskIndex).markAsNotDone();
                    saveTasks(storage, tasks);
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println("   " + tasks.get(taskIndex));
                    System.out.println("____________________________________________________________");
                } catch (NomsException e) {
                    printError(e.getMessage());
                }
            } else if (commandType == CommandType.DELETE) {
                try {
                    int taskNumber = parseTaskNumber(command, "delete", tasks.size());
                    Task deletedTask = tasks.remove(taskNumber - 1);
                    saveTasks(storage, tasks);
                    System.out.println(" Noted. Noms has taken this task off the menu:");
                    System.out.println("   " + deletedTask);
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println("____________________________________________________________");
                } catch (NomsException e) {
                    printError(e.getMessage());
                }
            } else if (commandType == CommandType.ON) {
                try {
                    printTasksOn(command, tasks);
                } catch (NomsException e) {
                    printError(e.getMessage());
                }
            } else {
                Task task;
                try {
                    task = parseTask(command);
                } catch (NomsException e) {
                    printError(e.getMessage());
                    continue;
                }
                tasks.add(task);
                saveTasks(storage, tasks);

                System.out.println(" Got it. I've added this task:");
                System.out.println("   " + task);
                System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                System.out.println("____________________________________________________________");
            }
        }

        scanner.close();
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
     * Prints every deadline and event that falls on the given date. The
     * date is extracted from the {@code on <date>} command and parsed
     * with the same rules used when adding tasks, so an invalid date
     * surfaces the same friendly error message.
     */
    private static void printTasksOn(String command, List<Task> tasks) throws NomsException {
        String[] parts = command.trim().split("\\s+", 2);
        if (parts.length < 2 || parts[1].isBlank()) {
            throw new InvalidDateException("");
        }
        LocalDate date = DateUtil.parse(parts[1].trim());

        System.out.println(" Tasks on " + DateUtil.format(date) + ":");
        int matches = 0;
        for (Task task : tasks) {
            boolean occurs = task instanceof Deadline d && d.occursOn(date)
                    || task instanceof Event e && e.occursOn(date);
            if (occurs) {
                matches++;
                System.out.println("   " + matches + ". " + task);
            }
        }
        if (matches == 0) {
            System.out.println(" (nothing on the menu that day)");
        }
        System.out.println("____________________________________________________________");
    }

    /**
     * Saves the current task list to disk, reporting a Noms-style error
     * if the save fails instead of crashing the program.
     */
    private static void saveTasks(Storage storage, List<Task> tasks) {
        try {
            storage.save(tasks);
        } catch (IOException e) {
            printError("Noms couldn't save the menu to disk: " + e.getMessage());
        }
    }

    private static void printError(String message) {
        System.out.println(" OOPS! " + message);
        System.out.println("____________________________________________________________");
    }
}
