/**
 * Marks a task as not done. The task number is validated against the
 * current list size at execution time.
 */
public class UnmarkCommand extends Command {
    private final String command;

    /**
     * @param command the full command line, e.g. {@code "unmark 2"}
     */
    public UnmarkCommand(String command) {
        this.command = command;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws NomsException {
        int taskNumber = Parser.parseTaskNumber(command, "unmark", tasks.size());
        Task task = tasks.get(taskNumber - 1);
        task.markAsNotDone();
        save(tasks, ui, storage);
        ui.showTaskUnmarked(task);
    }
}
