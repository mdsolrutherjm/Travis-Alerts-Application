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

        }
        //System.out.println(branch.name + branch.defaultBranch +branch.existsOnGithub);
        return branch;
    }
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
