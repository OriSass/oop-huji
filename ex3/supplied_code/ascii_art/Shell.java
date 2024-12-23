package ascii_art;

import exceptions.IncorrectCommandException;
import shell_commands_handlers.*;

import java.util.HashMap;

import static utils.Constants.DID_NOT_EXECUTE_DUE_TO_INCORRECT_COMMAND;

public class Shell {

    // commands
    private static final String EXIT = "exit";
    private static final String CHARS = "chars";
    private static final String ADD = "add";
    private static final String REMOVE = "remove";
    private static final String RES = "res";
    private static final String OUTPUT = "output";
    private static final String ROUND = "round";
    private static final String ASCII_ART = "asciiArt";

    private static final String INPUT_ARROWS = ">>>";

    private HashMap<String, ShellCommandHandler> commandMap;

    public Shell(){}

    private void initCommandMap(AsciiArtAlgorithm asciiArtAlgorithm) {
        this.commandMap = new HashMap<>();
        commandMap.put(CHARS, new CharsCommandHandler(asciiArtAlgorithm));
        commandMap.put(ADD, new AddCommandHandler(asciiArtAlgorithm));
        commandMap.put(REMOVE, new RemoveCommandHandler(asciiArtAlgorithm));
        commandMap.put(RES, new ResolutionCommandHandler(asciiArtAlgorithm));
        commandMap.put(OUTPUT, new OutputCommandHandler(asciiArtAlgorithm));
        commandMap.put(ROUND, new RoundCommandHandler(asciiArtAlgorithm));
        commandMap.put(ASCII_ART, new AsciiArtCommandHandler(asciiArtAlgorithm));
    }

    public void run(String imageName){
        try{
            AsciiArtAlgorithm asciiArtAlgorithm = new AsciiArtAlgorithm(imageName);
            initCommandMap(asciiArtAlgorithm);
            String cmnd = "";
            String param = "";
            String userInput = "";

            System.out.print(INPUT_ARROWS);
            userInput = KeyboardInput.readLine();
            cmnd = userInput.split(" ")[0];
            param = getCommandParam(userInput);

            while (!cmnd.equals(EXIT)){
                runCommand(cmnd, param);
                System.out.print(INPUT_ARROWS);
                userInput = KeyboardInput.readLine();
                cmnd = userInput.split(" ")[0];
                param = getCommandParam(userInput);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void runCommand(String cmnd, String param) throws Exception {
        if(!commandMap.containsKey(cmnd)){
            throw new IncorrectCommandException(DID_NOT_EXECUTE_DUE_TO_INCORRECT_COMMAND);
        }
        ShellCommandHandler commandHandler = commandMap.get(cmnd);
        commandHandler.handleCommand(param);
    }

    private String getCommandParam(String userInput){
        if(userInput.split(" ").length > 1){
            return userInput.split(" ")[1];
        }
        return null;
    }

    public static void main(String[] args) {
        Shell shell = new Shell();
        shell.run(args[0]);
    }
}
