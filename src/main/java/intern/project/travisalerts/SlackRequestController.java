package intern.project.travisalerts;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import java.util.Iterator;

@RestController
@RequestMapping("/command")
public class SlackRequestController implements Runnable {
    private final String CONSUMES = "application/x-www-form-urlencoded";
    private final String ADD_BRANCH_USAGE = "Usage: /addbranch [repo] [branch]";

    public void run(){}

    @RequestMapping(value ="/getstatus")
    public void getStatus(WebRequest request)
    {
    }

    @RequestMapping(value ="/addbranch", consumes = CONSUMES)
    public void addbranch(WebRequest request)
    {
        String channelID = request.getParameter("channel_id");
        SlackNotifier response = new SlackNotifier(request.getParameter("response_url"));
        String[] parameter = request.getParameter("text").split(" "); //Array of each parameter sent.

        //attempt to get the permenant URL of the channel invoking this method.
        String permanentURL = TravisAlertsApplication.dc.getChannelURL(channelID);

        if (permanentURL == null) //do we have a permanent room URL??
        {
            response.sendSetupNewRoom(); // no room set-up - send an error message.
        }
        else if (parameter.length != 2)
        {
            response.sendInvalidParameters(ADD_BRANCH_USAGE); //
        }
        else
        {
            String repo = parameter[0];
            String branch = parameter[1];
            Thread t = new Thread(new MainService(repo, branch, 5,new SlackNotifier(permanentURL)));
            t.start();
        }
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
    }
}
