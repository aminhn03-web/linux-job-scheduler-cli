import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Handles recurring execution of saved jobs using Java multithreading.
 *
 * SchedulerService uses ScheduledExecutorService to run jobs on a background
 * worker thread at a fixed interval. This is a more scalable and professional
 * approach than manually using an infinite loop with Thread.sleep().
 */
public class SchedulerService {
    private final CommandExecutor commandExecutor;
    private final HistoryRepository historyRepository;
    private final ScheduledExecutorService scheduler;

    /**
     * Creates a SchedulerService with the dependencies needed to execute
     * Linux commands and save job execution history.
     *
     * A single-threaded scheduled executor is used so scheduled tasks run
     * on a dedicated background thread.
     *
     * @param commandExecutor   executes Linux shell commands
     * @param historyRepository saves execution results
     */
    public SchedulerService(CommandExecutor commandExecutor, HistoryRepository historyRepository) {
        this.commandExecutor = commandExecutor;
        this.historyRepository = historyRepository;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Schedules a saved job to run repeatedly at a fixed interval.
     *
     * The scheduled job runs on a background scheduler thread. The main thread
     * waits until the user stops the program with Ctrl + C.
     *
     * Example:
     * ./run.sh schedule 3 10
     *
     * @param job             saved job to execute
     * @param intervalSeconds number of seconds between executions
     */
    public void scheduleJob(Job job, int intervalSeconds) {
        if (intervalSeconds <= 0) {
            AppLogger.error("Interval must be greater than 0 seconds.");
            return;
        }

        CountDownLatch keepProgramRunning = new CountDownLatch(1);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println();
            AppLogger.info("Stopping scheduler...");
            shutdown();
            keepProgramRunning.countDown();
        }));

        System.out.println("Scheduling job: " + job);
        System.out.println("Interval: every " + intervalSeconds + " seconds");
        System.out.println("Scheduler is running on a background thread.");
        System.out.println("Press Ctrl + C to stop the scheduler.");
        System.out.println();

        scheduler.scheduleAtFixedRate(
                () -> executeScheduledJob(job),
                0,
                intervalSeconds,
                TimeUnit.SECONDS);

        try {
            keepProgramRunning.await();
        } catch (InterruptedException e) {
            AppLogger.error("Main scheduler thread was interrupted.");
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Executes one scheduled job run and saves the result to history.
     *
     * This method is called repeatedly by the ScheduledExecutorService.
     *
     * @param job saved job to execute
     */
    private void executeScheduledJob(Job job) {
        System.out.println("Running scheduled job on thread: " + Thread.currentThread().getName());
        System.out.println("Job: " + job);

        int exitCode = commandExecutor.execute(job.getCommand());
        historyRepository.saveExecution(job, exitCode);

        AppLogger.success("Scheduled execution saved to history.");
        System.out.println();
    }

    /**
     * Safely shuts down the scheduler service.
     *
     * shutdownNow() is used when the user stops the program so the scheduler
     * does not continue accepting or running new tasks.
     */
    private void shutdown() {
        scheduler.shutdownNow();
    }
}