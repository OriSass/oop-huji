package shell_commands_handlers;

import ascii_art.AsciiArtAlgorithm;
import exceptions.IncorrectFormatException;
import exceptions.OutOfBoundsException;

/**
 * The ResolutionCommandHandler class handles the "resolution" command for
 * changing the resolution of the AsciiArtAlgorithm.
 */
public class ResolutionCommandHandler implements ShellCommandHandler {

    /**
     * The AsciiArtAlgorithm instance whose resolution will be changed.
     */
    private final AsciiArtAlgorithm algorithm;

    /**
     * The error message for incorrect format.
     */
    private static final String ATTEMPTED_TO = "change resolution";

    /**
     * Constructs a ResolutionCommandHandler with the specified AsciiArtAlgorithm.
     *
     * @param algorithm the AsciiArtAlgorithm instance to use
     */
    public ResolutionCommandHandler(AsciiArtAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * Handles the "resolution" command with the specified parameter.
     *
     * @param param the parameter for the "resolution" command
     * @throws Exception if an error occurs while handling the command
     */
    @Override
    public void handleCommand(String param) throws Exception {
        if(!param.isEmpty() && !param.equals("up") && !param.equals("down")){
            throw new IncorrectFormatException(ATTEMPTED_TO);
        }
        int newResolution = getNewResolution(param);
        algorithm.setResolution(newResolution);
        System.out.println("Resolution set to " + newResolution);
    }

    /**
     * Calculates the new resolution based on the specified parameter.
     *
     * @param param the parameter for the "resolution" command
     * @return the new resolution
     * @throws Exception if an error occurs while calculating the new resolution
     */
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
