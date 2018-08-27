package intern.project.travisalerts;
import org.apache.tomcat.jni.Poll;

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
        String fileName = FILE_CHANNEL_RECORD; //get .txt file

        if (channelData.isEmpty()) { //if channelData is empty
            try {
                FileReader fr = new FileReader(fileName);
                BufferedReader br = new BufferedReader(fr); //object to read .txt file
                String line;
                while ((line = br.readLine()) != null){ //continues to read through the whole file
                    System.out.println(line);
                    String[] channelElements = line.split(","); //split the line by comma instances
                    channelData.add(new ChannelRecord(channelElements[0], channelElements[1]));
                }
            }
            catch (IOException e){
                System.out.println(String.format(FAILED_IOEX_READING_FILE, fileName, e.toString()));
            }
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
        System.out.println("SEARCHING FOR CHANNEL " + name);
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
    public boolean isChannelAlreadyConfigured(String channelID, String channelURL)
    {
        for (ChannelRecord record: channelData)
        {
            if ((record.id.contains(channelID)) && (record.url.contains(channelURL)))
            {
                return true;
            }
        }
        return false;
    }
    /**
     * Checks if this repo/branch is already being polled for this channel.
     * @param channelID the ID of the channel
     * @param repo repo
     * @param branch branch.
     * @return true if it already exists.
     */
    public boolean isRepoBranchAlreadyBeingPolled(String repo, String branch, String channelID)
    {
        for (PollingRecord record: pollingData)
        {
            if ((record.repo.contains(repo)) && (record.branch.contains(branch)) && (record.channelID.contains(channelID)))
            {
                return true;
            }
        }
        return false;
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
        try
        {
            BufferedReader read = new BufferedReader(new FileReader(FILE_POLLING_RECORD));
            String line;
            try
            {
                while ((line = read.readLine()) != null) {
                    String[] elements = line.split(","); //split the line by comma instances
                    PollingRecord pr = new PollingRecord(elements[0], elements[1], elements[2], convertToInteger(elements[3]), true, new SlackNotifier(getChannelURL(elements[2])));
                    pollingData.add(pr);
                    }
            }
            catch (IOException e)
            {
                System.out.println(String.format(FAILED_IOEX_READING_FILE, FILE_POLLING_RECORD, e.toString()));
            }

        }
        catch (FileNotFoundException e)
        {
            System.out.println(String.format(FAILED_MISSING_FILE, FILE_POLLING_RECORD, e.toString()));
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
