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
    SlackNotifier notify = new SlackNotifier("T2BJH134Y/BC1JWUXUJ/wTCZ5YYFrTbe6D9OQVpKGBQy");

    public void run(){}

    @RequestMapping(value ="/getStatus", consumes = CONSUMES)
    public void getStatus(WebRequest request)
    {
        notify.sendText("Getting status for " +request.getParameter("text"));

    }
}
