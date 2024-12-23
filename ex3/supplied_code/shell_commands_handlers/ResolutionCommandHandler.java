package shell_commands_handlers;

import ascii_art.AsciiArtAlgorithm;
import exceptions.IncorrectFormatException;
import exceptions.OutOfBoundsException;

public class ResolutionCommandHandler implements ShellCommandHandler {

    private final AsciiArtAlgorithm algorithm;

    private static final String ATTEMPTED_TO = "change resolution";


    public ResolutionCommandHandler(AsciiArtAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public void handleCommand(String param) throws Exception {
        if(!param.isEmpty() && !param.equals("up") && !param.equals("down")){
            throw new IncorrectFormatException(ATTEMPTED_TO);
        }
        int newResolution = getNewResolution(param);
        algorithm.setResolution(newResolution);
        System.out.println("Resolution set to " + newResolution);
    }

    private int getNewResolution(String param) throws Exception {
        int currentResolution = algorithm.getResolution();
        int newResolution;
        if(param.equals("up")){
            newResolution = currentResolution * 2;
            if(newResolution > this.algorithm.getMaxResolution()){
                throw new OutOfBoundsException(ATTEMPTED_TO);
            }
        }
        else {
            newResolution = currentResolution / 2;
            if(newResolution < this.algorithm.getMinResolution()){
                throw new OutOfBoundsException(ATTEMPTED_TO);
            }
        }
        return newResolution;
    }
}
