import java.io.IOException;
import java.time.LocalDate;

public class Noms {
    private static final String SAVE_DIRECTORY = "data";
    private static final String SAVE_FILE_NAME = "noms.txt";

    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();

        Storage storage = new Storage(SAVE_DIRECTORY, SAVE_FILE_NAME);
        TaskList tasks;
        try {
            tasks = new TaskList(storage.load());
        } catch (IOException e) {
            tasks = new TaskList();
            ui.showError("Noms couldn't load the saved menu, starting with an empty plate: " + e.getMessage());
        }

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
                    saveTasks(storage, tasks, ui);
                    ui.showTaskMarked(task);
                } catch (NomsException e) {
                    ui.showError(e.getMessage());
                }
            } else if (commandType == CommandType.UNMARK) {
                try {
                    int taskNumber = Parser.parseTaskNumber(command, "unmark", tasks.size());
                    Task task = tasks.get(taskNumber - 1);
                    task.markAsNotDone();
                    saveTasks(storage, tasks, ui);
                    ui.showTaskUnmarked(task);
                } catch (NomsException e) {
                    ui.showError(e.getMessage());
                }
            } else if (commandType == CommandType.DELETE) {
                try {
                    int taskNumber = Parser.parseTaskNumber(command, "delete", tasks.size());
                    Task deletedTask = tasks.delete(taskNumber - 1);
                    saveTasks(storage, tasks, ui);
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
                saveTasks(storage, tasks, ui);
                ui.showTaskAdded(task, tasks.size());
            }
        }

        ui.close();
    }

    /**
     * Saves the current task list to disk, reporting a Noms-style error
     * if the save fails instead of crashing the program.
     */
    private static void saveTasks(Storage storage, TaskList tasks, Ui ui) {
        try {
            storage.save(tasks.asList());
        } catch (IOException e) {
            ui.showError("Noms couldn't save the menu to disk: " + e.getMessage());
        }
    }
}
