/**
 * Indicates that a deadline command is missing or has invalid deadline details.
 */
public class InvalidDeadlineException extends NomsException {
    public InvalidDeadlineException(String usage) {
        super("This deadline recipe is incomplete.\nTry: " + usage);
    }
}
