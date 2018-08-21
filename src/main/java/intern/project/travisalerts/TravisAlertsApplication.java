package intern.project.travisalerts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.HttpClientErrorException;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Scanner;

@SpringBootApplication
public class TravisAlertsApplication {
    public static DataController dc = new DataController();
    //AUTHORIZATION
    static Map<String, String> env = System.getenv();
    private static String TRAVIS_AUTH_TOKEN = env.get("TRAVIS_TOKEN");
    static boolean programState = true;
	public static void main(String[] args) {
		SpringApplication.run(TravisAlertsApplication.class, args);

        Scanner inputListener = new Scanner(System.in);
        String arg0,arg1,arg2,arg3 = "";

        loadPolling();

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
                case "rawJson":
                    arg1 = inputListener.next(); //repo
                    arg2 = inputListener.next(); //branch
                    try
                    {
                        MainService ms = new MainService(arg1, arg2, new SlackNotifier("https://hooks.slack.com/services/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"));
                        System.out.println(ms.getAPIStringResponse(arg1, arg2));

                    }
                    catch (HttpClientErrorException|UnsupportedEncodingException e)
                    {
                        System.out.println(e);
                    }
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

	public static void loadPolling() {
        System.out.println("Loading configuration");

        //Read file
        //Extract information
        //Start new threads accordingly
        //creating a every-1-min poll for study_management develop branch.
    }
}
