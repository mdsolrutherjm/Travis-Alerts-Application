package intern.project.travisalerts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TravisAlertsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravisAlertsApplication.class, args);


		loadPolling();
		//creating a every-1-min poll for study_management develop branch.
        String repo = "3521753";
        String branch = "develop";
        int pollMin = 1;
        String room = "T2BJH134Y/BC1JWUXUJ/wTCZ5YYFrTbe6D9OQVpKGBQy";


Thread pollingSvc = new Thread(new MainService(repo, branch, pollMin, new SlackNotifier(room)));
		pollingSvc.start();
	}

	public static void loadPolling() {
        //Read file
        //Extract information
        //Start new threads accordingly
    }
}
