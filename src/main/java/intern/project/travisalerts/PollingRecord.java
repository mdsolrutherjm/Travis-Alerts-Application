package intern.project.travisalerts;

public class PollingRecord {
    public String repo; //repo to poll from
    public String branch; //branch to poll from
    public String channelID; //channelID of slack room
    public int poll; //time interval to poll from
    public boolean active; //active = true if polling service on. False if turned off
    public SlackNotifier sn; //SlackNotifier object with room to send records to
    public PollingRecord(String repo, String branch, String channelID, int poll, boolean active, SlackNotifier sn)
    {
        this.repo = repo;
        this.branch = branch;
        this.poll = poll;
        this.channelID = channelID;
        this.active = active;
        this.sn = sn;
    }
}
