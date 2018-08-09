package intern.project.travisalerts;
// READ http://www.baeldung.com/jackson-annotations
//3. Jackson Deserialization Annotations

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


public class Repository {
//    String type;
//    String href;
//    String representation;
//    int id;
    @JsonProperty
    String name;
//    String slug;

//    @JsonCreator
//    public Repository(
//            @JsonProperty("@type") String type,
//            @JsonProperty("@href") String href,
//            @JsonProperty("@representation") String representation,
//            int id,
//            String name,
//            String slug) {
//        this.type = type;
//        this.href = href;
//        this.representation = representation;
//        this.id = id;
//        this.name = name;
//        this.slug = slug;


}
