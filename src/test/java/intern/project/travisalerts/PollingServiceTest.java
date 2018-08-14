package intern.project.travisalerts;

import org.junit.Test;

import java.time.LocalDateTime;

public class PollingServiceTest {
    @Test
    public void sendValidRequest()
    {
        String repo = "mdsol/study_management";
        String branch = "develop";
        int pollMin = 1;
        String room = "T2BJH134Y/BC1JWUXUJ/wTCZ5YYFrTbe6D9OQVpKGBQy";

        MainService ms = new MainService(repo, branch, new SlackNotifier(room));

        System.out.println(ms.getAPIStringResponse(repo,branch));

    }
}
