package shell_commands_handlers;

import ascii_art.AsciiArtAlgorithm;
import ascii_art.RoundMethod;
import exceptions.IncorrectFormatException;

/**
 * The RoundCommandHandler class handles the "round" command for changing the rounding method of the AsciiArtAlgorithm.
 */
public class RoundCommandHandler implements ShellCommandHandler {

    /**
     * The AsciiArtAlgorithm instance whose rounding method will be changed.
     */
    private final AsciiArtAlgorithm algorithm;

    /**
     * Constructs a RoundCommandHandler with the specified AsciiArtAlgorithm.
     *
     * @param algorithm the AsciiArtAlgorithm instance to use
     */
    public RoundCommandHandler(AsciiArtAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * Handles the "round" command with the specified parameter.
     *
     * @param param the parameter for the "round" command
     * @throws IncorrectFormatException if the parameter is invalid
     */
    @Override
    public void handleCommand(String param) {
        if(param.isEmpty() || !param.equals("up") && !param.equals("down") && !param.equals("abs")){
            throw new IncorrectFormatException("change rounding method");
        }
        RoundMethod newRoundMethod = RoundMethod.fromString(param);
        algorithm.setRoundMethod(newRoundMethod);
    }
}
