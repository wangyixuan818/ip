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
        Task[] tasks = new Task[100];
        int taskCount = 0;

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();

            if (command.equals("bye")) {
                System.out.println("Bye~ Hope to see you again soon!");
                System.out.println("____________________________________________________________");
                break;
            } else if (command.equals("list")) {
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + "." + tasks[i]);
                }
                System.out.println("____________________________________________________________");
            } else if (command.equals("mark") || command.startsWith("mark ")) {
                String[] parts = command.split("\\s+");

                if (parts.length != 2) {
                    System.out.println(" Please provide a task number to mark as done.");
                    System.out.println("____________________________________________________________");
                    continue;
                }

                try {
                    int taskNumber = Integer.parseInt(parts[1]);
                    if (taskNumber < 1 || taskNumber > taskCount) {
                        System.out.println(" Please provide a valid task number.");
                    } else {
                        int taskIndex = taskNumber - 1;
                        tasks[taskIndex].markAsDone();
                        System.out.println(" Nice! I've marked this task as done:");
                        System.out.println("   " + tasks[taskIndex]);
                    }
                } catch (NumberFormatException e) {
                    System.out.println(" Please provide a valid task number.");
                }
                System.out.println("____________________________________________________________");
            } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                String[] parts = command.split("\\s+");

                if (parts.length != 2) {
                    System.out.println(" Please provide a task number to mark as not done.");
                    System.out.println("____________________________________________________________");
                    continue;
                }

                try {
                    int taskNumber = Integer.parseInt(parts[1]);
                    if (taskNumber < 1 || taskNumber > taskCount) {
                        System.out.println(" Please provide a valid task number.");
                    } else {
                        int taskIndex = taskNumber - 1;
                        tasks[taskIndex].markAsNotDone();
                        System.out.println(" OK, I've marked this task as not done yet:");
                        System.out.println("   " + tasks[taskIndex]);
                    }
                } catch (NumberFormatException e) {
                    System.out.println(" Please provide a valid task number.");
                }
                System.out.println("____________________________________________________________");
            } else if (taskCount >= tasks.length) {
                System.out.println(" The task list is full.");
                System.out.println("____________________________________________________________");
            } else {
                Task task;
                try {
                    task = parseTask(command);
                } catch (NomsException e) {
                    printError(e.getMessage());
                    continue;
                }
                if (task == null) {
                    System.out.println(" I couldn't understand that task command.");
                    System.out.println(" Use todo <description>, deadline <description> /by <date/time>, or"
                            + " event <description> /from <date/time> /to <date/time>.");
                    System.out.println("____________________________________________________________");
                    continue;
                }

                tasks[taskCount] = task;
                taskCount++;

                System.out.println(" Got it. I've added this task:");
                System.out.println("   " + task);
                System.out.println(" Now you have " + taskCount + " tasks in the list.");
                System.out.println("____________________________________________________________");
            }
        }

        scanner.close();
    }

    private static Task parseTask(String command) throws NomsException {
        if (command.equals("todo") || command.startsWith("todo ")) {
            String description = command.substring(4).trim();
            if (description.isEmpty()) {
                throw new EmptyDescriptionException("todo", TODO_FORMAT);
            }
            return new ToDo(description);
        }

        if (command.equals("deadline") || command.startsWith("deadline ")) {
            if (command.equals("deadline")) {
                throw new EmptyDescriptionException("deadline", DEADLINE_FORMAT);
            }
            String remainder = command.substring(9);
            int byIndex = remainder.indexOf(" /by ");
            if (byIndex < 1) {
                return null;
            }
            String description = remainder.substring(0, byIndex).trim();
            String by = remainder.substring(byIndex + 5).trim();
            if (description.isEmpty()) {
                throw new EmptyDescriptionException("deadline", DEADLINE_FORMAT);
            }
            return by.isEmpty() ? null : new Deadline(description, by);
        }

        if (command.equals("event") || command.startsWith("event ")) {
            if (command.equals("event")) {
                throw new EmptyDescriptionException("event", EVENT_FORMAT);
            }
            String remainder = command.substring(6);
            int fromIndex = remainder.indexOf(" /from ");
            if (fromIndex < 1) {
                return null;
            }
            int toIndex = remainder.indexOf(" /to ", fromIndex + 7);
            if (toIndex < 0) {
                return null;
            }
            String description = remainder.substring(0, fromIndex).trim();
            String from = remainder.substring(fromIndex + 7, toIndex).trim();
            String to = remainder.substring(toIndex + 5).trim();
            if (description.isEmpty()) {
                throw new EmptyDescriptionException("event", EVENT_FORMAT);
            }
            return from.isEmpty() || to.isEmpty() ? null : new Event(description, from, to);
        }

        throw new UnknownCommandException();
    }

    private static void printError(String message) {
        System.out.println(" OOPS! " + message);
        System.out.println("____________________________________________________________");
    }
}
