package intern.project.travisalerts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IncomingWebhook {
    @JsonProperty("channel")
    String channel;
    @JsonProperty("channel_id")
    String channelID;
    @JsonProperty("configuration_url")
    String configurationURL;
    @JsonProperty("url")
    String channelURL;
}
