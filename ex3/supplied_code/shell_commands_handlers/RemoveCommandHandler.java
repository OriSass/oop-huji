package shell_commands_handlers;

import ascii_art.AsciiArtAlgorithm;
import exceptions.IncorrectFormatException;

/**
 * The RemoveCommandHandler class handles the "remove" command for removing characters from the AsciiArtAlgorithm.
 */
public class RemoveCommandHandler implements ShellCommandHandler {

    /**
     * The AsciiArtAlgorithm instance from which characters will be removed.
     */
    private final AsciiArtAlgorithm algorithm;

    /**
     * Constructs a RemoveCommandHandler with the specified AsciiArtAlgorithm.
     *
     * @param algorithm the AsciiArtAlgorithm instance to use
     */
    public RemoveCommandHandler(AsciiArtAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * Handles the "remove" command with the specified parameter.
     *
     * @param param the parameter for the "remove" command
     * @throws Exception if an error occurs while handling the command
     */
    @Override
    public void handleCommand(String param) throws Exception {
        if(param == null) {
            return;
        }
        else if (param.length() == 1){
            this.algorithm.removeChar(param.charAt(0));
        }
        else{
            // case all
            if (param.equals("all")){
                removeAllChars();
            }
            // case space
            else if (param.equals("space")){
                this.algorithm.removeChar(' ');
            }
            // case range
            else if(param.contains("-")){
                removeCharsInRange(param);
            }
            else{
                throw new IncorrectFormatException("remove");
            }
        }

    }

    /**
     * Removes characters in the specified range from the AsciiArtAlgorithm.
     *
     * @param param the range parameter in the format "a-z"
     */
    private void removeCharsInRange(String param) {
        String[] range = param.split("-");
        if (range.length != 2) {
            System.out.println("Invalid command");
            return;
        }
        char leftChar = range[0].charAt(0);
        char rightChar = range[1].charAt(0);

        char start = (char) Math.min(leftChar, rightChar);
        char end = (char) Math.max(leftChar, rightChar);
        for (char c = start; c <= end; c++) {
            this.algorithm.removeChar(c);
        }
    }

    /**
     * Removes all printable ASCII characters from the AsciiArtAlgorithm.
     */
    private void removeAllChars() {
        for (char c = ' '; c <= '~'; c++) {
            this.algorithm.removeChar(c);
        }
    }
}
