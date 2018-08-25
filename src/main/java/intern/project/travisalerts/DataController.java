package intern.project.travisalerts;

import org.apache.tomcat.util.bcel.Const;

import java.io.*;
import java.util.ArrayList;

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
        readPollingRecordFromDisk();

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

        writePollingRecordToDisk();
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
    public void writePollingRecordToDisk()
    {
        try
        {
            BufferedWriter write = new BufferedWriter(new FileWriter(ConstantUtils.FILE_POLLING_RECORD, false));
            for (PollingRecord r: pollingData)
            {

                write.write(r.repo + "," + r.branch + "," + r.channelID + "," +  r.poll +"\n");
            }
            write.close(); //finish with the file.
        }
        catch (IOException e)
        {
            //is this error handling really enough?
            System.out.println("ERROR: could not write to " + ConstantUtils.FILE_POLLING_RECORD);
        }
    }
    public void readPollingRecordFromDisk()
    {
        try
        {
            BufferedReader read = new BufferedReader(new FileReader(ConstantUtils.FILE_POLLING_RECORD));
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
                System.out.println("ERROR: could not read from " + ConstantUtils.FILE_POLLING_RECORD);
            }

        }
        catch (FileNotFoundException e)
        {
            System.out.println("ERROR: Missing " + ConstantUtils.FILE_POLLING_RECORD + ". This is OK if this is a clean boot. ");
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
