/**
 * Marks a task as done. The task number is validated against the current
 * list size at execution time, since the list can change between commands.
 */
public class MarkCommand extends Command {
    private final String command;

    /**
     * @param command the full command line, e.g. {@code "mark 2"}
     */
    public MarkCommand(String command) {
        this.command = command;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws NomsException {
        int taskNumber = Parser.parseTaskNumber(command, "mark", tasks.size());
        Task task = tasks.get(taskNumber - 1);
        task.markAsDone();
        save(tasks, ui, storage);
        ui.showTaskMarked(task);
    }
}
