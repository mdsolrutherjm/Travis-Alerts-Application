package intern.project.travisalerts;

import java.io.*;

public class DataController {
    int size = 0;
    String[][] channelData = new String[10][2]; //2d array with columns channelID and channelURL


    /**
     * this method is called when our program starts.
     * it should load the data from the text file into the variables.
     */
    public DataController()
    {
        /**
         * add functionality to constructor to unpack each line in the text file as a row in channelData, if channelData
         * is empty. If not, do not do this.
          */
        String fileName = "/Users/jgannon/Travis-Alerts-Application/src/main/resources/channelDB.txt"; //get .txt file
        if (channelData[0].length == 0 & channelData[1].length == 0) { //if channelData is empty
            try {
                FileReader fr = new FileReader(fileName);
                BufferedReader br = new BufferedReader(fr); //object to read .txt file
                String line;
                int i =0; //initialize counter
                while ((line = br.readLine()) != null){ //continues to read through the whole file
                    String[] channelElements = line.split(","); //split the line by comma instances
                    channelData[i][0] = channelElements[0];
                    channelData[i][1] = channelElements[1];
                    i++;
                }
            }
            catch (IOException e){}
        }
    }

    private String[][] returnChannels()
    {
        return channelData;
    }

    /**

     * Method called when /configure command received from webpage (i.e. when a channel is configured)
     * Adds new row to channelData - channelID and channelURL
     * @param name
     * @param url

     */
    public void addChannel(String name, String url)
    {
        System.out.println("NEW CHANNEL: " + name + " " + url);
        channelData[size][0] = name;
        channelData[size][1] = url;
        size++;


        /**
         * Extra functionality to add 'name' and 'url' to a new line in the text file, (delimited by commas)
         * Needs to be such that this data in the text file remains even when the server goes down.
         */

        String fileName = "/Users/jgannon/Travis-Alerts-Application/src/main/resources/channelDB.txt";
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            fw = new FileWriter(fileName);
            bw = new BufferedWriter(fw);
            bw.write(name+","+url);
        }
        catch (IOException e) {e.printStackTrace();}

        finally {
            try {
                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();
            } catch (IOException ex) { ex.printStackTrace(); }
        }
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
