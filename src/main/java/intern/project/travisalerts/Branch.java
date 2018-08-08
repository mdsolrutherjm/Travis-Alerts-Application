package intern.project.travisalerts;
// READ http://www.baeldung.com/jackson-annotations
//3. Jackson Deserialization Annotations

import com.fasterxml.jackson.annotation.JsonProperty;

public class Branch {
    String name;
    boolean exists_on_github;
    String defaultBranch;
    Repository repository;
    LastBuild lastBuild;




}
