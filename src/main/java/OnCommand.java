import java.time.LocalDate;

/**
 * Shows the deadlines and events that occur on a given date. The date is
 * parsed by the {@link Parser} before this command is created.
 */
public class OnCommand extends Command {
    private final LocalDate date;

    /**
     * @param date the date to list tasks for
     */
    public OnCommand(LocalDate date) {
        this.date = date;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTasksOn(date, tasks.tasksOn(date));
    }
}
