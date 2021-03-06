package intern.project.travisalerts;
// READ http://www.baeldung.com/jackson-annotations
//3. Jackson Deserialization Annotations

import com.fasterxml.jackson.annotation.JsonProperty;


public class Repository {
    @JsonProperty("@type")
    String type;
    @JsonProperty("@href")
    String href;
    @JsonProperty("@representation")
    String representation;
    @JsonProperty
    int id;
    @JsonProperty
    String name;
    @JsonProperty
    String slug;
}
