package intern.project.travisalerts;

public class PollingRecord {
    public String repo;
    public String branch;
    public String channelID;
    public int poll;
    public boolean active;
    public SlackNotifier sn;
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
