package intern.project.travisalerts;


import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;




public class SlackNotifier {
    private final String PASSED_COLOUR = "#36a64f";
    private final String FAILED_COLOUR = "#ff0000";
    private final String SLACK_API = "https://hooks.slack.com/services/{room}";
    private final String SLACK_ROOM = "T2BJH134Y/BC1JWUXUJ/wTCZ5YYFrTbe6D9OQVpKGBQy";

    //Template for the passed/failed build messages.
    private final String BUILD_TEMPLATE = "{\n" +
            "    \"attachments\": [\n" +
            "        {\n" +
            "            \"fallback\": \"Review Build at %s\",\n" +
            "\t\t\t\"color\": \"%s\",\n" +
            "\t\t\t\"text\":\"%s\",\n" +
            "            \"actions\": [\n" +
            "                {\n" +
            "                    \"type\": \"button\",\n" +
            "                    \"text\": \"Review build\",\n" +
            "                    \"url\": \"%s\"\n" +
            "                }\n" +
            "            ]\n" +
            "        }\n" +
            "    ]\n" +
            "}";
    //Template for the passed/failed build description (to go within the template)
    private final String DESCRIPTION = "Build #%d %s (%s, %s)\n%s@%s";


    /**
     * Sends a properly formatted build failure message to the pre-set Slack channel.
     */
    public void sendFailed(int buildID, String slug, String branch, String  by, String time, String buildURL) {
        String failedDescription = String.format(DESCRIPTION, buildID, "failed", by, time, slug, branch);

        String json = String.format(BUILD_TEMPLATE, buildURL,FAILED_COLOUR, failedDescription, buildURL);
        sendMsg(json);
    }

    /**
     * Sends a properly formatted build passed message to the pre-set Slack channel.
     */
    public void sendPassed(int buildID, String slug, String branch, String  by, String time, String buildURL) {
        String passedDescription = String.format(DESCRIPTION, buildID, "passed", by, time, slug, branch);

        String json = String.format(BUILD_TEMPLATE, buildURL,PASSED_COLOUR, passedDescription, buildURL);
        sendMsg(json);
    }

    /**
     * Sends a standard text message to the pre-set Slack channel.
     * @param text text to be contained within the message.
     */
    public void sendText(String text)
    {
        sendMsg("{\'text\':\'" + text + "\'}");
    }

    /**
     * Sends JSON to the pre-set Slack channel.
     * @param json JSON to be sent.
     * @throws HttpClientErrorException throws this if some kind of failure occurs (e.g. 404).
     */
    @Bean
    public void sendMsg(String json) throws HttpClientErrorException
    {
        RestTemplate restTemplate = new RestTemplate();

        UriComponents uri = UriComponentsBuilder
                .fromHttpUrl(SLACK_API)
                .buildAndExpand(SLACK_ROOM);


        HttpEntity<String> request = new HttpEntity<>(json);
        restTemplate.exchange(uri.toString(), HttpMethod.POST, request, String.class);
    }
}