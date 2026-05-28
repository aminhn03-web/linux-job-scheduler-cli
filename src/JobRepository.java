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
			AppLogger.error("Error reading jobs from database: " + e.getMessage());
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

			AppLogger.success("Job added: " + name + " -> " + command);

		} catch (SQLException e) {
			AppLogger.error("Error saving job to database: " + e.getMessage());
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
			AppLogger.error("Error finding job: " + e.getMessage());
		}

		return null;
	}

	/**
	 * Updates an existing saved job in the SQLite database.
	 *
	 * This method changes both the job name and Linux command for the
	 * job matching the provided ID.
	 *
	 * @param id         ID of the job to update
	 * @param newName    updated readable job name
	 * @param newCommand updated Linux shell command
	 * @return true if a job was updated, false if no matching job was found
	 */
	public boolean updateJob(int id, String newName, String newCommand) {
		String sql = "UPDATE jobs SET name = ?, command = ? WHERE id = ?";

		try (Connection connection = DatabaseManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, newName);
			statement.setString(2, newCommand);
			statement.setInt(3, id);

			int rowsUpdated = statement.executeUpdate();
			return rowsUpdated > 0;

		} catch (SQLException e) {
			AppLogger.error("Error updating job: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Deletes a saved job from the SQLite database by ID.
	 *
	 * This removes the job from the jobs table. Existing execution history
	 * remains stored so past runs can still be reviewed.
	 *
	 * @param id ID of the job to delete
	 * @return true if a job was deleted, false if no matching job was found
	 */
	public boolean deleteJob(int id) {
		String sql = "DELETE FROM jobs WHERE id = ?";

		try (Connection connection = DatabaseManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, id);

			int rowsDeleted = statement.executeUpdate();
			return rowsDeleted > 0;

		} catch (SQLException e) {
			AppLogger.error("Error deleting job: " + e.getMessage());
			return false;
		}
	}
}