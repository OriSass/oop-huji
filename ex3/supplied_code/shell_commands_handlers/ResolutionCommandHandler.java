package shell_commands_handlers;

import ascii_art.AsciiArtAlgorithm;
import exceptions.OutOfResBoundsException;

import static utils.constants.*;

public class ResolutionCommandHandler implements ShellCommandHandler {

    private final AsciiArtAlgorithm algorithm;

    public ResolutionCommandHandler(AsciiArtAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public void handleCommand(String param) {
        try{
            if(param.isEmpty() || !param.equals("up") && !param.equals("down")){
                throw new IllegalArgumentException();
            }
            int newResolution = getNewResolution(param);
            algorithm.setResolution(newResolution);
            System.out.println("Resolution set to " + newResolution);
        } catch (OutOfResBoundsException e){
            System.out.println(DID_NOT_CHANGE + "resolution" + DUE_TO_EXCEEDING_BOUNDARIES);
        } catch (IllegalArgumentException e){
            System.out.println(DID_NOT_CHANGE + "resolution" + DUE_TO_INCORRECT_FORMAT);
        }
    }

    private int getNewResolution(String param) throws OutOfResBoundsException {
        int currentResolution = algorithm.getResolution();
        int newResolution;
        if(param.equals("up")){
            newResolution = currentResolution * 2;
            if(newResolution > this.algorithm.getMaxResolution()){
                throw new OutOfResBoundsException();
            }
        }
        else {
            newResolution = currentResolution / 2;
            if(newResolution < this.algorithm.getMinResolution()){
                throw new OutOfResBoundsException();
            }
        }
        return newResolution;
    }
}
