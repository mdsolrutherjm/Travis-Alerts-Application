package intern.project.travisalerts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TravisAlertsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravisAlertsApplication.class, args);

	Thread pollingSvc = new Thread(new MainService("3521753", "develop", 30));
		pollingSvc.start();

	}
}
