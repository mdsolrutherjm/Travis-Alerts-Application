package intern.project.travisalerts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Scanner;

@SpringBootApplication
public class TravisAlertsApplication {

    //The DataController manages all of the records in relation to the channels and the repo/branches to poll
    public static DataController dc = new DataController();
    //The SystemConfiguration holds API tokens required for this application to interact with Slack and Travis-CI.
    public static SystemConfiguration config = new SystemConfiguration();

    //Program state is to keep a while-loop alive for the terminal-based commands.
    static boolean programState = true;

	public static void main(String[] args) {
		SpringApplication.run(TravisAlertsApplication.class, args);
		System.out.println(ConstantUtils.STARTING_LOGO);


        //Run a health check to ensure that all of the environment variables have been loaded correctly.
        if (config.areAnyEnvironmentVariablesMissing())
        {
            System.out.println(ConstantUtils.MISSING_ENV_VARIABLE);
            //System is missing data required to communicate to Slack/Travis so terminate now.
            System.exit(1);
        }


        //Create new threads for the loaded PollingRecords.
        for (PollingRecord record: dc.getPollingRecords())
        {
            new Thread(new MainService(record)).start(); //Create a new polling service for each record.
        }

        //Accept user input now.
        Scanner inputListener = new Scanner(System.in);
        //'arg0' is the command that the user types into the terminal.
        String arg0;

        //start responding to terminal text-based commands.
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

    /**
     * Prints to terminal a list of the available commands for terminal-based interaction.
     */
	public static void help()
    {
        System.out.println("Commands: \nExit        Terminates Travis Alerts safely. ");
    }


}