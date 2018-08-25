package intern.project.travisalerts;

import org.junit.Assert;
import org.junit.Test;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.Channel;

import static org.junit.Assert.assertEquals;

public class DataControllerTest {
    @Test
    public void addEntry()
    {
        TravisAlertsApplication.dc.addChannel("channelid", "url");
        assertEquals(TravisAlertsApplication.dc.getChannelURL("channelid"), "url");
    }
    @Test
    public void loadFromDisk()
    {

        DataController dc = new DataController();

        dc.addChannel("channelid", "url");
        assertEquals(dc.getChannelURL("channelid"), "url");

        String line;

        try {
            FileReader fr = new FileReader("channelDB.txt");
            BufferedReader br = new BufferedReader(fr);
            line = br.readLine();//continues to read through the whole file
            String[] channelElements = line.split(","); //split the line by comma instances
            System.out.println("XXX" + channelElements[0]);


            for (ChannelRecord channel: dc.channelData)
            {
                assert (channel.id.contains(channelElements[0]));
            }
        }
        catch (IOException e) {}

        dc = new DataController();
        for (ChannelRecord channel: dc.channelData)
        {
            assert(channel.id.contains("channelid"));
        }

    }
    //Testing RecordPolling read/write functionality
    @Test
    public void canWritePollingRecordsToFileOK()
    {
        DataController dc = new DataController();

        dc.createPollingRecord("mdsol/somethingelse", "develop", "someChannelID", 3, true, new SlackNotifier("http://someReasonableURL.com"));

        //Wipe the DC and make it read from file.
        dc = new DataController();
        boolean found = false;

        for (PollingRecord record : dc.pollingData)
            {
                if (record.repo.contains("mdsol/somethingelse"))
                {
                    found = true;
                }
            }
        assert(found);

    }
}
