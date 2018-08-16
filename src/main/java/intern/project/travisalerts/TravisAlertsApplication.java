package intern.project.travisalerts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class TravisAlertsApplication {
    static boolean programState = true;
	public static void main(String[] args) {
		SpringApplication.run(TravisAlertsApplication.class, args);

        Scanner inputListener = new Scanner(System.in);
        String input = "";
        loadPolling();

		while (programState)
        {
            System.out.print("Travis Alerts > ");
            input = inputListener.next();
            switch(input)
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
        System.out.println("Commands: \n");
    }
	public static void loadPolling() {
        System.out.println("Loading configuration");

        //Read file
        //Extract information
        //Start new threads accordingly
        //creating a every-1-min poll for study_management develop branch.
        String repo = "3521753";
        String branch = "develop";
        int pollMin = 30;
        String room = "T2BJH134Y/BC1JWUXUJ/wTCZ5YYFrTbe6D9OQVpKGBQy";


        Thread pollingSvc = new Thread(new MainService(repo, branch, pollMin, new SlackNotifier(room)));
        pollingSvc.start();
    }
}
