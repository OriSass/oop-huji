package shell_commands_handlers;

import ascii_art.AsciiArtAlgorithm;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;

import static utils.constants.DID_NOT_CHANGE;
import static utils.constants.DUE_TO_INCORRECT_FORMAT;

public class OutputCommandHandler implements ShellCommandHandler {

    private final AsciiArtAlgorithm algorithm;

    public OutputCommandHandler(AsciiArtAlgorithm algorithm) {
        this.algorithm = algorithm;
    }


    @Override
    public void handleCommand(String param) {
        try{
            if(param.isEmpty() || !param.equals("console") && !param.equals("html")){
                throw new IllegalArgumentException();
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
        }  catch (IllegalArgumentException e){
            System.out.println(DID_NOT_CHANGE + "output" + DUE_TO_INCORRECT_FORMAT);
        }
    }
}
