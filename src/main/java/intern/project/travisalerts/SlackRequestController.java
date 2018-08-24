package intern.project.travisalerts;

import org.apache.tomcat.util.bcel.Const;
import org.apache.tomcat.util.bcel.classfile.Constant;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import java.util.Iterator;

@RestController
@RequestMapping("/command")
public class SlackRequestController implements Runnable {
    private final String CONSUMES = "application/x-www-form-urlencoded";

    public void run(){}

    /**
     * Functionality to respond to /getstatus command.
     * @param request
     */
    @RequestMapping(value ="/getstatus", consumes = CONSUMES)
    public void getStatus(WebRequest request)
    {
        String channelID = request.getParameter("channel_id"); //chanelID of slack channel

        //new SlackNotifier object with same room as where request is from
        SlackNotifier response = new SlackNotifier(request.getParameter("response_url"));

        //Array of each parameter sent. [0] = repo, [1] = branch.
        String[] parameter = request.getParameter("text").split(" ");

        System.out.println(parameter[0] + "," + parameter[1]);

        //attempt to get the permanent URL of the channel invoking this method.
        String permanentURL = TravisAlertsApplication.dc.getChannelURL(channelID);

        //Validation checks
        //checking if the current slack channel has already been configured
        if (permanentURL == null)
        {
            response.sendSetupNewRoom(); // no room set-up - send an error message.
        }
        else if (parameter.length != 2) //check if 2 parameters have been sent
        {
            response.sendInvalidParameters(ConstantUtils.USAGE_GET_STATUS);
        }

        String repo = parameter[0]; //repo = first paramater
        String branch = parameter[1]; //branch = second parameter
        SlackNotifier slackRoom = new SlackNotifier(permanentURL); //slack room where request is from
        MainService ms = new MainService(repo,branch, slackRoom);
        ms.pollAndNotify();
    }

    @RequestMapping(value ="/startpolling", consumes = CONSUMES)
    public void startpolling(WebRequest request)
    {
        String channelID = request.getParameter("channel_id"); //chanelID of slack channel

        //new SlackNotifier object with same room as where request is from
        SlackNotifier response = new SlackNotifier(request.getParameter("response_url"));

        String[] parameter = request.getParameter("text").split(""); //Array of each parameter sent.

        //attempt to get the permanent URL of the channel invoking this method.
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
                Thread t = new Thread(new MainService(TravisAlertsApplication.dc.createPollingRecord(repo, branch, channelID, minutes * 60000,true, new SlackNotifier(permanentURL))));
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
        String channelID = request.getParameter("channel_id");
        SlackNotifier response = new SlackNotifier(request.getParameter("response_url"));

        String[] parameter = request.getParameter("text").split(" "); //Array of each parameter sent.


        if (parameter.length != 2)
        {
            response.sendInvalidParameters(ConstantUtils.USAGE_START_POLLING);
        }
        else
        {
            String repo = parameter[0];
            String branch = parameter[1];
            if (TravisAlertsApplication.dc.cancelPollingRecord(channelID,repo,branch) == true) //true indicates it has successfully terminated the polling service.
            {
                response.sendText(String.format(ConstantUtils.TERMINATING_POLLING, repo, branch));
            }
            else
            {
                response.sendText(String.format(ConstantUtils.TERMINATING_POLLING_ERROR, repo,branch));
            }
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
}
