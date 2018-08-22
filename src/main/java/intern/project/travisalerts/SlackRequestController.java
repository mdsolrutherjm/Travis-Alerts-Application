package intern.project.travisalerts;

import org.apache.tomcat.util.bcel.Const;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import java.util.Iterator;

@RestController
@RequestMapping("/command")
public class SlackRequestController implements Runnable {
    private final String CONSUMES = "application/x-www-form-urlencoded";

    public void run(){}

    @RequestMapping(value ="/getstatus")
    public void getStatus(WebRequest request)
    {
    }

    @RequestMapping(value ="/startpolling", consumes = CONSUMES)
    public void startpolling(WebRequest request)
    {
        String channelID = request.getParameter("channel_id");
        SlackNotifier response = new SlackNotifier(request.getParameter("response_url"));
        String[] parameter = request.getParameter("text").split(" "); //Array of each parameter sent.

        //attempt to get the permenant URL of the channel invoking this method.
        String permanentURL = TravisAlertsApplication.dc.getChannelURL(channelID);

        //Validation checks - do we have all of the required parameters?
        if (permanentURL == null) //do we have a permanent room URL??
        {
            response.sendSetupNewRoom(); // no room set-up - send an error message.
        }
        else if (parameter.length != 3)
        {
            response.sendInvalidParameters(ConstantUtils.USAGE_START_POLLING);
        }
        else
        {
            int minutes = convertToInteger(parameter[2]);
            //Validation checks - are these parameters valid?
            if (minutes > 0) //Our converter returns '0' if minutes is invalid.
            {
                String repo = parameter[0];
                String branch = parameter[1];
                Thread t = new Thread(new MainService(repo, branch, minutes,new SlackNotifier(permanentURL)));
                t.start();
            }
            else
            {
                response.sendInvalidParameters(ConstantUtils.INVALID_TIME_PARAMETER);
            }
        }
    }

    @RequestMapping(value ="/stoppolling", consumes = CONSUMES)
    public void stoppolling(WebRequest request)
    {
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
}
