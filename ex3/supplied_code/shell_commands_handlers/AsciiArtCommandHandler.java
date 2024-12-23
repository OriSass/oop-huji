package shell_commands_handlers;

import ascii_art.AsciiArtAlgorithm;

import static utils.constants.DIDNT_EXECUTE_CHARSET_TOO_SMALL;

public class AsciiArtCommandHandler implements ShellCommandHandler {

    private final AsciiArtAlgorithm algorithm;

    public AsciiArtCommandHandler(AsciiArtAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public void handleCommand(String param) {
        if(algorithm.getCharset().length < 2){
            System.out.println(DIDNT_EXECUTE_CHARSET_TOO_SMALL);
            return;
        }
        char[][] asciiArt = algorithm.run();
        algorithm.getAsciiOutput().out(asciiArt);
    }
}
