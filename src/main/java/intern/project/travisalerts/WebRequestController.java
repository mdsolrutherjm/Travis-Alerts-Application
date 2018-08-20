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
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;

@RestController

public class WebRequestController {
    @RequestMapping(value ="/app_status")
    public String app_status()
    {
        return "Travis Alerts Application Alive";
    }

    @RequestMapping(value ="/configure", params = {"code", "state"})
    public String slackconfig(@RequestParam (value = "code") String code)
    {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", "79629037168.408923971728" ); //REMOVE THIS LATER.
        map.add("client_secret", "e0d8e425514cad61362fc6167e93c386");
        map.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map,headers);

        ResponseEntity<String> response = restTemplate.postForEntity("https://slack.com/api/oauth.access", request, String.class);

        System.out.println(response.getBody());
        SlackAuthJSON slackAuth = JsonUtils.deserializeSlackAuth(response.getBody());


        TravisAlertsApplication.dc.addChannel(slackAuth.incomingWebhook.channelID, slackAuth.incomingWebhook.channelURL);
        new SlackNotifier(slackAuth.incomingWebhook.channelURL).sendText("Configured");
        try
        {
            return StreamUtils.copyToString( new ClassPathResource("configure.html").getInputStream(), Charset.defaultCharset()  );
        }
        catch (IOException e)
        {
            return "Not Found";
        }
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
            return "Not Found";
        }

    }
}
