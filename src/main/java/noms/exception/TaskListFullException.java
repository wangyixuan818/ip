package noms.exception;

/**
 * Indicates that Noms cannot add another task because its task list is full.
 */
public class TaskListFullException extends NomsException {
    /** Creates the exception with a message that the task list is full. */
    public TaskListFullException() {
        super("Noms is full! There is no room for another task.");
    }
}
