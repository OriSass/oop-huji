package exceptions;

import static utils.Constants.*;

/**
 * The IncorrectFormatException class represents an exception that is thrown
 * when an incorrect format is encountered in the shell.
 */
public class IncorrectFormatException extends RuntimeException {

  /**
   * Constructs a new IncorrectFormatException with the specified action.
   *
   * @param action the action that caused the exception
   */
  public IncorrectFormatException(String action) {
        super(DID_NOT + action + DUE_TO + INCORRECT_FORMAT);
    }
}
