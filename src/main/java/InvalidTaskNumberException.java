/**
 * Indicates that a mark or unmark command contains an invalid task number.
 */
public class InvalidTaskNumberException extends NomsException {
    public InvalidTaskNumberException(String message) {
        super(message);
    }
}
