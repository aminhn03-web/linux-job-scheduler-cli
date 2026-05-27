import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages the SQLite database connection and table setup.
 *
 * This class creates the scheduler.db database file and initializes
 * the jobs and history tables if they do not already exist.
 */
public class DatabaseManager {
    private static final String DATABASE_URL = "jdbc:sqlite:scheduler.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC driver not found: " + e.getMessage());
        }
    }

    /**
     * Opens a connection to the SQLite database.
     *
     * @return active database connection
     * @throws SQLException if the connection fails
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }

    /**
     * Creates the required database tables if they do not already exist.
     */
    public static void initializeDatabase() {
        String createJobsTable = """
                CREATE TABLE IF NOT EXISTS jobs (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    command TEXT NOT NULL
                );
                """;

        String createHistoryTable = """
                CREATE TABLE IF NOT EXISTS history (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    job_id INTEGER NOT NULL,
                    job_name TEXT NOT NULL,
                    command TEXT NOT NULL,
                    status TEXT NOT NULL,
                    exit_code INTEGER NOT NULL,
                    executed_at TEXT NOT NULL
                );
                """;

        try (Connection connection = getConnection();
                Statement statement = connection.createStatement()) {

            statement.execute(createJobsTable);
            statement.execute(createHistoryTable);

        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }
}