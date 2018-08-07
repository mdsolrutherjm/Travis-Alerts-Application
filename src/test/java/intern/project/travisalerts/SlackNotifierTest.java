package intern.project.travisalerts;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SlackNotifierTest {

    SlackNotifier sf = new SlackNotifier();
    @Test
    public void sendsMessage()
    {
        sf.sendText("Send generic message text. ");

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