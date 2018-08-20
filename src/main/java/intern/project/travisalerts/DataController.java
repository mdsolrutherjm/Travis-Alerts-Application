package intern.project.travisalerts;

import java.util.ArrayList;

public class DataController {
    int size = 0;
    String[][] urls = new String[10][10];
    private String[][] returnChannels()
    {
        return urls;
    }
    public void addChannel(String name, String url)
    {

        urls[size][0] =name;
        urls[size][1] = url;
        size++;
    }
    public String getChannelURL(String name)
    {
        for (int i = 0; i < size; i++)
        {
            if (urls[i][0].equals(name))
            {
                return urls[i][1];
            }
        }
        return null;
    }
}
