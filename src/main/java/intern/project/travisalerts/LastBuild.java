package intern.project.travisalerts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;
import java.util.Date;

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
//
//    @JsonCreator
//    public LastBuild(
//            @JsonProperty("@type") String type,
//            @JsonProperty("@href") String href,
//            @JsonProperty("@representation") String representation,
//            int id,
//            int number,
//            String state,
//            int duration,
//            String event_type,
//            String previous_state,
//            String pull_request_title,
//            String pull_request_number,
//            LocalDateTime started_at,
//            LocalDateTime finished_at,
//            @JsonProperty("private") boolean priv) {
//        this.type = type;
//        this.href = href;
//        this.representation = representation;
//        this.id = id;
//        this.number = number;
//        this.state = state;
//        this.duration = duration;
//        this.event_type = event_type;
//        this.previous_state = previous_state;
//        this.pull_request_title = pull_request_title;
//        this.pull_request_number = pull_request_number;
//        this.started_at = started_at;
//        this.finished_at = finished_at;
//        this.priv = priv;
//
//    }

}
