package shell_commands_handlers;

import ascii_art.AsciiArtAlgorithm;
import ascii_art.RoundMethod;
import exceptions.IncorrectFormatException;

public class RoundCommandHandler implements ShellCommandHandler {

    private final AsciiArtAlgorithm algorithm;

    public RoundCommandHandler(AsciiArtAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public void handleCommand(String param) {
        if(param.isEmpty() || !param.equals("up") && !param.equals("down") && !param.equals("abs")){
            throw new IncorrectFormatException("change rounding method");
        }
        RoundMethod newRoundMethod = RoundMethod.fromString(param);
        algorithm.setRoundMethod(newRoundMethod);
    }
}
