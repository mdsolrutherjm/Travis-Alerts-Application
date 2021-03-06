package intern.project.travisalerts;

import java.io.*;
import java.util.ArrayList;

import static intern.project.travisalerts.ConstantUtils.*;

public class DataController {
    //ArrayLists
    ArrayList<PollingRecord> pollingData = new ArrayList<>();
    ArrayList<ChannelRecord> channelData = new ArrayList<>();

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
        readChannelRecordsFromDisk();
        readPollingRecordFromDisk();

    }

    public void readChannelRecordsFromDisk()
    {
        //Create a file object for the channel records file
        File file = new File(FILE_CHANNEL_RECORD);

        if (file.exists()) //Only attempt to read the file if we know it exists.
        {
            if (channelData.isEmpty()) { //if channelData is empty
                try {
                    FileReader fr = new FileReader(file);
                    BufferedReader br = new BufferedReader(fr); //object to read .txt file
                    String line;
                    while ((line = br.readLine()) != null){ //continues to read through the whole file
                        //Split the line up into separate elements.
                        String[] channelElements = line.split(","); //split the line by comma instances
                        //Load those elements into a record within the ArrayList
                        channelData.add(new ChannelRecord(channelElements[0], channelElements[1]));
                    }
                }
                catch (IOException e){
                    System.out.println(String.format(FAILED_IOEX_READING_FILE, file.toString(), e.toString()));
                }
            }
        }
        else
        {
            System.out.println(String.format(FAILED_MISSING_FILE, FILE_CHANNEL_RECORD));
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

        String fileName = FILE_CHANNEL_RECORD;
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            fw = new FileWriter(fileName);
            bw = new BufferedWriter(fw);

            //write each record to the file.
            for (ChannelRecord record: channelData){
                bw.write(record.id+"," + record.url + "\n");
            }
        }
        catch (IOException e) {System.out.println(String.format(FAILED_IOEX_WRITING_FILE, fileName, e.toString()));}

        finally {
            try {
                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();
            } catch (IOException ex) {System.out.println(String.format(FAILED_IOEX_WRITING_FILE, fileName, ex.toString()));}
        }
    }

    /**
     * Traverses through the URL array and finds the requested channel URL based on a matching channel ID.
     * @param name the channel ID to find the relevant URL for .
     * @return the channel URL found (or null if the URL does not exist. )
     */
    public String getChannelURL(String name)
    {
         for (ChannelRecord channel: channelData)
         {
             if (channel.id.contains(name))
             {
                 return channel.url;
             }
         }
         return null; //Channel wasn't found.
    }

    /**
     * Sometimes channels are re-configured and Slack will generate a new channel URL. This method deals with this event and will update an existing channel record
     * with the new channel URL.
     * @param id ID of the channel record to perform the operation upon.
     * @param newUrl The new URL to update the record with
     * @return True if the operation was successful.
     */
    public boolean changeChannelURL(String id, String newUrl)
    {
        for (ChannelRecord channel: channelData)
        {
            if (channel.id.contains(id))
            {
                channel.url = newUrl;
                return true; //Record was found and updated.
            }
        }
        return false; //Record wasn't found.
    }
    /**
     * Takes in the parameters to create a new polling record, creates it and returns the address of it.
     * @return the PollingRecord object reference.
     */
    public PollingRecord createPollingRecord(String repo, String branch, String channelID, int poll, boolean active, SlackNotifier sn)
    {
        PollingRecord pr = new PollingRecord(repo, branch, channelID, poll, active, sn);
        pollingData.add(pr);


        writePollingRecordToDisk();
        return pr;
    }

    /**
     * Checks if this channel has already been configured based on its name.
     * @param channelID the ID of the channel
     * @return true if it already exists.
     */
    public boolean isChannelAlreadyConfigured(String channelID)
    {
        for (ChannelRecord record: channelData)
        {
            if ((record.id.contains(channelID)))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds a polling record based on its associated repo, branch and channel ID.
     * @param repo Repo name
     * @param branch Branch name
     * @param channelID Associated channel name
     * @return The associated polling record.
     */
    public PollingRecord getPollingRecord(String repo, String branch, String channelID)
    {
        for (PollingRecord record: pollingData)
        {
            if ((record.repo.contains(repo)) && (record.branch.contains(branch)) && (record.channelID.contains(channelID)))
            {
                return record; //Found the record - return it.
            }
        }
        return null; //No record found - return null.
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
                writePollingRecordToDisk(); //We no longer want this in our records - update the text file.
                return true;
            }
        }
        return false;
    }

    /**
     * Removes the specified record from the Array List.
     *
     * WARNING: Do NOT use this to terminate a Polling Service.
     *          This should be invoked by MainService ONLY once it has left the while loop.
     *          To safely terminate a Polling Service, see 'cancelPollingRecord()'
     *
     * @param record The record to be removed from the Array List.
     */
    public void removePollingRecordFromArray(PollingRecord record)
    {
        pollingData.remove(record);
    }
    public void writePollingRecordToDisk()
    {
        try
        {
            BufferedWriter write = new BufferedWriter(new FileWriter(ConstantUtils.FILE_POLLING_RECORD, false));
            for (PollingRecord r: pollingData)
            {
                if (r.active) //is this a polling record that is actually in use?
                {
                    write.write(r.repo + "," + r.branch + "," + r.channelID + "," +  r.poll +"\n");

                }
            }
            write.close(); //finish with the file.
        }
        catch (IOException e)
        {
            //is this error handling really enough?
            System.out.println(String.format(FILE_POLLING_RECORD, FILE_POLLING_RECORD, e.toString()));
        }
    }
    public void readPollingRecordFromDisk()
    {
        File file = new File(FILE_POLLING_RECORD);

        if (file.exists()) //Only attempt to read the file if we know it exists.
        {
                try
                {
                    FileReader fr = new FileReader(file);
                    BufferedReader read = new BufferedReader(fr);
                    String line;
                    while ((line = read.readLine()) != null) { //Continues to read through the whole file.
                        //Split the line up into separate elements.
                        String[] elements = line.split(",");
                        //Load those elements into a record within the ArrayList
                        pollingData.add(new PollingRecord(elements[0], elements[1], elements[2], convertToInteger(elements[3]), true, new SlackNotifier(getChannelURL(elements[2]))));
                    }
                }
                catch (IOException e)
                {
                    System.out.println(String.format(FAILED_IOEX_READING_FILE, file.toString(), e.toString()));
                }
            }
           else
            {
                System.out.println(String.format(FAILED_MISSING_FILE, FILE_POLLING_RECORD));
            }

    }
    /**
     * Converts strings to integers.
     * Returns '0' if the number was not converted.
     * @param s String to convert to integer
     * @return the converted value.
     */
    public int convertToInteger(String s)
    {
        try
        {
            return Integer.parseInt(s);
        }
        catch (NumberFormatException e)
        {
            return 0;
        }
    }

    /**
     * When TravisAlertsApplication loads, we need to create new polling services from each record.
     * This accessor provides those records in an ArrayList.
     * @return List of pollingrecords.
     * */
    public ArrayList<PollingRecord> getPollingRecords()
    {
        return pollingData;
    }
}
