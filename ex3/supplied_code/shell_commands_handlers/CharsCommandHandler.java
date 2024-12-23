package shell_commands_handlers;

import ascii_art.AsciiArtAlgorithm;

import java.util.Arrays;

public class CharsCommandHandler implements ShellCommandHandler {

    private final AsciiArtAlgorithm algorithm;

    public CharsCommandHandler(AsciiArtAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public void handleCommand(String param) {
        printCharSet(algorithm.getCharset());
    }

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
