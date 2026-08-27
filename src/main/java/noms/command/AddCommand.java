package noms.command;

import noms.parser.Parser;
import noms.storage.Storage;
import noms.task.Task;
import noms.task.TaskList;
import noms.ui.Ui;

/**
 * Adds a ready-made task (todo, deadline, or event) to the task list.
 * The task is built by the {@link Parser} before this command is created,
 * so executing it simply stores and confirms the task.
 */
public class AddCommand extends Command {
    private final Task task;

    /**
     * @param task the task to add, already parsed from the user's command
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.add(task);
        save(tasks, ui, storage);
        ui.showTaskAdded(task, tasks.size());
    }
}
