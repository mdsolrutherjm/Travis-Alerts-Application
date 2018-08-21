package intern.project.travisalerts;

public class DataController {
    int size = 0;
    String[][] channelData = new String[10][10];

    /**
     * this method is called when our program starts.
     * it should load the data from the text file into the variables.
     */
    public DataController()
    {
        //Load data from file
    }
    private String[][] returnChannels()
    {
        return channelData;
    }

    /**
     * Adds a new channel ID and URL to the data storage
     * @param name  ID of the channel.
     * @param url URL of the channel.
     */
    public void addChannel(String name, String url)
    {
        System.out.println("NEW CHANNEL: " + name + " " + url);
        channelData[size][0] = name;
        channelData[size][1] = url;
        size++;

        //Save data to file

    }

    /**
     * Traverses through the URL array and finds the requested channel URL based on a matching channel ID.
     * @param name the channel ID to find the relevant URL for .
     * @return the channel URL found (or null if the URL does not exist. )
     */
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
