import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandExecutor
{
	public void execute(String command)
	{
		try
		{
			ProcessBuilder builder = new ProcessBuilder("bash", "-c", command);
			Process process = builder.start();

			BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));		

			BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;

			System.out.println("----- Command Output -----");
			while ((line = outputReader.readLine()) != null)
			{
				System.out.println(line);
			}

			System.out.println("----- Command Errors -----");
			while ((line = errorReader.readLine()) != null)
			{
				System.out.println(line);
			}

			int exitCode = process.waitFor();
			System.out.println("Exit Code: " + exitCode);
		}
		catch (IOException e)
		{
			System.out.println("Error running command: " + e.getMessage());

		}
		catch (InterruptedException e)
		{
			System.out.println("Command was interruped.");
			Thread.currentThread().interrupt();
		}
	}
}
