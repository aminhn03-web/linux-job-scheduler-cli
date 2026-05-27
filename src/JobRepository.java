import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles database operations for saved jobs.
 *
 * This class uses SQLite to add, retrieve, and search jobs.
 */
public class JobRepository {

	/**
	 * Reads all saved jobs from the SQLite database.
	 *
	 * @return list of saved jobs
	 */
	public List<Job> getAllJobs() {
		List<Job> jobs = new ArrayList<>();

		String sql = "SELECT id, name, command FROM jobs ORDER BY id";

		try (Connection connection = DatabaseManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {
				Job job = new Job(
						resultSet.getInt("id"),
						resultSet.getString("name"),
						resultSet.getString("command"));

				jobs.add(job);
			}

		} catch (SQLException e) {
			System.out.println("Error reading jobs from database: " + e.getMessage());
		}

		return jobs;
	}

	/**
	 * Adds a new job to the SQLite database.
	 *
	 * @param name    readable job name
	 * @param command Linux shell command to save
	 */
	public void addJob(String name, String command) {
		String sql = "INSERT INTO jobs (name, command) VALUES (?, ?)";

		try (Connection connection = DatabaseManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, name);
			statement.setString(2, command);
			statement.executeUpdate();

			System.out.println("Job added: " + name + " -> " + command);

		} catch (SQLException e) {
			System.out.println("Error saving job to database: " + e.getMessage());
		}
	}

	/**
	 * Finds one saved job by ID.
	 *
	 * @param id job ID to search for
	 * @return matching Job object, or null if no job exists
	 */
	public Job getJobById(int id) {
		String sql = "SELECT id, name, command FROM jobs WHERE id = ?";

		try (Connection connection = DatabaseManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, id);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return new Job(
							resultSet.getInt("id"),
							resultSet.getString("name"),
							resultSet.getString("command"));
				}
			}

		} catch (SQLException e) {
			System.out.println("Error finding job: " + e.getMessage());
		}

		return null;
	}
}