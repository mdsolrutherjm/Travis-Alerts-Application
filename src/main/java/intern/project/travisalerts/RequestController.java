package intern.project.travisalerts;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO. is currently blank.
 *
 * maybe make it in a forever loop and create a new main svc within it as needed & destroy after???
 */

@RestController
@RequestMapping("/cmd")
public class RequestController implements Runnable {
    SlackNotifier notify = new SlackNotifier("T2BJH134Y/BC1JWUXUJ/wTCZ5YYFrTbe6D9OQVpKGBQy");
    public RequestController()
    {
    }
    public void run(){


    }
    @RequestMapping()
    public void listenForIncomingConnections(@RequestParam(value = "name", defaultValue = "noname") String name)
    {
        notify.sendText(name);
    }
}
