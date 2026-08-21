import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Noms {
    private static final String TODO_FORMAT = "todo <description>";
    private static final String DEADLINE_FORMAT =
            "deadline <description> /by <date/time>";
    private static final String EVENT_FORMAT =
            "event <description> /from <date/time> /to <date/time>";

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
        List<Task> tasks = new ArrayList<>();

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
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("   " + tasks.get(taskIndex));
                } catch (NomsException e) {
                    printError(e.getMessage());
                }
            } else if (commandType == CommandType.UNMARK) {
                try {
                    int taskNumber = parseTaskNumber(command, "unmark", tasks.size());
                    int taskIndex = taskNumber - 1;
                    tasks.get(taskIndex).markAsNotDone();
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println("   " + tasks.get(taskIndex));
                } catch (NomsException e) {
                    printError(e.getMessage());
                }
            } else if (commandType == CommandType.DELETE) {
                try {
                    int taskNumber = parseTaskNumber(command, "delete", tasks.size());
                    Task deletedTask = tasks.remove(taskNumber - 1);
                    System.out.println(" Noted. Noms has taken this task off the menu:");
                    System.out.println("   " + deletedTask);
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println("____________________________________________________________");
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
            return new Deadline(description, by);
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
            return new Event(description, from, to);
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
        String[] parts = command.split("\\s+");
        if (parts.length != 2) {
            throw new InvalidTaskNumberException(
                    "Noms needs a task number to " + action + ".\nTry: " + action + " 1");
        }

        try {
            int taskNumber = Integer.parseInt(parts[1]);
            if (taskNumber < 1 || taskNumber > taskCount) {
                throw new InvalidTaskNumberException(
                        "That task number is off the menu.\n"
                                + "Choose a number from your task list.");
            }
            return taskNumber;
        } catch (NumberFormatException e) {
            throw new InvalidTaskNumberException(
                    "Noms only understands task numbers here.\nTry: " + action + " 1");
        }
    }

    private static void printError(String message) {
        System.out.println(" OOPS! " + message);
        System.out.println("____________________________________________________________");
    }
}
