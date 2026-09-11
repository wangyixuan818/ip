package noms.ui;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import noms.task.Task;
import noms.util.DateUtil;

/**
 * Handles all interaction with the user: reading typed commands from
 * standard input and printing Noms' responses to standard output.
 *
 * Centralizing console I/O here keeps the rest of the program free of
 * {@code System.out.println} calls and gives every message and the
 * horizontal divider a single, consistent home.
 */
public class Ui {
    private static final String DIVIDER =
            "____________________________________________________________";

    private final Scanner scanner;

    /**
     * Creates a Ui that reads user input from standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Returns whether there is another line of input to read.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads and returns the next line the user typed.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Releases the input resource once no more commands will be read.
     */
    public void close() {
        scanner.close();
    }

    /**
     * Prints the startup banner and greeting shown when Noms launches.
     */
    public void showWelcome() {
        String banner = DIVIDER + "\n"
                + " _   _  ___  __  __  ____\n"
                + "| \\ | |/ _ \\|  \\/  |/ ___|\n"
                + "|  \\| | | | | |\\/| | \\___ \\\n"
                + "| |\\  | |_| | |  | |  ___) |\n"
                + "|_| \\_|\\___/|_|  |_| |____/\n"
                + DIVIDER;
        System.out.println(banner);
        System.out.println("Hello! I'm Noms.");
        System.out.println("NomNom, have you eaten? What can I do for you?");
        System.out.println(DIVIDER);
    }

    /**
     * Prints the farewell message shown when the user says bye.
     */
    public void showGoodbye() {
        System.out.println("Bye~ Hope to see you again soon!");
        System.out.println(DIVIDER);
    }

    /**
     * Prints an error message in the standard Noms error style.
     *
     * @param message the explanation of what went wrong
     */
    public void showError(String message) {
        System.out.println(" OOPS! " + message);
        System.out.println(DIVIDER);
    }

    /**
     * Prints the whole task list, numbered from 1, followed by a divider.
     * An empty list prints just the divider.
     *
     * @param tasks the tasks to display
     */
    public void showTaskList(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
        System.out.println(DIVIDER);
    }

    /**
     * Confirms that a task has just been added and reports the new count.
     *
     * @param task the task that was added
     * @param taskCount the number of tasks now in the list
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
        System.out.println(DIVIDER);
    }

    /**
     * Confirms that a task has just been deleted and reports the new count.
     *
     * @param task the task that was removed
     * @param taskCount the number of tasks remaining in the list
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println(" Noted. Noms has taken this task off the menu:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
        System.out.println(DIVIDER);
    }

    /**
     * Confirms that a task has just been marked as done.
     *
     * @param task the task that was marked done
     * @param changed whether the task changed from incomplete to complete
     */
    public void showTaskMarked(Task task, boolean changed) {
        System.out.println(changed
                ? " Nice! I've marked this task as done:"
                : " Nom nom! This task is already marked as done:");
        System.out.println("   " + task);
        System.out.println(DIVIDER);
    }

    /**
     * Confirms that a task has just been marked as not done.
     *
     * @param task the task that was marked not done
     * @param changed whether the task changed from complete to incomplete
     */
    public void showTaskUnmarked(Task task, boolean changed) {
        System.out.println(changed
                ? " OK, I've marked this task as not done yet:"
                : " Nom nom! This task is already unmarked:");
        System.out.println("   " + task);
        System.out.println(DIVIDER);
    }

    /**
     * Confirms that a dated task has been snoozed.
     *
     * @param task the task with its updated date or dates
     */
    public void showTaskSnoozed(Task task) {
        System.out.println(" Nom nom! Noms has snoozed this task:");
        System.out.println("   " + task);
        System.out.println(DIVIDER);
    }

    /**
     * Prints the deadlines and events that fall on the given date. If no
     * task matches, prints a friendly "nothing that day" note instead.
     *
     * @param date the date being queried
     * @param matches the tasks that occur on that date, in list order
     */
    public void showTasksOn(LocalDate date, List<Task> matches) {
        System.out.println(" Tasks on " + DateUtil.format(date) + ":");
        for (int i = 0; i < matches.size(); i++) {
            System.out.println("   " + (i + 1) + ". " + matches.get(i));
        }
        if (matches.isEmpty()) {
            System.out.println(" (nothing on the menu that day)");
        }
        System.out.println(DIVIDER);
    }

    /**
     * Prints the tasks whose description matched a {@code find} keyword. If no
     * task matches, prints a friendly "nothing found" note instead.
     *
     * @param keyword the keyword that was searched for
     * @param matches the tasks whose descriptions contain the keyword
     */
    public void showMatchingTasks(String keyword, List<Task> matches) {
        if (matches.isEmpty()) {
            System.out.println(" Hmm, Noms sniffed around but found no tasks matching \""
                    + keyword + "\".");
            System.out.println(" Nothing on the menu to nibble on!");
            System.out.println(DIVIDER);
            return;
        }
        System.out.println(" Yum! Noms dug up these matching tasks for \"" + keyword + "\":");
        for (int i = 0; i < matches.size(); i++) {
            System.out.println("   " + (i + 1) + ". " + matches.get(i));
        }
        System.out.println(DIVIDER);
    }
}
