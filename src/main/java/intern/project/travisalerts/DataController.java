package intern.project.travisalerts;

public class DataController {
    int size = 0;
    String[][] channelData = new String[10][10];

    public DataController()
    {
        //Load data from file
    }
    private String[][] returnChannels()
    {
        return channelData;
    }
    public void addChannel(String name, String url)
    {
        System.out.println("NEW CHANNEL: " + name + " " + url);
        channelData[size][0] = name;
        channelData[size][1] = url;
        size++;

        //Save data to file
    }
    public String getChannelURL(String name)
    {
        System.out.println("SEARCHING FOR CHANNEL " + name);
        for (int i = 0; i < size; i++)
        {
            if (channelData[i][0].equals(name))
            {
                return channelData[i][1];
            }
        }
        return null;
    }

}
