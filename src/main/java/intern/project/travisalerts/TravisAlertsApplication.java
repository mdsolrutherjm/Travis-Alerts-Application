package intern.project.travisalerts;

import org.apache.tomcat.jni.Poll;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.HttpClientErrorException;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Scanner;

@SpringBootApplication
public class TravisAlertsApplication {
    public static DataController dc = new DataController(); //Load in any saved-state configuration.
    //AUTHORIZATION
    static Map<String, String> env = System.getenv();
    private static String TRAVIS_AUTH_TOKEN = env.get("TRAVIS_TOKEN");
    static boolean programState = true;
	public static void main(String[] args) {
		SpringApplication.run(TravisAlertsApplication.class, args);

        Scanner inputListener = new Scanner(System.in);
        String arg0,arg1,arg2,arg3 = "";


        //Run a health check to ensure that all of the environment variables have been loaded correctly.
        healthCheck(); //Check all env variables are present.


        //Create new threads for the loaded PollingRecords.
        for (PollingRecord record: dc.getPollingRecords())
        {
            new Thread(new MainService(record)).start(); //Create a new polling service for each record.
        }

        //start accepting user commands.
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
                case "system":
                    System.out.println("Using Travis Token " + TRAVIS_AUTH_TOKEN);
                    break;
                case "slack":
                    arg1 = inputListener.next(); //slack link
                    arg2 = inputListener.next(); //text
                    SlackNotifier sn = new SlackNotifier(arg1);
                    sn.sendText(arg2);
                    break;
                case "addbranch":
                    arg1 = inputListener.next(); //repo
                    arg2 = inputListener.next(); //branch
                    System.out.println("Slack Channel Link > ");
                    arg3 = inputListener.next();

                    break;
                case "deletebranch":
                    arg1 = inputListener.next(); //repo
                    arg2 = inputListener.next(); //branch
                    break;


            }
        }


	}
	public static void help()
    {
        System.out.println("Commands: \n");
    }

    /**
     * Checks that all the required Environment Variables are loaded.
     */
    public static void healthCheck()
    {
        if ((env.get("TRAVIS_TOKEN") == null) || (env.get(ConstantUtils.ENV_CLIENT_ID) == null) || (env.get(ConstantUtils.ENV_CLIENT_SECRET) == null))
        {
            System.out.println(ConstantUtils.MISSING_ENV_VARIABLE);
            System.exit(1);
        }
    }
}
