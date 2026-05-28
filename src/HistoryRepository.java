import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Handles database operations for job execution history.
 *
 * This class stores and displays past job executions using SQLite.
 */
public class HistoryRepository {

    /**
     * Saves a job execution record to the SQLite database.
     *
     * @param job      job that was executed
     * @param exitCode exit code returned by the command
     */
    public void saveExecution(Job job, int exitCode) {
        String sql = """
                INSERT INTO history
                (job_id, job_name, command, status, exit_code, executed_at)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        String status = exitCode == 0 ? "SUCCESS" : "FAILED";
        String timestamp = getCurrentTimestamp();

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, job.getId());
            statement.setString(2, job.getName());
            statement.setString(3, job.getCommand());
            statement.setString(4, status);
            statement.setInt(5, exitCode);
            statement.setString(6, timestamp);

            statement.executeUpdate();

        } catch (SQLException e) {
            AppLogger.error("Error saving history to database: " + e.getMessage());
        }
    }

    /**
     * Prints all saved execution history records from SQLite.
     */
    public void printHistory() {
        String sql = """
                SELECT job_id, job_name, command, status, exit_code, executed_at
                FROM history
                ORDER BY id
                """;

        boolean hasHistory = false;

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            AppLogger.info("Execution History:");

            while (resultSet.next()) {
                hasHistory = true;

                System.out.println(
                        resultSet.getInt("job_id") + " | "
                                + resultSet.getString("job_name") + " | "
                                + resultSet.getString("command") + " | "
                                + resultSet.getString("status") + " | Exit Code: "
                                + resultSet.getInt("exit_code") + " | "
                                + resultSet.getString("executed_at"));
            }

            if (!hasHistory) {
                AppLogger.warning("No execution history found.");
            }

        } catch (SQLException e) {
            AppLogger.error("Error reading history from database: " + e.getMessage());
        }
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