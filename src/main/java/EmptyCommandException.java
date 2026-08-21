/**
 * Indicates that Noms received an empty command.
 */
public class EmptyCommandException extends NomsException {
    public EmptyCommandException() {
        super("Noms needs a command. Try feeding me a todo, deadline, event, list, "
                + "mark, unmark, delete, or bye.");
    }
}
