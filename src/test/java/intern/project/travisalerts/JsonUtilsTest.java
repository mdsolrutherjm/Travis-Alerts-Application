package intern.project.travisalerts;

import org.junit.Test;

import static org.junit.Assert.*;

public class JsonUtilsTest {
    @Test
    public void testShouldCreateBranchFromJson(){

        String inputJson = "{\n" +
                "  \"name\": \"develop\",\n" +
                "  \"default_branch\": true,\n" +
                "  \"exists_on_github\": true,\n" +
                "  \"repository\": {\n" +
                "    \"name\": \"banana\"\n" +
                "  },\n" +
                "  \"last_build\": {\n" +
                "    \"@type\": \"build\",\n" +
                "    \"@href\": \"/build/81247186\",\n" +
                "    \"@representation\": \"minimal\",\n" +
                "    \"id\": 81247186,\n" +
                "    \"number\": \"3946\",\n" +
                "    \"state\": \"passed\",\n" +
                "    \"duration\": 703,\n" +
                "    \"event_type\": \"push\",\n" +
                "    \"previous_state\": \"passed\",\n" +
                "    \"pull_request_title\": null,\n" +
                "    \"pull_request_number\": null,\n" +
                "    \"private\": true\n" +
                "  }\n" +
                "}";
        Branch branch = JsonUtils.deserializeJson(inputJson);
        assertEquals("develop", branch.name);
        assertEquals(true, branch.existsOnGithub);
        assertEquals(true, branch.defaultBranch);
        assertEquals("banana", branch.repository.name );
        assertEquals("build", branch.lastBuild.type);
        assertEquals("/build/81247186", branch.lastBuild.href);
        assertEquals("minimal", branch.lastBuild.representation);
        assertEquals(81247186, branch.lastBuild.id);
        assertEquals(703, branch.lastBuild.duration);
        assertEquals("push", branch.lastBuild.event_type);



    }

}