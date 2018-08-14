package intern.project.travisalerts;
// READ http://www.baeldung.com/jackson-annotations
//3. Jackson Deserialization Annotations

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sun.applet.Main;

public class Branch {

    @JsonProperty("name")
    String name;
    @JsonProperty("exists_on_github")
    boolean existsOnGithub;
    @JsonProperty("default_branch")
    boolean defaultBranch;
    @JsonProperty("repository")
    Repository repository;
    @JsonProperty("last_build")
    LastBuild lastBuild;

    //public Branch(String name, boolean exists_on_github, String defaultBranch, Repository repository  )

}
