package shell_commands_handlers;

import ascii_art.AsciiArtAlgorithm;

import java.util.Arrays;

/**
 * The CharsCommandHandler class handles the "chars" command for printing the character set used by the AsciiArtAlgorithm.
 */
public class CharsCommandHandler implements ShellCommandHandler {

    /**
     * The AsciiArtAlgorithm instance whose character set will be printed.
     */
    private final AsciiArtAlgorithm algorithm;

    /**
     * Constructs a CharsCommandHandler with the specified AsciiArtAlgorithm.
     *
     * @param algorithm the AsciiArtAlgorithm instance to use
     */
    public CharsCommandHandler(AsciiArtAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * Handles the "chars" command with the specified parameter.
     *
     * @param param the parameter for the "chars" command
     */
    @Override
    public void handleCommand(String param) {
        printCharSet(algorithm.getCharset());
    }

    /**
     * Prints the character set used by the AsciiArtAlgorithm.
     *
     * @param charset the character set to print
     */
    private void printCharSet(char[] charset) {
        if(charset == null || charset.length == 0){
            return;
        }
        Arrays.sort(charset);
        for (char c : charset) {
            System.out.print(c + " ");
        }
        System.out.println();
    }
}
