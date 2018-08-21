package intern.project.travisalerts;

import org.apache.tomcat.util.bcel.Const;
import org.apache.tomcat.util.bcel.classfile.Constant;
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
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

@RestController

public class WebRequestController {
    static Map<String, String> env = System.getenv();
    private static String CLIENT_ID = env.get(ConstantUtils.ENV_CLIENT_ID);
    private static String CLIENT_SECRET = env.get(ConstantUtils.ENV_CLIENT_SECRET);

    private final String INTERNAL_HTML_MISSING = "A project HTML document was missing. Rebuild this Java Application. ";
    public WebRequestController()
    {
        if (CLIENT_ID == null)
        {
            System.out.println(
                    String.format(ConstantUtils.MISSING_ENV_VARIABLE, ConstantUtils.ENV_CLIENT_ID));
        }
        if (CLIENT_SECRET == null)
        {
            System.out.println(
                    String.format(ConstantUtils.MISSING_ENV_VARIABLE, ConstantUtils.ENV_CLIENT_SECRET));
        }
    }
    @RequestMapping(value ="/app_status")
    public String app_status()
    {
        return "Travis Alerts Application Alive";
    }

    @RequestMapping(value ="/configure", params = {"code", "state"})
    public String slackConfig(@RequestParam (value = "code") String code)
    {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", CLIENT_ID);
        map.add("client_secret", CLIENT_SECRET);
        map.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map,headers);

        try
        {
            ResponseEntity<String> response = restTemplate.postForEntity("https://slack.com/api/oauth.access", request, String.class);

            System.out.println(response.getBody());
            SlackAuthJSON slackAuth = JsonUtils.deserializeSlackAuth(response.getBody());

            if (slackAuth.ok != true)
            {
                return slackConfigError("Channel Information couldn't be retrieved. Is Slack alive?");
            }
            else
            {
                TravisAlertsApplication.dc.addChannel(slackAuth.incomingWebhook.channelID, slackAuth.incomingWebhook.channelURL);
                new SlackNotifier(slackAuth.incomingWebhook.channelURL).sendText("Configured");
                try
                {
                    return String.format(StreamUtils.copyToString( new ClassPathResource("configure.html").getInputStream(), Charset.defaultCharset()  ), slackAuth.incomingWebhook.channel);
                }
                catch (IOException e)
                {
                    return INTERNAL_HTML_MISSING;
                }
            }
        }
        catch (RestClientException e)
        {
            return slackConfigError(e.toString());
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
            return StreamUtils.copyToString( new ClassPathResource("newchannel.html").getInputStream(), Charset.defaultCharset()  );
        }
        catch (IOException e)
        {
            return INTERNAL_HTML_MISSING;
        }

    }
}
