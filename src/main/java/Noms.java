import java.io.IOException;
import java.time.LocalDate;

/**
 * Entry point and top-level coordinator for the Noms task manager.
 *
 * Noms itself no longer contains any UI, parsing, or storage logic: it
 * simply wires together a {@link Ui}, a {@link Storage}, and a
 * {@link TaskList}, and drives the read-evaluate-respond loop, delegating
 * each concern to the class that owns it.
 */
public class Noms {
    private static final String SAVE_DIRECTORY = "data";
    private static final String SAVE_FILE_NAME = "noms.txt";

    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    /**
     * Creates a Noms instance backed by the save file in the given
     * directory, loading any previously saved tasks. If the file cannot be
     * read, Noms starts with an empty list and reports the problem rather
     * than crashing.
     *
     * @param directory folder containing the save file, relative to the project root
     * @param fileName name of the save file within that folder
     */
    public Noms(String directory, String fileName) {
        ui = new Ui();
        storage = new Storage(directory, fileName);

        TaskList loaded;
        try {
            loaded = new TaskList(storage.load());
        } catch (IOException e) {
            loaded = new TaskList();
            ui.showError("Noms couldn't load the saved menu, starting with an empty plate: " + e.getMessage());
        }
        tasks = loaded;
    }

    /**
     * Runs the main loop: greets the user, then repeatedly reads a command,
     * carries it out, and prints the response, until the user says bye or
     * the input ends.
     */
    public void run() {
        ui.showWelcome();

        while (ui.hasNextCommand()) {
            String command = ui.readCommand();

            CommandType commandType;
            try {
                commandType = Parser.getCommandType(command);
            } catch (NomsException e) {
                ui.showError(e.getMessage());
                continue;
            }

            if (commandType == CommandType.BYE) {
                ui.showGoodbye();
                break;
            } else if (commandType == CommandType.LIST) {
                ui.showTaskList(tasks.asList());
            } else if (commandType == CommandType.MARK) {
                try {
                    int taskNumber = Parser.parseTaskNumber(command, "mark", tasks.size());
                    Task task = tasks.get(taskNumber - 1);
                    task.markAsDone();
                    saveTasks();
                    ui.showTaskMarked(task);
                } catch (NomsException e) {
                    ui.showError(e.getMessage());
                }
            } else if (commandType == CommandType.UNMARK) {
                try {
                    int taskNumber = Parser.parseTaskNumber(command, "unmark", tasks.size());
                    Task task = tasks.get(taskNumber - 1);
                    task.markAsNotDone();
                    saveTasks();
                    ui.showTaskUnmarked(task);
                } catch (NomsException e) {
                    ui.showError(e.getMessage());
                }
            } else if (commandType == CommandType.DELETE) {
                try {
                    int taskNumber = Parser.parseTaskNumber(command, "delete", tasks.size());
                    Task deletedTask = tasks.delete(taskNumber - 1);
                    saveTasks();
                    ui.showTaskDeleted(deletedTask, tasks.size());
                } catch (NomsException e) {
                    ui.showError(e.getMessage());
                }
            } else if (commandType == CommandType.ON) {
                try {
                    LocalDate date = Parser.parseOnDate(command);
                    ui.showTasksOn(date, tasks.tasksOn(date));
                } catch (NomsException e) {
                    ui.showError(e.getMessage());
                }
            } else {
                Task task;
                try {
                    task = Parser.parseTask(command);
                } catch (NomsException e) {
                    ui.showError(e.getMessage());
                    continue;
                }
                tasks.add(task);
                saveTasks();
                ui.showTaskAdded(task, tasks.size());
            }
        }

        ui.close();
    }

    /**
     * Saves the current task list to disk, reporting a Noms-style error
     * if the save fails instead of crashing the program.
     */
    private void saveTasks() {
        try {
            storage.save(tasks.asList());
        } catch (IOException e) {
            ui.showError("Noms couldn't save the menu to disk: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Noms(SAVE_DIRECTORY, SAVE_FILE_NAME).run();
    }
}
