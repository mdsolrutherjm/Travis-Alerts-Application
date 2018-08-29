package intern.project.travisalerts;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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
    private final String CHANNEL_SETUP_LINK = "https://slack.com/oauth/authorize?client_id=%s&scope=incoming-webhook,commands";
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
    private final String ERROR_SETUP_NEW_ROOM = "{\n" +
            "    \"text\": \"You have not set up this channel for Travis Alerts! \\n Click the button below to begin the setup.\",\n" +
            "    \"attachments\": [\n" +
            "        {\n" +
            "            \"fallback\": \"You have not set up this Channel for Travis Alerts! \\n Click the button below to begin the setup.\",\n" +
            "            \"actions\": [\n" +
            "                {\n" +
            "                    \"type\": \"button\",\n" +
            "                    \"text\": \"Set up Travis Alerts\",\n" +
            "                    \"url\": \""+CHANNEL_SETUP_LINK+"\"\n" +
            "                }\n" +
            "            ]\n" +
            "        }\n" +
            "    ]\n" +
            "}";
    private final String SETUP_NEW_ROOM = "{\n" +
            "    \"text\": \"Click the button below to set up this channel for Travis Alerts!\",\n" +
            "    \"attachments\": [\n" +
            "        {\n" +
            "            \"fallback\": \"Click the button below to set up this channel for Travis Alerts!\",\n" +
            "            \"actions\": [\n" +
            "                {\n" +
            "                    \"type\": \"button\",\n" +
            "                    \"text\": \"Set up Travis Alerts\",\n" +
            "                    \"url\": \""+CHANNEL_SETUP_LINK+"\"\n" +
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
    private final String USAGE_WITH_DCESCRIPTION = "{\n" +
            "    \"attachments\": [\n" +
            "        {\n" +
            "            \"fallback\": \"%s\",\n" +
            "            \"color\": \"#808080\",\n" +
            "            \"pretext\": \"%s\",\n" +
            "            \"author_name\": \"Usage for command '%s'\",\n" +
            "            \"text\": \"%s\"\n" +
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
        sendJson("{\"text\":\""+text+"\"}");
    }
    /**
     * Sends a standard text message to set up a new room for when they have tried to submit a command on a non-configured room.
     */
    public void errorSendSetupNewRoom()
    {
        sendJson(String.format(ERROR_SETUP_NEW_ROOM, TravisAlertsApplication.config.clientID()));
    }
    /**
     * Sends a standard text message to set up a new room
     */
    public void sendSetupNewRoom()
    {
        sendJson(String.format(SETUP_NEW_ROOM, TravisAlertsApplication.config.clientID()));
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
    public void sendRepoBranchAlreadyBeingPolled(String repo, String branch)
    {
        String title = String.format("Travis Alerts was unable to poll %s@%s.", repo, branch);
        String description = String.format("The requested repo/branch is already being polled for this channel. ");
        sendJson(String.format(ERROR_PROCESSING, title, description, FAILED_COLOUR));
    }
    /**
     * Sends a standard error message for if the polling service returned 404 not found.
     */
    public void sendInvalidParameters(String usage)
    {
        sendJson(String.format(ERROR_PROCESSING, INVALID_PARAMETERS, usage, FAILED_COLOUR));
    }

    public void sendUsageWithDescription(String description, String command, String usage)
    {
        sendJson(String.format(USAGE_WITH_DCESCRIPTION, description,description, command, usage));
    }
    @Bean
    public void sendJson(String json) throws HttpClientErrorException
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();

        ObjectMapper om = new ObjectMapper();

        HttpEntity<String> request = new HttpEntity<>(json, headers);
        restTemplate.exchange(URL, HttpMethod.POST, request, String.class);
    }
    public String getURL()
    {
        return URL;
    }
}