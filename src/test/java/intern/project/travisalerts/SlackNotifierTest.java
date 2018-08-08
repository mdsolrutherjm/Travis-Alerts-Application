package intern.project.travisalerts;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SlackNotifierTest {

    SlackNotifier sf = new SlackNotifier("T2BJH134Y/BC1JWUXUJ/wTCZ5YYFrTbe6D9OQVpKGBQy");
    @Test
    public void sendsMessage()
    {
        sf.sendText(LocalDateTime.now().toString());

    }
    @Test
    public void sendsFailed()
    {
        sf.sendFailed(80, "mdsol/studymanagement", "feature/MCC-374748_sample_branch", "James Rutherford", "2 min 46 sec", "http://www.google.co.uk");

    }
    @Test
    public void sendsPassed()
    {
        sf.sendPassed(80, "mdsol/studymanagement", "feature/MCC-374748_sample_branch", "James Rutherford", "2 min 46 sec", "http://www.google.co.uk");

    }
}