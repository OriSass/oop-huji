package shell_commands_handlers;

import ascii_art.AsciiArtAlgorithm;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import exceptions.IncorrectFormatException;

public class OutputCommandHandler implements ShellCommandHandler {

    private final AsciiArtAlgorithm algorithm;

    public OutputCommandHandler(AsciiArtAlgorithm algorithm) {
        this.algorithm = algorithm;
    }


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
