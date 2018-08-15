package intern.project.travisalerts;

import org.junit.Test;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;

public class PollingServiceTest {
    @Test
    public void validRequestStMgDevelop()
    {
        String repo = "mdsol/study_management";
        String branch = "develop";
        int pollMin = 1;
        String room = "T2BJH134Y/BC1JWUXUJ/wTCZ5YYFrTbe6D9OQVpKGBQy";

        MainService ms = new MainService(repo, branch, new SlackNotifier(room));
        System.out.println(ms.getAPIStringResponse(repo,branch));
    }
    @Test
    public void validRequestWithSlashDots()
    {
        String repo = "mdsol/phoenix";
        String branch = "release/2018.2.1";
        int pollMin = 1;
        String room = "T2BJH134Y/BC1JWUXUJ/wTCZ5YYFrTbe6D9OQVpKGBQy";

        MainService ms = new MainService(repo, branch, new SlackNotifier(room));
        System.out.println(ms.getAPIStringResponse(repo,branch));
    }
    @Test
    public void validRequestHyphen()
    {
        String repo = "mdsol/ract_ui";
        String branch = "codacy-integration";
        int pollMin = 1;
        String room = "T2BJH134Y/BC1JWUXUJ/wTCZ5YYFrTbe6D9OQVpKGBQy";

        MainService ms = new MainService(repo, branch, new SlackNotifier(room));
        System.out.println(ms.getAPIStringResponse(repo,branch));
    }
}
