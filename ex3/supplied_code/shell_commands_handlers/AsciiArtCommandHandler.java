package shell_commands_handlers;

import ascii_art.AsciiArtAlgorithm;

import static utils.Constants.DIDNT_EXECUTE_CHARSET_TOO_SMALL;

public class AsciiArtCommandHandler implements ShellCommandHandler {

    private final AsciiArtAlgorithm algorithm;

    public AsciiArtCommandHandler(AsciiArtAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public void handleCommand(String param) throws Exception {
        if(algorithm.getCharset().length < 2){
            throw new Exception(DIDNT_EXECUTE_CHARSET_TOO_SMALL);
        }
        char[][] asciiArt = algorithm.run();
        algorithm.getAsciiOutput().out(asciiArt);
    }
}
