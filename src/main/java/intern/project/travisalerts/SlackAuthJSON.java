package intern.project.travisalerts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)

public class SlackAuthJSON {
    @JsonProperty("ok")
    boolean ok;
    @JsonProperty("access_token")
    String accessToken;
    @JsonProperty("scope")
    String scope;
    @JsonProperty("user_id")
    String user_id;
    @JsonProperty("team_name")
    String teamName;
    @JsonProperty("team_id")
    String teamID;
    @JsonProperty("incoming_webhook")
    IncomingWebhook incomingWebhook;

}
