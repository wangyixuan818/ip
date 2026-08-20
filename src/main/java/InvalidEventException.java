/**
 * Indicates that an event command is missing or has invalid event details.
 */
public class InvalidEventException extends NomsException {
    public InvalidEventException(String usage) {
        super("This event recipe needs more ingredients.\nTry: " + usage);
    }
}
