import java.util.List;

/**
 * Entry point for the Linux Job Scheduler CLI.
 *
 * This class handles command-line arguments and routes commands
 * to the correct part of the application.
 */
public class Main {
	public static void main(String[] args) {
		DatabaseManager.initializeDatabase();

		JobRepository repository = new JobRepository();
		HistoryRepository historyRepository = new HistoryRepository();

		if (args.length == 0) {
			printHelp();
			return;
		}

		String command = args[0].toLowerCase();

		switch (command) {
			case "help":
				printHelp();
				break;

			case "run":
				runImmediateCommand(args);
				break;

			case "add":
				addJob(args, repository);
				break;

			case "list":
				listJobs(repository);
				break;

			case "run-job":
				runSavedJob(args, repository, historyRepository);
				break;

			case "schedule":
				scheduleJob(args, repository, historyRepository);
				break;

			case "history":
				historyRepository.printHistory();
				break;

			case "update":
				updateJob(args, repository);
				break;

			case "delete":
				deleteJob(args, repository);
				break;

			default:
				AppLogger.error("Unknown command: " + command);
				printHelp();
		}
	}

	/**
	 * Runs a Linux command immediately without saving it as a job.
	 *
	 * Example:
	 * ./run.sh run "ls -la"
	 *
	 * @param args command-line arguments
	 */
	private static void runImmediateCommand(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage: ./run.sh run \"<linux-command>\"");
			return;
		}

		String linuxCommand = args[1];
		CommandExecutor executor = new CommandExecutor();
		executor.execute(linuxCommand);
	}

	/**
	 * Saves a new job with a name and Linux command.
	 *
	 * Example:
	 * ./run.sh add "List Files" "ls -la"
	 *
	 * @param args       command-line arguments
	 * @param repository repository used to save jobs
	 */
	private static void addJob(String[] args, JobRepository repository) {
		if (args.length < 3) {
			System.out.println("Usage: ./run.sh add \"<job-name>\" \"<linux-command>\"");
			return;
		}

		String jobName = args[1];
		String jobCommand = args[2];

		repository.addJob(jobName, jobCommand);
	}

	/**
	 * Displays all saved jobs.
	 *
	 * @param repository repository used to retrieve jobs
	 */
	private static void listJobs(JobRepository repository) {
		List<Job> jobs = repository.getAllJobs();

		if (jobs.isEmpty()) {
			AppLogger.warning("No jobs found.");
			return;
		}

		System.out.println("Saved Jobs:");
		for (Job job : jobs) {
			System.out.println(job);
		}
	}

	/**
	 * Runs a saved job by its ID and saves the execution result to history.
	 *
	 * Example:
	 * ./run.sh run-job 1
	 *
	 * @param args              command-line arguments
	 * @param repository        repository used to retrieve saved jobs
	 * @param historyRepository repository used to save execution history
	 */
	private static void runSavedJob(
			String[] args,
			JobRepository repository,
			HistoryRepository historyRepository) {
		if (args.length < 2) {
			System.out.println("Usage: ./run.sh run-job <job-id>");
			return;
		}

		int jobId;

		try {
			jobId = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			AppLogger.error("Invalid job ID. Please enter a number.");
			return;
		}

		Job job = repository.getJobById(jobId);

		if (job == null) {
			AppLogger.warning("No job found with ID: " + jobId);
			return;
		}

		System.out.println("Running job: " + job);

		CommandExecutor executor = new CommandExecutor();
		int exitCode = executor.execute(job.getCommand());

		historyRepository.saveExecution(job, exitCode);
		AppLogger.success("Execution saved to history.");
	}

	/**
	 * Updates a saved job by ID.
	 *
	 * Expected command format:
	 * ./run.sh update <job-id> "<new-job-name>" "<new-linux-command>"
	 *
	 * Example:
	 * ./run.sh update 1 "Show Directory" "pwd"
	 *
	 * @param args       command-line arguments entered by the user
	 * @param repository repository used to update the saved job
	 */
	private static void updateJob(String[] args, JobRepository repository) {
		if (args.length < 4) {
			System.out.println("Usage: ./run.sh update <job-id> \"<new-job-name>\" \"<new-linux-command>\"");
			return;
		}

		int jobId;

		try {
			jobId = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			AppLogger.error("Invalid job ID. Please enter a number.");
			return;
		}

		String newName = args[2];
		String newCommand = args[3];

		boolean updated = repository.updateJob(jobId, newName, newCommand);

		if (updated) {
			AppLogger.success("Job updated successfully.");
		} else {
			AppLogger.warning("No job found with ID: " + jobId);
		}
	}

	/**
	 * Deletes a saved job by ID.
	 *
	 * Expected command format:
	 * ./run.sh delete <job-id>
	 *
	 * Example:
	 * ./run.sh delete 1
	 *
	 * @param args       command-line arguments entered by the user
	 * @param repository repository used to delete the saved job
	 */
	private static void deleteJob(String[] args, JobRepository repository) {
		if (args.length < 2) {
			System.out.println("Usage: ./run.sh delete <job-id>");
			return;
		}

		int jobId;

		try {
			jobId = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			AppLogger.error("Invalid job ID. Please enter a number.");
			return;
		}

		boolean deleted = repository.deleteJob(jobId);

		if (deleted) {
			AppLogger.success("Job deleted successfully.");
		} else {
			AppLogger.warning("No job found with ID: " + jobId);
		}
	}

	/**
	 * Schedules a saved job to run repeatedly at a fixed interval.
	 *
	 * Expected command format:
	 * ./run.sh schedule <job-id> <interval-seconds>
	 *
	 * Example:
	 * ./run.sh schedule 1 60
	 *
	 * @param args              command-line arguments entered by the user
	 * @param repository        repository used to find saved jobs
	 * @param historyRepository repository used to save execution history
	 */
	private static void scheduleJob(
			String[] args,
			JobRepository repository,
			HistoryRepository historyRepository) {
		if (args.length < 3) {
			System.out.println("Usage: ./run.sh schedule <job-id> <interval-seconds>");
			return;
		}

		int jobId;
		int intervalSeconds;

		try {
			jobId = Integer.parseInt(args[1]);
			intervalSeconds = Integer.parseInt(args[2]);
		} catch (NumberFormatException e) {
			AppLogger.error("Invalid input. Job ID and interval must both be numbers.");
			return;
		}

		Job job = repository.getJobById(jobId);

		if (job == null) {
			AppLogger.warning("No job found with ID: " + jobId);
			return;
		}

		CommandExecutor commandExecutor = new CommandExecutor();
		SchedulerService schedulerService = new SchedulerService(commandExecutor, historyRepository);

		schedulerService.scheduleJob(job, intervalSeconds);
	}

	/**
	 * Prints available CLI commands and examples.
	 */
	private static void printHelp() {
		System.out.println("Linux Job Scheduler CLI");
		System.out.println();
		System.out.println("Commands:");
		System.out.println("  help\t\t\t\t\t\t\t\tShow available commands");
		System.out.println("  run \"<linux-command>\"\t\t\t\t\t\tRun a Linux command immediately");
		System.out.println("  add \"<job-name>\" \"<linux-command>\"\t\t\t\tSave a job");
		System.out.println("  list\t\t\t\t\t\t\t\tList saved jobs");
		System.out.println("  run-job <job-id>\t\t\t\t\t\tRun a saved job by ID");
		System.out.println("  history\t\t\t\t\t\t\tShow execution history");
		System.out.println("  update <job-id> \"<new-job-name>\" \"<new-linux-command>\"\tUpdate a saved job");
		System.out.println("  delete <job-id>\t\t\t\t\t\tDelete a saved job");
		System.out.println("  schedule <job-id> <interval-seconds>  Run a saved job repeatedly");
		System.out.println();
		System.out.println("Examples:");
		System.out.println("  ./run.sh help");
		System.out.println("  ./run.sh run \"ls -la\"");
		System.out.println("  ./run.sh add \"List Files\" \"ls -la\"");
		System.out.println("  ./run.sh list");
		System.out.println("  ./run.sh run-job 1");
		System.out.println("  ./run.sh history");
		System.out.println("  ./run.sh update 1 \"Show Directory\" \"pwd\"");
		System.out.println("  ./run.sh delete 1");
		System.out.println("  ./run.sh schedule 1 60");
	}
}
