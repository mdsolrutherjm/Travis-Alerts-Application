package intern.project.travisalerts;

import junit.framework.AssertionFailedError;
import org.junit.Assert;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import javax.validation.constraints.AssertFalse;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

public class PollingServiceTest {
    @Test
    public void validRequestStMgDevelop()
    {
        String repo = "mdsol/study_management";
        String branch = "feature/MCC-415815_mock_actual_contract_correction";
        int pollMin = 1;
        String room =  "https://hooks.slack.com/services/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";

        MainService ms = new MainService(new PollingRecord(repo,branch,"fake", pollMin, false,new SlackNotifier(room)));
        try
        {
            System.out.println(ms.getAPIStringResponse(repo,branch));
        }
        catch(HttpClientErrorException | UnsupportedEncodingException | ResourceAccessException e)
        {
            Assert.fail(e.toString());
        }
    }
    @Test
    public void validRequestWithSlashDots()
    {
        String repo = "mdsol/phoenix";
        String branch = "release/2018.2.1";
        int pollMin = 1;
        String room =  "https://hooks.slack.com/services/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";

        MainService ms = new MainService(new PollingRecord(repo,branch,"fake", pollMin, false,new SlackNotifier(room)));
        try
        {
            System.out.println(ms.getAPIStringResponse(repo,branch));
        }
        catch(HttpClientErrorException | UnsupportedEncodingException | ResourceAccessException e)
        {
            Assert.fail(e.toString());
        }
    }
    @Test
    public void validRequestHyphen()
    {
        String repo = "mdsol/ract_ui";
        String branch = "codacy-integration";
        int pollMin = 1;
        String room = "https://hooks.slack.com/services/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";

        MainService ms = new MainService(new PollingRecord(repo,branch,"fake", pollMin, false,new SlackNotifier(room)));

        try
        {
            System.out.println(ms.getAPIStringResponse(repo,branch));
        }
        catch(HttpClientErrorException | UnsupportedEncodingException | ResourceAccessException e)
        {
            Assert.fail(e.toString());
        }
    }
}
