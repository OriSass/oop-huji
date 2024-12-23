package shell_commands_handlers;

import ascii_art.AsciiArtAlgorithm;
import exceptions.IncorrectFormatException;

public class AddCommandHandler implements ShellCommandHandler {

    private final AsciiArtAlgorithm algorithm;

    public AddCommandHandler(AsciiArtAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public void handleCommand(String param) throws Exception{
        if(param == null) {
            return;
        }
        else if (param.length() == 1){
            this.algorithm.addChar(param.charAt(0));
        }
        else{
            // case all
            if (param.equals("all")){
                addAllChars();
            }
            // case space
            else if (param.equals("space")){
                this.algorithm.addChar(' ');
            }
            // case range
            else if(param.contains("-")){
                addCharsInRange(param);
            }
            else{
                throw new IncorrectFormatException("add");
            }
        }
    }

    private void addCharsInRange(String param) {
        String[] range = param.split("-");

        char leftChar = range[0].charAt(0);
        char rightChar = range[1].charAt(0);

        char start = (char) Math.min(leftChar, rightChar);
        char end = (char) Math.max(leftChar, rightChar);
        for (char c = start; c <= end; c++) {
            this.algorithm.addChar(c);
        }
    }

    private void addAllChars() {
        for (char c = ' '; c <= '~'; c++) {
            this.algorithm.addChar(c);
        }
    }
}
