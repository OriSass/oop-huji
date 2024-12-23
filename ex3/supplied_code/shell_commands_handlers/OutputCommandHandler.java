package shell_commands_handlers;

import ascii_art.AsciiArtAlgorithm;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import exceptions.IncorrectFormatException;

/**
 * The OutputCommandHandler class handles the "output" command for changing the output method
 * of the AsciiArtAlgorithm.
 */
public class OutputCommandHandler implements ShellCommandHandler {

    /**
     * The AsciiArtAlgorithm instance whose output method will be changed.
     */
    private final AsciiArtAlgorithm algorithm;

    /**
     * Constructs an OutputCommandHandler with the specified AsciiArtAlgorithm.
     *
     * @param algorithm the AsciiArtAlgorithm instance to use
     */
    public OutputCommandHandler(AsciiArtAlgorithm algorithm) {
        this.algorithm = algorithm;
    }


    /**
     * Handles the "output" command with the specified parameter.
     *
     * @param param the parameter for the "output" command
     * @throws Exception if an error occurs while handling the command
     */
    @Override
    public void handleCommand(String param) throws Exception {
        if(param.isEmpty() || !param.equals("console") && !param.equals("html")){
            throw new IncorrectFormatException("change output method");
        }
        if(param.equals("console")){
            if(!(algorithm.getAsciiOutput() instanceof ConsoleAsciiOutput)){
                algorithm.setAsciiOutput(new ConsoleAsciiOutput());
            }
        }
        // else html
        else {
            if(!(algorithm.getAsciiOutput() instanceof HtmlAsciiOutput)){
                algorithm.setAsciiOutput(new HtmlAsciiOutput("out.html", "Courier New"));
            }
        }
    }
}
