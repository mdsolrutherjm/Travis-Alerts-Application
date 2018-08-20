package intern.project.travisalerts;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


public class SlackNotifier {
    private final String PASSED_COLOUR = "#36a64f";
    private final String FAILED_COLOUR = "#ff0000";
    private String URL;


    public SlackNotifier(String url)
    {
        this.URL = url;
    }

    //Template for the passed/failed build description (to go within the template)
    private final String DESCRIPTION = "Build #%d %s (%s, %s)\n%s@%s";
    private final String INVALID_PARAMETERS = "The specified query parameters are invalid. ";
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
    private final String SETUP_NEW_ROOM = "{\n" +
            "    \"text\": \"You have not set up this channel for Travis Alerts! \\n Click the button below to begin the setup.\",\n" +
            "    \"attachments\": [\n" +
            "        {\n" +
            "            \"fallback\": \"You have not set up this Channel for Travis Alerts! \\n Click the button below to begin the setup.\",\n" +
            "            \"actions\": [\n" +
            "                {\n" +
            "                    \"type\": \"button\",\n" +
            "                    \"text\": \"Set up Travis Alerts\",\n" +
            "                    \"url\": \"http://travisalertdemo-sandbox.imedidata.net/newchannel\"\n" +
            "                }\n" +
            "            ]\n" +
            "        }\n" +
            "    ]\n" +
            "}";
    private final String ERROR_PROCESSING = "{\n" +
            "    \"text\": \"%s\",\n" +
            "    \"attachments\": [\n" +
            "        {\n" +
            "            \"text\": \"%s\",\n" +
            "\t\t\t\"color\": \"%s\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";
    /**
     * Sends a properly formatted build failure message to the pre-set Slack channel.
     */
    public void sendFailed(int buildID, String slug, String branch, String  by, String time, String buildURL) {
        String failedDescription = String.format(DESCRIPTION, buildID, "failed", by, time, slug, branch);

        String json = String.format(BUILD_TEMPLATE, buildURL,FAILED_COLOUR, failedDescription, buildURL);
        sendJson(json);
    }

    /**
     * Sends a properly formatted build passed message to the pre-set Slack channel.
     */
    public void sendPassed(int buildID, String slug, String branch, String  by, String time, String buildURL) {
        String passedDescription = String.format(DESCRIPTION, buildID, "passed", by, time, slug, branch);

        String json = String.format(BUILD_TEMPLATE, buildURL,PASSED_COLOUR, passedDescription, buildURL);
        sendJson(json);
    }

    /**
     * Sends a standard text message to the pre-set Slack channel.
     * @param text text to be contained within the message.
     */
    public void sendText(String text)
    {
        sendJson("{\'text\':\'" + text + "\'}");
    }
    /**
     * Sends a standard text message to set up a new room.
     */
    public void sendSetupNewRoom()
    {
        sendJson(SETUP_NEW_ROOM);
    }
    /**
     * Sends a standard error message for if the polling service returned 404 not found.
     */
    public void sendRepoBranchNotFound(String repo, String branch, String e)
    {
        String title = String.format("Travis Alerts was unable to poll %s@%s.\nEnsure the specified repository and branch names are accurate and try again.", repo, branch);
        String description = String.format("(%s)", e);
        sendJson(String.format(ERROR_PROCESSING, title, description, FAILED_COLOUR));
    }
    /**
     * Sends a standard error message for if the polling service returned 404 not found.
     */
    public void sendInvalidParameters(String usage)
    {
        sendJson(String.format(ERROR_PROCESSING, INVALID_PARAMETERS, usage, FAILED_COLOUR));
    }
    /**
     * Sends JSON to the pre-set Slack channel.
     * @param json JSON to be sent.
     * @throws HttpClientErrorException throws this if some kind of failure occurs (e.g. 404).
     */
    @Bean
    public void sendJson(String json) throws HttpClientErrorException
    {
        RestTemplate restTemplate = new RestTemplate();



        HttpEntity<String> request = new HttpEntity<>(json);
        restTemplate.exchange(URL, HttpMethod.POST, request, String.class);
    }
}