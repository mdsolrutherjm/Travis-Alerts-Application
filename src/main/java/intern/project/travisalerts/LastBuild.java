package intern.project.travisalerts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;
import java.util.Date;
@JsonIgnoreProperties(ignoreUnknown = true)
public class LastBuild {

    @JsonProperty("@type")
    String type;
    @JsonProperty("@href")
    String href;
    @JsonProperty("@representation")
    String representation;
    @JsonProperty
    int id;
    @JsonProperty
    int number;
    @JsonProperty
    String state;
    @JsonProperty
    int duration;
    @JsonProperty
    String event_type;
    @JsonProperty
    String previous_state;
    @JsonProperty
    String pull_request_title;
    @JsonProperty
    String pull_request_number;
    @JsonProperty @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    Date started_at;
    @JsonProperty @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    Date finished_at;
    @JsonProperty("private")
    boolean priv;

}
