import java.util.List;

/**
 * Entry point for the Linux Job Scheduler CLI.
 *
 * This class handles command-line arguments and routes commands
 * to the correct part of the application.
 */
public class Main {
	public static void main(String[] args) {
		JobRepository repository = new JobRepository();

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
				runSavedJob(args, repository);
				break;

			default:
				System.out.println("Unknown command: " + command);
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
			System.out.println("No jobs found.");
			return;
		}

		System.out.println("Saved Jobs:");
		for (Job job : jobs) {
			System.out.println(job);
		}
	}

	    /**
     * Runs a saved job by its ID.
     *
     * Example:
     * ./run.sh run-job 1
     *
     * @param args command-line arguments
     * @param repository repository used to retrieve saved jobs
     */
    private static void runSavedJob(String[] args, JobRepository repository) {
        if (args.length < 2) {
            System.out.println("Usage: ./run.sh run-job <job-id>");
            return;
        }

        int jobId;

        try {
            jobId = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid job ID. Please enter a number.");
            return;
        }

        Job job = repository.getJobById(jobId);

        if (job == null) {
            System.out.println("No job found with ID: " + jobId);
            return;
        }

        System.out.println("Running job: " + job);
        CommandExecutor executor = new CommandExecutor();
        executor.execute(job.getCommand());
    }

	/**
	 * Prints available CLI commands and examples.
	 */
	private static void printHelp() {
		System.out.println("Linux Job Scheduler CLI");
		System.out.println();
		System.out.println("Commands:");
		System.out.println("  help\t\t\t\t\tShow available commands");
		System.out.println("  run \"<linux-command>\"\t\t\tRun a Linux command immediately");
		System.out.println("  add \"<job-name>\" \"<linux-command>\"\tSave a job");
		System.out.println("  list\t\t\t\t\tList saved jobs");
		System.out.println("  run-job <job-id>\t\t\tRun a saved job by ID");
		System.out.println();
		System.out.println("Examples:");
		System.out.println("  ./run.sh help");
		System.out.println("  ./run.sh run \"ls -la\"");
		System.out.println("  ./run.sh add \"List Files\" \"ls -la\"");
		System.out.println("  ./run.sh list");
		System.out.println("  ./run.sh run-job 1");
	}
}
