package noms.exception;

/**
 * Indicates that a {@code find} command was given without a keyword to
 * search for.
 */
public class EmptyKeywordException extends NomsException {
    public EmptyKeywordException(String usage) {
        super("Noms can't sniff out a task without a scent!\n"
                + "Tell Noms a keyword to hunt for.\n"
                + "Try: " + usage);
    }
}
