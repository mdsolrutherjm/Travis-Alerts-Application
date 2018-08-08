package intern.project.travisalerts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TravisAlertsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravisAlertsApplication.class, args);

	Thread pollingSvc = new Thread(new MainService("3521753", "develop", 1, "T2BJH134Y/BC1JWUXUJ/wTCZ5YYFrTbe6D9OQVpKGBQy"));
		pollingSvc.start();

		Thread oneOffSvc = new Thread(new MainService("3521753", "develop", "T2BJH134Y/BC1JWUXUJ/wTCZ5YYFrTbe6D9OQVpKGBQy"));
		oneOffSvc.start();
	}
}
