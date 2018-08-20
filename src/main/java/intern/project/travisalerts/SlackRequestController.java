package intern.project.travisalerts;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import java.util.Iterator;

@RestController
@RequestMapping("/command")
public class SlackRequestController implements Runnable {
    private final String CONSUMES = "application/x-www-form-urlencoded";

    /**
     * use of slacknotifier should be temporary????? should response using the response_url method instead once we are actually
     * having this hosted.
     */
    SlackNotifier slackAPI = new SlackNotifier( "https://hooks.slack.com/services/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");

    public void run(){}

    @RequestMapping(value ="/getstatus")
    public void getStatus(WebRequest request)
    {
        slackAPI.sendText("Not yet implemented :(\nNo method yet exists for getstatus. ");
    }
    @RequestMapping(value ="/debug")
    public void debug(WebRequest request)
    {
        SlackNotifier sn = new SlackNotifier(request.getParameter("response_url"));
        Iterator<String> params = request.getParameterNames();
        while(params.hasNext()){
            String paramName = params.next();
            sn.sendText("Parameter Name - "+paramName+", Value - "+request.getParameter(paramName));
        }
    }
    /**
     * TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO
     * 1) there's no input validation for the parameters that we're dealing with here.
     * 2) this command should ONLY be adding to a database so you can start it with startpolling. but since theres no DB, we'll just do it here for now.
     *
     * Deals with the addbranch command.
     * Adds a new branch to poll.
     */
    @RequestMapping(value ="/addbranch", consumes = CONSUMES)
    public void addbranch(WebRequest request)
    {
        String channelID = request.getParameter("channel_id");
        String channelURL = request.getParameter("response_url");
        String[] parameter = request.getParameter("text").split(" "); //Array of each parameter sent.

        String repo = parameter[0];
        String branch = parameter[1];


        new SlackNotifier(channelURL).sendText("Set to poll " + repo + " " + branch);
        Thread t = new Thread(new MainService(repo, branch, new SlackNotifier(TravisAlertsApplication.dc.getChannelURL(channelID))));
        t.start();
    }
    /**
     * TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO
     * should insert the polling time here. but currently theres no db - so it doesn't do much.
     *
     * Deals with the addbranch command.
     * Adds a new branch to poll.
     * @param request
     */
    @RequestMapping(value ="/deletebranch", consumes = CONSUMES)
    public void deletebranch(WebRequest request)
    {
        slackAPI.sendText("Not yet implemented :(\nNo method yet exists for deletebranch. ");
    }
    /**
     * TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO
     * should insert the polling time here. but currently theres no db - so it doesn't do much.
     *
     * Deals with the addbranch command.
     * Adds a new branch to poll.
     * @param request
     */
    @RequestMapping(value ="/startpolling", consumes = CONSUMES)
    public void startpolling(WebRequest request)
    {
        slackAPI.sendText("Not yet implemented :(\nNo method yet exists for startpolling. ");

    }
    /**
     * TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO TO-DO
     * should insert the polling time here. but currently theres no db - so it doesn't do much.
     *
     * Deals with the addbranch command.
     * Adds a new branch to poll.
     * @param request
     */
    @RequestMapping(value ="/stoppolling", consumes = CONSUMES)
    public void stoppolling(WebRequest request)
    {
        slackAPI.sendText("Not yet implemented :(\nNo method yet exists for startpolling. ");
    }
}
