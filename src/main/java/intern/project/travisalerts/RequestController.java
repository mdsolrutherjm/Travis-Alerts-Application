package intern.project.travisalerts;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("/command")
public class RequestController implements Runnable {
    private final String CONSUMES = "application/x-www-form-urlencoded";

    /**
     * use of slacknotifier should be temporary????? should response using the response_url method instead once we are actually
     * having this hosted.
     */
    SlackNotifier slackAPI = new SlackNotifier("T2BJH134Y/BC1JWUXUJ/wTCZ5YYFrTbe6D9OQVpKGBQy");

    public void run(){}

    @RequestMapping(value ="/getstatus")
    public void getStatus(WebRequest request)
    {
        slackAPI.sendText("Not yet implemented :(\nNo method yet exists for getstatus. ");

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
        String[] parameter = request.getParameter("text").split(" "); //Array of each parameter sent.
        slackAPI.sendText("Added " + parameter[0] + "/" +  parameter[1]);
        Thread pollingSvc = new Thread(new MainService(parameter[0], parameter[1], slackAPI));
        pollingSvc.start();
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
