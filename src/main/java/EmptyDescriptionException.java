/**
 * Indicates that a task was created without a description.
 */
public class EmptyDescriptionException extends NomsException {
    public EmptyDescriptionException(String taskType, String usage) {
        super("This " + taskType
                + " is missing its main ingredient: a description.\n"
                + "Try: " + usage);
    }
}
