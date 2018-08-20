package intern.project.travisalerts;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.xml.ws.Response;

@RestController

public class AppStatusRequestController {
    private final String SLACKBTN = "<a href=\"https://slack.com/oauth/authorize?client_id=79629037168.408923971728&scope=incoming-webhook,commands\"><img alt=\"Add to Slack\" height=\"40\" width=\"139\" src=\"https://platform.slack-edge.com/img/add_to_slack.png\" srcset=\"https://platform.slack-edge.com/img/add_to_slack.png 1x, https://platform.slack-edge.com/img/add_to_slack@2x.png 2x\" /></a>";
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

        SlackNotifier sn = new SlackNotifier(slackAuth.incomingWebhook.channelURL);

        //save the channel ID and channel URL to a text file.

        sn.sendText("New Channel authenticated!");
        return "<h1>Slack Alerts Notifier</h1>\nProcessing your request... ";
    }
    @RequestMapping(value ="/newchannel")
    public String newchannel()
    {
        return "<h2>Click below to add Travis Alerts to a new Medidata Slack channel!\n" + SLACKBTN;
    }
}
