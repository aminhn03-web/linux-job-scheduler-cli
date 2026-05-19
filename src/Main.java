public class Main 
{
	public static void main(String[] args) 
	{
        	// If the user did not type any command, show the help menu and stop
		if (args.length == 0)
		{
            		printHelp();
            		return;
        	}

		// Get the first command-line argument and convert it to lwoercase
        	String command = args[0].toLowerCase();

		// Decide whwat to do based on the command the user entered
        	switch (command)
		{
	    		// Display  the help menu
            		case "help":
                		printHelp();
                		break;
	    		// Execute a Linux command
            		case "run":
				// Ensure the user provided a Linux command after "run"
				// Example: ./run.sh run "ls -la"
                		if (args.length < 2)
				{
                    			System.out.println("Usage: ./run.sh run \"<linux-command>\"");
                    			return;
                		}

				// Store the Linux command the user wants to run
                		String linuxCommand = args[1];

				// Create a COmmandExecutor object to handle running the command
                		CommandExecutor executor = new CommandExecutor();

				// Execute the Linux command
                		executor.execute(linuxCommand);
                		break;
			// If command is not recognized, show an error and the help menu
            		default:
                		System.out.println("Unknown command: " + command);
                		printHelp();
        	}
    }
	// Prints the help menu showing availablae commands and examples
	private static void printHelp()
	{
        	System.out.println("Linux Job Scheduler CLI");
        	System.out.println();
        	System.out.println("Commands:");
        	System.out.println("  help\t\t\t\tShow available commands");
        	System.out.println("  run \"<linux-command>\"\t\tRun a Linux command immediately");
        	System.out.println();
        	System.out.println("Examples:");
        	System.out.println("  ./run.sh help");
        	System.out.println("  ./run.sh run \"ls -la\"");
        	System.out.println("  ./run.sh run \"pwd\"");
    	}
}

