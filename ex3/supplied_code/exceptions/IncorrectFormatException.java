package exceptions;

import static utils.Constants.*;

public class IncorrectFormatException extends RuntimeException {


  public IncorrectFormatException(String action) {
        super(DID_NOT + action + DUE_TO + INCORRECT_FORMAT);
    }
}
