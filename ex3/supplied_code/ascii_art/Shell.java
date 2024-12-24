package ascii_art;

import exceptions.IncorrectCommandException;
import shell_commands_handlers.*;

import java.util.HashMap;

import static utils.Constants.DID_NOT_EXECUTE_DUE_TO_INCORRECT_COMMAND;

/**
 * The Shell class provides a command-line interface for interacting with the AsciiArtAlgorithm.
 */
public class Shell {

    // commands
    /**
     * Command to exit the shell.
     */
    private static final String EXIT = "exit";
    /**
     * Command to display the current character set.
     */
    private static final String CHARS = "chars";
    /**
     * Command to add a character to the character set.
     */
    private static final String ADD = "add";
    /**
     * Command to remove a character from the character set.
     */
    private static final String REMOVE = "remove";
    /**
     * Command to set the resolution of the ASCII art.
     */
    private static final String RES = "res";
    /**
     * Command to set the output method of the ASCII art.
     */
    private static final String OUTPUT = "output";
    /**
     * Command to set the rounding method of the ASCII art.
     */
    private static final String ROUND = "round";
    /**
     * Command to generate and display the ASCII art.
     */
    private static final String ASCII_ART = "asciiArt";
    /**
     * The prompt string for user input.
     */
    private static final String INPUT_ARROWS = ">>> ";

    /**
     * A map of command strings to their corresponding handlers.
     */
    private HashMap<String, ShellCommandHandler> commandMap;

    /**
     * Constructs a Shell instance.
     */
    public Shell(){}

    /**
     * Initializes the command map with the given AsciiArtAlgorithm instance.
     *
     * @param asciiArtAlgorithm the AsciiArtAlgorithm instance to use for command handling
     */
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

    /**
     * Runs the shell with the specified image name.
     *
     * @param imageName the name of the image file to convert to ASCII art
     */
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

    /**
     * Executes the specified command with the given parameter.
     *
     * @param cmnd the command to execute
     * @param param the parameter for the command
     * @throws Exception if an error occurs during command execution
     */
    private void runCommand(String cmnd, String param) throws Exception {
        if(!commandMap.containsKey(cmnd)){
            throw new IncorrectCommandException(DID_NOT_EXECUTE_DUE_TO_INCORRECT_COMMAND);
        }
        ShellCommandHandler commandHandler = commandMap.get(cmnd);
        commandHandler.handleCommand(param);
    }

    /**
     * Extracts the parameter from the user input string.
     *
     * @param userInput the user input string
     * @return the extracted parameter, or null if no parameter is present
     */
    private String getCommandParam(String userInput){
        if(userInput.split(" ").length > 1){
            return userInput.split(" ")[1];
        }
        return null;
    }

    /**
     * The main method to start the shell.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        Shell shell = new Shell();
        shell.run(args[0]);
    }
}
