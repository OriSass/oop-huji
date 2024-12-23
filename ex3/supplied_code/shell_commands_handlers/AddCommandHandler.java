package shell_commands_handlers;

import ascii_art.AsciiArtAlgorithm;
import exceptions.IncorrectFormatException;

/**
 * The AddCommandHandler class handles the "add" command for adding characters to the AsciiArtAlgorithm.
 */
public class AddCommandHandler implements ShellCommandHandler {

    /**
     * The AsciiArtAlgorithm instance to which characters will be added.
     */
    private final AsciiArtAlgorithm algorithm;

    /**
     * Constructs an AddCommandHandler with the specified AsciiArtAlgorithm.
     *
     * @param algorithm the AsciiArtAlgorithm instance to use
     */
    public AddCommandHandler(AsciiArtAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * Handles the "add" command with the specified parameter.
     *
     * @param param the parameter for the "add" command
     * @throws Exception if an error occurs while handling the command
     */
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

    /**
     * Adds characters in the specified range to the AsciiArtAlgorithm.
     *
     * @param param the range parameter in the format "a-z"
     */
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

    /**
     * Adds all printable ASCII characters to the AsciiArtAlgorithm.
     */
    private void addAllChars() {
        for (char c = ' '; c <= '~'; c++) {
            this.algorithm.addChar(c);
        }
    }
}
