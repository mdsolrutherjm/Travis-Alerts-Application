package intern.project.travisalerts;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Branch {

    @JsonProperty("@type")
    String type;
    @JsonProperty("@href")
    String href;
    @JsonProperty("@representation")
    String representation;
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
}
