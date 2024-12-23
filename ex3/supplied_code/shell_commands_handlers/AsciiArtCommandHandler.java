package shell_commands_handlers;

import ascii_art.AsciiArtAlgorithm;

import static utils.Constants.DIDNT_EXECUTE_CHARSET_TOO_SMALL;

/**
 * The AsciiArtCommandHandler class handles the "ascii art" command for generating ASCII art from an image.
 */
public class AsciiArtCommandHandler implements ShellCommandHandler {

    /**
     * The AsciiArtAlgorithm instance used to generate ASCII art.
     */
    private final AsciiArtAlgorithm algorithm;


    /**
     * Constructs an AsciiArtCommandHandler with the specified AsciiArtAlgorithm.
     *
     * @param algorithm the AsciiArtAlgorithm instance to use
     */
    public AsciiArtCommandHandler(AsciiArtAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * Handles the "ascii art" command with the specified parameter.
     *
     * @param param the parameter for the "ascii art" command
     * @throws Exception if an error occurs while handling the command
     */
    @Override
    public void handleCommand(String param) throws Exception {
        if(algorithm.getCharset().length < 2){
            throw new Exception(DIDNT_EXECUTE_CHARSET_TOO_SMALL);
        }
        char[][] asciiArt = algorithm.run();
        algorithm.getAsciiOutput().out(asciiArt);
    }
}
