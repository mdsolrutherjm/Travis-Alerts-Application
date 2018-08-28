package intern.project.travisalerts;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import sun.applet.Main;

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
        //new SlackNotifier object with same room as where request is from
        SlackNotifier response = new SlackNotifier(request.getParameter("response_url"));

        //Array of each parameter sent. [0] = repo, [1] = branch.
        String[] parameter = request.getParameter("text").split(" ");

        //Validation checks
        if (parameter.length != 2) //check if 2 parameters have been sent
        {
            response.sendInvalidParameters(ConstantUtils.USAGE_GET_STATUS);
        }
        else
        {
            String repo = parameter[0]; //repo = first parameter
            String branch = parameter[1]; //branch = second parameter
            MainService ms = new MainService(repo,branch, response);
            ms.pollAndNotify();
        }
    }

    @RequestMapping(value ="/startpolling", consumes = CONSUMES)
    public void startpolling(WebRequest request)
    {
        String channelID = request.getParameter("channel_id"); //chanelID of slack channel

        //new SlackNotifier object with same room as where request is from
        SlackNotifier response = new SlackNotifier(request.getParameter("response_url"));

        String[] parameter = request.getParameter("text").split(" "); //Array of each parameter sent.


        //Validation checks - is the channel configured?
        if (TravisAlertsApplication.dc.isChannelAlreadyConfigured(channelID) == false) //Is the channel that we're working on already configured?
        {
            //Channel not configured - stop this operation now and tell the user to configure the channel.
            response.sendSetupNewRoom();
        }
        else if (parameter.length != 3)
        {
            response.sendInvalidParameters(ConstantUtils.USAGE_START_POLLING);
        }
        else
        {
            String repo = parameter[0];
            String branch = parameter[1];
            int minutes = convertToInteger(parameter[2]);
            String permenantURL = TravisAlertsApplication.dc.getChannelURL(channelID);

            //Find any existing record which matches the specified repo and branch for this channel.
            PollingRecord existingRecord = TravisAlertsApplication.dc.getPollingRecord(repo, branch, channelID);

            //Validation checks - has this repo/branch already been configured?
            if (minutes > 0) //Our converter returns '0' if minutes is invalid.
            {
                if (existingRecord == null) //Case whereby there is no existing record.
                {
                    //No existing record - create a new one.
                    Thread t = new Thread(new MainService(TravisAlertsApplication.dc.createPollingRecord(repo, branch, channelID, minutes * 60000,true, new SlackNotifier(permenantURL))));
                    t.start();
                }
                else
                {
                    //Record for this repo and branch already exists.
                    if (existingRecord.status() == false) //Case whereby the record has been set to false, but the
                                                        // associated thread hasn't woken up yet so it hasn't been terminated yet.
                    {
                    //Re-enable the record and update the time parameter, then load a new thread for this record.
                    existingRecord.setPollingInterval(minutes);
                    existingRecord.activate();

                    //When we activate a record, it won't poll until its' associated thread wakes up.
                        // This gets around the issue by doing a one-off additional poll.
                    MainService immediateResponse =  new MainService(repo, branch, new SlackNotifier(permenantURL));
                    immediateResponse.pollAndNotify();
                    }
                    else
                    {
                        //Repo and branch for this channel is already being polled  and is active - tell the user.
                        response.sendRepoBranchAlreadyBeingPolled(repo, branch);
                    }
                }
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

        //Validation checks - is the channel configured?
        if (TravisAlertsApplication.dc.isChannelAlreadyConfigured(channelID) == false)
        {
            //Channel not configured - stop this operation now and tell the user to configure the channel.
            response.sendSetupNewRoom();
        }
        //Validation checks - do we have the right number of parameters?
        else if (parameter.length != 2)
        {
            //Missing or too many parameters - stop and inform the user.
            response.sendInvalidParameters(ConstantUtils.USAGE_STOP_POLLING);
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
