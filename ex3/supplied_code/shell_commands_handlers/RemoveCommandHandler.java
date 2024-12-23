package shell_commands_handlers;

import ascii_art.AsciiArtAlgorithm;

public class RemoveCommandHandler implements ShellCommandHandler {

    private static final String REMOVE_INCORRECT_FORMAT_MSG = "Did not remove due to incorrect format.";
    private final AsciiArtAlgorithm algorithm;

    public RemoveCommandHandler(AsciiArtAlgorithm algorithm) {
        this.algorithm = algorithm;
    }


    @Override
    public void handleCommand(String param) {
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
                System.out.println(REMOVE_INCORRECT_FORMAT_MSG);
            }
        }

    }

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

    private void removeAllChars() {
        for (char c = ' '; c <= '~'; c++) {
            this.algorithm.removeChar(c);
        }
    }
}
