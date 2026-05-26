import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Handles saving and displaying job execution history.
 *
 * For now, history is stored in a local text file called history.txt.
 * Later, this class can be upgraded to store execution history in SQLite.
 */
public class HistoryRepository {
    private static final String HISTORY_FILE = "history.txt";

    /**
     * Saves a job execution record to history.txt.
     *
     * @param job job that was executed
     * @param exitCode exit code returned by the command
     */
    public void saveExecution(Job job, int exitCode) {
        String status = exitCode == 0 ? "SUCCESS" : "FAILED";
        String timestamp = getCurrentTimestamp();

        String historyLine = job.getId()
                + "|" + job.getName()
                + "|" + job.getCommand()
                + "|" + status
                + "|" + exitCode
                + "|" + timestamp;

        try (FileWriter writer = new FileWriter(HISTORY_FILE, true)) {
            writer.write(historyLine + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Error saving history: " + e.getMessage());
        }
    }

    /**
     * Prints all saved execution history records.
     */
    public void printHistory() {
        File file = new File(HISTORY_FILE);

        if (!file.exists()) {
            System.out.println("No execution history found.");
            return;
        }

        System.out.println("Execution History:");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                printFormattedHistoryLine(line);
            }

        } catch (IOException e) {
            System.out.println("Error reading history: " + e.getMessage());
        }
    }

    /**
     * Formats one saved history line for terminal display.
     *
     * Saved format:
     * jobId|jobName|command|status|exitCode|timestamp
     *
     * @param line saved history line from history.txt
     */
    private void printFormattedHistoryLine(String line) {
        String[] parts = line.split("\\|", 6);

        if (parts.length < 6) {
            System.out.println("Invalid history entry: " + line);
            return;
        }

        String jobId = parts[0];
        String jobName = parts[1];
        String command = parts[2];
        String status = parts[3];
        String exitCode = parts[4];
        String timestamp = parts[5];

        System.out.println(
                jobId + " | "
                        + jobName + " | "
                        + command + " | "
                        + status + " | Exit Code: "
                        + exitCode + " | "
                        + timestamp
        );
    }

    /**
     * Gets the current timestamp in a readable format.
     *
     * @return formatted current timestamp
     */
    private String getCurrentTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }
}