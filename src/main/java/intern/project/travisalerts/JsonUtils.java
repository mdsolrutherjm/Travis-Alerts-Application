package intern.project.travisalerts;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;


public class JsonUtils {

    public static Branch deserializeJson(String inputJson){
        ObjectMapper objectMapper = new ObjectMapper();
        Branch branch = new Branch();
        try {
            branch = objectMapper.readValue(inputJson, Branch.class);
        }
        catch (IOException e){
            System.out.println(e);
        }
        return branch;
    }

    /**
     * This method interprets the JSON response that Slack sends us when we are authenticating a new room and puts it into a SlackAuthJSON object.
     * @param inputJson the JSON sent by Slack.
     * @return the object which represents the inputted JSON.
     */
    public static SlackAuthJSON deserializeSlackAuth(String inputJson){
        ObjectMapper objectMapper = new ObjectMapper();
        SlackAuthJSON slackAuth = new SlackAuthJSON();
        try {
            slackAuth = objectMapper.readValue(inputJson, SlackAuthJSON.class);
        }
        catch (IOException e){
            System.out.println(e);
        }
        return slackAuth;
    }
}
