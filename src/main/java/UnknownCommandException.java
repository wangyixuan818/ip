/**
 * Indicates that Noms does not recognise a user's command.
 */
public class UnknownCommandException extends NomsException {
    public UnknownCommandException() {
        super("Grrr... Noms couldn't understand that command.\n"
                + "Try feeding me a todo, deadline, event, list, mark, unmark, delete, or bye.");
    }
}
