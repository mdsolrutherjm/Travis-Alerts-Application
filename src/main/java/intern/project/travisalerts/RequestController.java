package intern.project.travisalerts;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("/command")
public class RequestController implements Runnable {
    SlackNotifier notify = new SlackNotifier("T2BJH134Y/BC1JWUXUJ/wTCZ5YYFrTbe6D9OQVpKGBQy");

    public void run(){
        System.out.println("Web Command Response Service started. ");

    }
    @RequestMapping(value ="/getStatus", consumes ="application/x-www-form-urlencoded")
    public void getStatus(WebRequest request)
    {
        notify.sendText("Getting status for " +request.getParameter("text"));

    }
}
