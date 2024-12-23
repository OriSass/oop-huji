package exceptions;

/**
 * The IncorrectCommandException class represents an exception that is thrown
 * when an incorrect command is encountered in the shell.
 */
public class IncorrectCommandException extends RuntimeException {
    /**
     * Constructs a new IncorrectCommandException with the specified detail message.
     *
     * @param message the detail message
     */
    public IncorrectCommandException(String message) {
        super(message);
    }
}
