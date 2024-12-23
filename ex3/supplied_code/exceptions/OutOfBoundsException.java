package exceptions;

import static utils.Constants.DID_NOT;
import static utils.Constants.DUE_TO_EXCEEDING_BOUNDARIES;

public class OutOfBoundsException extends RuntimeException {
    public OutOfBoundsException(String action) {

        super(DID_NOT + action + DUE_TO_EXCEEDING_BOUNDARIES);
    }
}
