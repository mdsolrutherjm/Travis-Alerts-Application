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
    @Test
    public void testReadingSlackAuth()
    {
        String inputJson = "{\n" +
                "    \"ok\": true,\n" +
                "    \"access_token\": \"xoxp-79629037168-389640243568-409400601460-b8a2c9cb11e2bf61f711ac8434707071\",\n" +
                "    \"scope\": \"identify,commands,incoming-webhook\",\n" +
                "    \"user_id\": \"UBFJU75GQ\",\n" +
                "    \"team_name\": \"mdsol\",\n" +
                "    \"team_id\": \"T2BJH134Y\",\n" +
                "    \"incoming_webhook\": {\n" +
                "        \"channel\": \"travis-project-test\",\n" +
                "        \"channel_id\": \"GC2U8DMFY\",\n" +
                "        \"configuration_url\": \"https://mdsol.slack.com/services/BCACVMU8J\",\n" +
                "        \"url\": \"https://hooks.slack.com/services/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\"\n" +
                "    }\n" +
                "}";
        SlackAuthJSON sa = JsonUtils.deserializeSlackAuth(inputJson);
        assertEquals(true, sa.ok);
        assertEquals("xoxp-79629037168-389640243568-409400601460-b8a2c9cb11e2bf61f711ac8434707071", sa.accessToken);
        assertEquals("identify,commands,incoming-webhook", sa.scope);
        assertEquals("UBFJU75GQ", sa.user_id);
        assertEquals("mdsol", sa.teamName);
        assertEquals("T2BJH134Y",sa.teamID);
        assertEquals("travis-project-test", sa.incomingWebhook.channel);
        assertEquals("GC2U8DMFY", sa.incomingWebhook.channelID);
        assertEquals("https://mdsol.slack.com/services/BCACVMU8J", sa.incomingWebhook.configurationURL);
        assertEquals("https://hooks.slack.com/services/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", sa.incomingWebhook.channelURL);


    }

}