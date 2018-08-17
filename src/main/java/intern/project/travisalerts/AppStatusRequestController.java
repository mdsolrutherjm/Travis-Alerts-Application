package intern.project.travisalerts;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

@RestController

public class AppStatusRequestController {
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

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("client_id", "79629037168.408923971728" ); //REMOVE THIS LATER.
        map.add("client_secret", "e0d8e425514cad61362fc6167e93c386");
        map.add("code", code);


        restTemplate.postForEntity("https://slack.com/api/oauth.access", , String.class);
        return "<h1>Slack Alerts Notifier</h1>\nProcessing your request... ";

    }
}
