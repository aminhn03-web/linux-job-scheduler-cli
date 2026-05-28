/**
 * Handles recurring execution of saved jobs.
 *
 * SchedulerService is responsible for repeatedly running a saved job
 * at a fixed interval. This first version runs in the foreground and
 * can be stopped by pressing Ctrl + C in the terminal.
 */
public class SchedulerService {
    private final CommandExecutor commandExecutor;
    private final HistoryRepository historyRepository;

    /**
     * Creates a SchedulerService with the dependencies needed
     * to execute commands and save job execution history.
     *
     * @param commandExecutor executes Linux shell commands
     * @param historyRepository saves execution results
     */
    public SchedulerService(CommandExecutor commandExecutor, HistoryRepository historyRepository) {
        this.commandExecutor = commandExecutor;
        this.historyRepository = historyRepository;
    }

    /**
     * Runs a saved job repeatedly at a fixed interval.
     *
     * Example:
     * If intervalSeconds is 60, the job runs once every 60 seconds.
     *
     * @param job saved job to execute
     * @param intervalSeconds number of seconds between each execution
     */
    public void scheduleJob(Job job, int intervalSeconds) {
        if (intervalSeconds <= 0) {
            System.out.println("Interval must be greater than 0 seconds.");
            return;
        }

        System.out.println("Scheduling job: " + job);
        System.out.println("Interval: every " + intervalSeconds + " seconds");
        System.out.println("Press Ctrl + C to stop the scheduler.");
        System.out.println();

        while (true) {
            System.out.println("Running scheduled job: " + job);

            int exitCode = commandExecutor.execute(job.getCommand());
            historyRepository.saveExecution(job, exitCode);

            System.out.println("Scheduled execution saved to history.");
            System.out.println("Waiting " + intervalSeconds + " seconds before next run...");
            System.out.println();

            try {
                Thread.sleep(intervalSeconds * 1000L);
            } catch (InterruptedException e) {
                System.out.println("Scheduler was interrupted.");
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}