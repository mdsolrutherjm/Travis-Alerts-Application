package intern.project.travisalerts;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SlackNotifierTest {
    static Map<String, String> env = System.getenv();
    SlackNotifier sf = new SlackNotifier( env.get("SLACK_DEBUG_CHANNEL"));
    @Test
    public void sendsMessage()
    {
        sf.sendText(ConstantUtils.FIRST_TIME_CONFIG_RESPONSE);

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