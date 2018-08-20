package intern.project.travisalerts;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DataControllerTest {
    @Test
    public void addEntry()
    {
        TravisAlertsApplication.dc.addChannel("channelid", "url");
        assertEquals(TravisAlertsApplication.dc.getChannelURL("channelid"), "url");

    }
}
