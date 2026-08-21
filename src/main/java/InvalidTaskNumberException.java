/**
 * Indicates that a mark, unmark, or delete command contains an invalid task number.
 */
public class InvalidTaskNumberException extends NomsException {
    public InvalidTaskNumberException(String message) {
        super(message);
    }
}
