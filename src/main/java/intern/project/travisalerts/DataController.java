package intern.project.travisalerts;

import org.apache.tomcat.jni.Poll;

import java.io.*;
import java.nio.channels.Channel;
import java.util.ArrayList;

public class DataController {
    int size = 0;
    int pollingSize = 0;
    //String[][] channelData = new String[10][2]; //2d array with columns channelID and channelURL
    //PollingRecord[] pollingData = new PollingRecord[10];

    //ArrayLists
    ArrayList<PollingRecord> pollingData = new ArrayList<PollingRecord>();
    ArrayList<ChannelRecord> channelData = new ArrayList<ChannelRecord>();

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
        String fileName = "channelDB.txt"; //get .txt file
        if (channelData.isEmpty()) { //if channelData is empty
            try {
                FileReader fr = new FileReader(fileName);
                BufferedReader br = new BufferedReader(fr); //object to read .txt file
                String line;
                while ((line = br.readLine()) != null){ //continues to read through the whole file
                    String[] channelElements = line.split(","); //split the line by comma instances
                    channelData.add(new ChannelRecord(channelElements[0], channelElements[1]));
                }
            }
            catch (IOException e){System.out.println("Failed to read file: "+fileName+"/n"+e.toString());}
        }
    }

    /**

     * Method called when /configure command received from webpage (i.e. when a channel is configured)
     * Adds new row to channelData - channelID and channelURL
     * @param name
     * @param url

     */
    public void addChannel(String name, String url)
    {
        channelData.add(new ChannelRecord(name, url));


        /**
         * Extra functionality to add 'name' and 'url' to a new line in the text file, (delimited by commas)
         * Needs to be such that this data in the text file remains even when the server goes down.
         */

        String fileName = "channelDB.txt";
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
         for (ChannelRecord channel: channelData)
         {
             if (channel.id.contains(name))
             {
                 return channel.url;
             }
         }
         return null;
    }

    /**
     * Takes in the parameters to create a new polling record, creates it and returns the address of it.
     * @return the PollingRecord object reference.
     */
    public PollingRecord createPollingRecord(String repo, String branch, String channelID, int poll, boolean active, SlackNotifier sn)
    {
        PollingRecord pr = new PollingRecord(repo, branch, channelID, poll, active, sn);
        pollingData.add(pr);
        return pr;
    }

    /**
     * Identifies the PollingRecord to halt polling for and returns true IF it was successful.
     * Parameters identify the PollingRecord uniquely.
     * @return true if successful.
     */
    public boolean cancelPollingRecord(String channelID, String repo, String branch)
    {
        for (PollingRecord record: pollingData)
        {
            if ((record.channelID.contains(channelID)) && (record.repo.contains(repo)) && (record.branch.contains(branch)))
            {
                record.active = false;
                return true;
            }
        }
        return false;
    }

}
