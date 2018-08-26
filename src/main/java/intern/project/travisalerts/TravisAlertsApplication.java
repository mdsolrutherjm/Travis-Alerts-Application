package intern.project.travisalerts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Map;
import java.util.Scanner;

@SpringBootApplication
public class TravisAlertsApplication {
    public static DataController dc = new DataController(); //Load in any saved-state configuration.
    static boolean programState = true;

	public static void main(String[] args) {
		SpringApplication.run(TravisAlertsApplication.class, args);

        Scanner inputListener = new Scanner(System.in);
        String arg0 = "";


        //Run a health check to ensure that all of the environment variables have been loaded correctly.
        if (areAnyEnvironmentVariablesMissing())
        {
            System.out.println(ConstantUtils.MISSING_ENV_VARIABLE);
            System.exit(1);
        }


        //Create new threads for the loaded PollingRecords.
        for (PollingRecord record: dc.getPollingRecords())
        {
            new Thread(new MainService(record)).start(); //Create a new polling service for each record.
        }

        //start accepting terminal text-based commands.
		while (programState)
        {
            System.out.print("Travis Alerts > ");
            arg0 = inputListener.next();
            switch(arg0)
            {
                case "exit":
                    System.exit(0);
                    break;
                case "help":
                    help();
                    break;
            }
        }
	}
	public static void help()
    {
        System.out.println("Commands: \nExit        Terminates Travis Alerts safely. ");
    }

    /**
     * Checks that all the required Environment Variables are loaded.
     */
    public static boolean areAnyEnvironmentVariablesMissing()
    {
         Map<String, String> env = System.getenv();

        if ((env.get(ConstantUtils.ENV_TRAVIS_TOKEN) == null) || (env.get(ConstantUtils.ENV_CLIENT_ID) == null) || (env.get(ConstantUtils.ENV_CLIENT_SECRET) == null))
        {
            return true;
        }
        return false;
    }
}