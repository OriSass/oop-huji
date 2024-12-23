package exceptions;

import static utils.Constants.DID_NOT;
import static utils.Constants.DUE_TO_EXCEEDING_BOUNDARIES;

/**
 * The OutOfBoundsException class represents an exception that is thrown
 * when an action exceeds the defined boundaries.
 */
public class OutOfBoundsException extends RuntimeException {

    /**
     * Constructs a new OutOfBoundsException with the specified action.
     *
     * @param action the action that caused the exception
     */
    public OutOfBoundsException(String action) {
        super(DID_NOT + action + DUE_TO_EXCEEDING_BOUNDARIES);
    }
}
