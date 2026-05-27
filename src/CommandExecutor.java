import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Executes Linux shell commands from the Java application.
 *
 * This class uses ProcessBuilder to run commands through Bash,
 * capture command output, capture command errors, and return
 * the command's exit code.
 */
public class CommandExecutor {

    /**
     * Executes a Linux shell command and returns its exit code.
     *
     * Exit code 0 usually means the command succeeded.
     * Any non-zero exit code usually means the command failed.
     *
     * @param command Linux shell command to execute
     * @return exit code returned by the executed process
     */
    public int execute(String command) {
        int exitCode = -1;

        try {
            ProcessBuilder builder = new ProcessBuilder("bash", "-c", command);
            Process process = builder.start();

            BufferedReader outputReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()));

            String line;

            System.out.println("----- Command Output -----");
            while ((line = outputReader.readLine()) != null) {
                System.out.println(line);
            }

            System.out.println("----- Command Errors -----");
            while ((line = errorReader.readLine()) != null) {
                System.out.println(line);
            }

            exitCode = process.waitFor();
            System.out.println("Exit Code: " + exitCode);

        } catch (IOException e) {
            System.out.println("Error running command: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Command was interrupted.");
            Thread.currentThread().interrupt();
        }

        return exitCode;
    }
}