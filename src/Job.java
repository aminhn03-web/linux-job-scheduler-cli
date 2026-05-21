/**
 * Represents a scheduled job in the Linux Job Scheduler CLI.
 *
 * A job contains:
 * -  an ID used to identify the job
 * - a readable name for the job
 * -the Linux command that should be executed
 */
public class Job
{
    	private int id;
	private String name;
    	private String command;

	/**
	  * Creates a new job object.
	  * @param id unique job ID
	  * @param name readable job name
	  * @param command Linux shell command associated with the job
	  */
    	public Job(int id, String name, String command)
	{
        	this.id = id;
        	this.name = name;
        	this.command = command;
    	}

	/**
	  * @return the job ID
	  */
    	public int getId()
	{
        	return id;
    	}

	/**
	  * @return the job name
	  */
    	public String getName()
	{
        	return name;
    	}

	/**
	  * @return the Linux command for this job
	  */
    	public String getCommand()
	{
        	return command;
    	}

	/**
	  * Converts the job site into a single line that can be saved to a text file
	  * Format:
	  * id|name|command
          *
	  * @return formatted job string for file storage
	  */
    	public String toFileString()
	{
        	return id + "|" + name + "|" + command;
    	}

	/**
  	  * Creates a Job object from a line stored in the jobs.txt file.
	  *
	  * @param line saved job line nin id|name|command format
   	  * @return Job object created from the saved line
	  */
    	public static Job fromFileString(String line)
	{
        	String[] parts = line.split("\\|", 3);

        	int id = Integer.parseInt(parts[0]);
        	String name = parts[1];
        	String command = parts[2];

        	return new Job(id, name, command);
    	}

	/**
	  * Returns a readable string representation of the job.
	  *
	  * @return formatted job display text
	  */
    	@Override
    	public String toString()
	{
        	return id + ". " + name + " -> " + command;
    	}
}
