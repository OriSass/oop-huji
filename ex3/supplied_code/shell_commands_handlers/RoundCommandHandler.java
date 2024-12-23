package shell_commands_handlers;

import ascii_art.AsciiArtAlgorithm;
import ascii_art.RoundMethod;

import static utils.constants.DID_NOT_CHANGE;
import static utils.constants.DUE_TO_INCORRECT_FORMAT;

public class RoundCommandHandler implements ShellCommandHandler {

    private final AsciiArtAlgorithm algorithm;

    public RoundCommandHandler(AsciiArtAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public void handleCommand(String param) {
        try{
            if(param.isEmpty() || !param.equals("up") && !param.equals("down") && !param.equals("abs")){
                throw new IllegalArgumentException();
            }
            RoundMethod newRoundMethod = RoundMethod.fromString(param);
            algorithm.setRoundMethod(newRoundMethod);
        }  catch (IllegalArgumentException e){
            System.out.println(DID_NOT_CHANGE + "rounding method" + DUE_TO_INCORRECT_FORMAT);
        }
    }
}
