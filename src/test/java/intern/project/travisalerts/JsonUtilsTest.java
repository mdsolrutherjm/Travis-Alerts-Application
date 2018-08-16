package intern.project.travisalerts;

import org.junit.Test;

import static org.junit.Assert.*;

public class JsonUtilsTest {
    @Test
    public void testShouldCreateBranchFromJson(){

        String inputJson = "{\n" +
                " \"@type\": \"branch\",\n" +
                " \"@href\": \"/repo/3521753/branch/develop\",\n" +
                " \"@representation\": \"standard\",\n" +
                " \"name\": \"develop\",\n" +
                " \"repository\": {\n" +
                "   \"@type\": \"repository\",\n" +
                "   \"@href\": \"/repo/3521753\",\n" +
                "   \"@representation\": \"minimal\",\n" +
                "   \"id\": 3521753,\n" +
                "   \"name\": \"study_management\",\n" +
                "   \"slug\": \"mdsol/study_management\"\n" +
                " },\n" +
                " \"default_branch\": true,\n" +
                " \"exists_on_github\": true,\n" +
                " \"last_build\": {\n" +
                "   \"@type\": \"build\",\n" +
                "   \"@href\": \"/build/81919654\",\n" +
                "   \"@representation\": \"minimal\",\n" +
                "   \"id\": 81919654,\n" +
                "   \"number\": \"4355\",\n" +
                "   \"state\": \"started\",\n" +
                "   \"duration\": null,\n" +
                "   \"event_type\": \"push\",\n" +
                "   \"previous_state\": \"passed\",\n" +
                "   \"pull_request_title\": null,\n" +
                "   \"pull_request_number\": null,\n" +
                "   \"started_at\": \"2018-08-15T15:03:28Z\",\n" +
                "   \"finished_at\": null,\n" +
                "   \"private\": true\n" +
                " }\n" +
                "}";

        Branch branch = JsonUtils.deserializeJson(inputJson);
        assertEquals("develop", branch.name);
        assertEquals(true, branch.existsOnGithub);
        assertEquals(true, branch.defaultBranch);
        assertEquals("build", branch.lastBuild.type);
        assertEquals("/build/81919654", branch.lastBuild.href);
        assertEquals("minimal", branch.lastBuild.representation);
        assertEquals(81919654, branch.lastBuild.id);
        assertEquals(0, branch.lastBuild.duration);
        assertEquals("push", branch.lastBuild.event_type);
        assertEquals("mdsol/study_management", branch.repository.slug);
        assertEquals("repository",branch.repository.type);
        assertEquals(3521753, branch.repository.id);
        assertEquals("study_management", branch.repository.name );
        assertEquals("/repo/3521753", branch.repository.href);
        assertEquals("minimal",branch.repository.representation);
        assertEquals("branch", branch.type);


    }

}