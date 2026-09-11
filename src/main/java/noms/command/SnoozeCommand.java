package noms.command;

import noms.exception.InvalidSnoozeException;
import noms.exception.NomsException;
import noms.parser.Parser;
import noms.parser.Parser.EventSnoozeDates;
import noms.storage.Storage;
import noms.task.Deadline;
import noms.task.Event;
import noms.task.Task;
import noms.task.TaskList;
import noms.ui.Ui;

/** Reschedules a deadline or event selected by its displayed task number. */
public class SnoozeCommand extends Command {
    private final String command;

    /**
     * Creates a command that snoozes the dated task named by the command line.
     *
     * @param command the full snooze command
     */
    public SnoozeCommand(String command) {
        this.command = command;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws NomsException {
        int taskNumber = Parser.parseSnoozeTaskNumber(command, tasks.size());
        assert taskNumber >= 1 && taskNumber <= tasks.size()
                : "Parser returned an invalid task number";
        Task task = tasks.get(taskNumber - 1);

        if (task instanceof Deadline deadline) {
            deadline.rescheduleTo(Parser.parseDeadlineSnoozeDate(command));
        } else if (task instanceof Event event) {
            rescheduleEvent(event);
        } else {
            throw new InvalidSnoozeException(
                    "Noms can only snooze deadlines and events.\n"
                            + "Try choosing a task that has a date.");
        }

        save(tasks, ui, storage);
        ui.showTaskSnoozed(task);
    }

    /** Reschedules an event using its replacement start and optional end date. */
    private void rescheduleEvent(Event event) throws NomsException {
        EventSnoozeDates dates = Parser.parseEventSnoozeDates(command);
        if (dates.endDate().isPresent()) {
            event.reschedule(dates.startDate(), dates.endDate().get());
        } else {
            event.rescheduleFrom(dates.startDate());
        }
    }
}
