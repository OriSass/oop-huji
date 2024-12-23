package shell_commands_handlers;

/**
 * The ShellCommandHandler interface defines the method for handling shell commands.
 */
public interface ShellCommandHandler {
    /**
     * Handles the specified shell command with the given parameter.
     *
     * @param param the parameter for the shell command
     * @throws Exception if an error occurs while handling the command
     */
    void handleCommand(String param) throws Exception;
}
