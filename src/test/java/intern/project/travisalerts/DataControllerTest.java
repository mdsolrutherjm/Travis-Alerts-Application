package intern.project.travisalerts;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
}
