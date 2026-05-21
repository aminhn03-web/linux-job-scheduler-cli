import java.io.*;
import java.util.*;

/**
 * Handles saving and loading Job objects from local file storage.
 *
 * For now, jobs are stored in a simple text file called jobs.txt.
 * Later, this class can be upgraded to use SQLite for persistent database storage.
 */
public class JobRepository
{
	private static final String JOB_FILE = "jobs.txt";

    	/**
     	 * Reads all saved jobs from jobs.txt.
     	 *
     	 * If the file does not exist yet, an empty list is returned.
     	 *
     	 * @return list of saved jobs
     	 */
	public List<Job> getAllJobs()
	{
        	List<Job> jobs = new ArrayList<>();

        	File file = new File(JOB_FILE);

        	if (!file.exists())
		{
            		return jobs;
        	}

        	try (BufferedReader reader = new BufferedReader(new FileReader(file)))
		{
            		String line;

            		while ((line = reader.readLine()) != null)
			{
                		if (!line.trim().isEmpty())
				{
                    			jobs.add(Job.fromFileString(line));
                		}
            		}

        	}
		catch (IOException e)
		{
            		System.out.println("Error reading jobs: " + e.getMessage());
        	}

        	return jobs;
    	}

    	/**
     	 * Adds a new job to jobs.txt.
     	 *
     	 * The job receives the next available ID based on existing saved jobs.
     	 *
     	 * @param name readable job name
     	 * @param command Linux shell command to save
     	 */
    	public void addJob(String name, String command)
	{
        	List<Job> jobs = getAllJobs();
        	int nextId = getNextId(jobs);

        	Job job = new Job(nextId, name, command);

        	try (FileWriter writer = new FileWriter(JOB_FILE, true))
		{
            		writer.write(job.toFileString() + System.lineSeparator());
            		System.out.println("Job added: " + job);

        	}
		catch (IOException e)
		{
            		System.out.println("Error saving job: " + e.getMessage());
        	}
    	}

    	/**
     	 * Finds the next available job ID.
     	 *
     	 * @param jobs existing saved jobs
     	 * @return next available ID
     	 */
	private int getNextId(List<Job> jobs) {
        	int maxId = 0;

        	for (Job job : jobs)
		{
            		if (job.getId() > maxId)
			{
                		maxId = job.getId();
            		}
        	}

        	return maxId + 1;
    	}
}
