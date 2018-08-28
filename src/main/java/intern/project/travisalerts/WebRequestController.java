package intern.project.travisalerts;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import java.io.IOException;
import java.nio.charset.Charset;


@RestController

public class WebRequestController {
    private final String INTERNAL_HTML_MISSING = "A project HTML document was missing. Rebuild this Java Application. ";


    @RequestMapping(value ="/app_status")
    public String app_status()
    {
        return "Travis Alerts Application Alive";
    }

    @RequestMapping(value ="/configure", params = {"code", "state"})
    public ModelAndView slackConfig(@RequestParam (value = "code") String code)
    {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", TravisAlertsApplication.config.clientID());
        map.add("client_secret", TravisAlertsApplication.config.clientSecret());
        map.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map,headers);

        try
        {
            ResponseEntity<String> response = restTemplate.postForEntity("https://slack.com/api/oauth.access", request, String.class);

            SlackAuthJSON slackAuth = JsonUtils.deserializeSlackAuth(response.getBody());

            if (slackAuth.ok != true)
            {
                //Slack will return false in the 'ok' field if there was an issue during processing.
                //This reasonably indicates to us whether or not as to if the rest of the data in the response is going to be useful or if we should just stop here.
                return new ModelAndView("Channel Information couldn't be retrieved. Is Slack alive? Check that the " + ConstantUtils.ENV_CLIENT_ID + ", " + ConstantUtils.ENV_CLIENT_SECRET + ", " + ConstantUtils.ENV_TRAVIS_TOKEN + " settings are accurate on the server-side.  ");
            }
            else{
                //Validation checks have passed - perform the actual operation now.

                if (TravisAlertsApplication.dc.getChannelURL(slackAuth.incomingWebhook.channelID) == null)
                {
                    //This channel ID hasn't been added before - create a new record for it.
                    TravisAlertsApplication.dc.addChannel(slackAuth.incomingWebhook.channelID, slackAuth.incomingWebhook.channelURL);
                }
                else
                {
                    //This channel ID has been previously configured. Maybe the channel URL has changed so just update that in the existing record.
                    TravisAlertsApplication.dc.changeChannelURL(slackAuth.incomingWebhook.channelID, slackAuth.incomingWebhook.channelURL);
                }
                new SlackNotifier(slackAuth.incomingWebhook.channelURL).sendUsageWithDescription(ConstantUtils.FIRST_TIME_CONFIG_RESPONSE, "startpolling", ConstantUtils.USAGE_START_POLLING);

                return new ModelAndView("redirect:" + "https://slack.com/app_redirect?channel=" + slackAuth.incomingWebhook.channelID);
            }
        }
        catch (RestClientException e)
        {
            return new ModelAndView(e.toString());
        }
    }
    @RequestMapping(value ="/configure", params = {"error"})
    public String slackConfigError(@RequestParam("error") String error)
    {
        try
        {
            return String.format(StreamUtils.copyToString( new ClassPathResource("configure_error.html").getInputStream(), Charset.defaultCharset()  ), error);
        }
        catch (IOException e)
        {
            return INTERNAL_HTML_MISSING;
        }
    }
    @RequestMapping(value ="/configure")
    public String slackConfigNoParams()
    {
        return slackConfigError("Invalid Parameters in URL.");
    }
    @RequestMapping(value ="/newchannel")
    public String newchannel()
    {
        try
        {
            return String.format(StreamUtils.copyToString( new ClassPathResource("newchannel.html").getInputStream(), Charset.defaultCharset()  ), TravisAlertsApplication.config.clientID());
        }
        catch (IOException e)
        {
            return INTERNAL_HTML_MISSING;
        }

    }
}
