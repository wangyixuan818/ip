/**
 * Represents a task in Noms' task list.
 *
 * A task owns both its description and completion status so that task-related
 * state changes are handled by the task itself.
 */
public class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates a new incomplete task with the given description.
     *
     * @param description the text describing the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the text used to display this task's completion status.
     *
     * @return {@code X} for a completed task, or a space otherwise
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /** Marks this task as completed. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as incomplete. */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns this task's description.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    /**
     * Returns this task's representation for saving to disk, as a
     * pipe-separated line: {@code <done flag> | <description>}.
     *
     * Subclasses prepend their type letter and append any extra fields
     * (e.g. a deadline's date or an event's start/end times), mirroring
     * how {@link #toString()} is built up via {@code super.toString()}.
     *
     * @return the save-file line for this task, without its type marker
     */
    public String toFileFormat() {
        return (isDone ? "1" : "0") + " | " + description;
    }
}
