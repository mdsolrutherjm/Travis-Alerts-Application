package intern.project.travisalerts;

import com.sun.javafx.fxml.builder.URLBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class MainService implements Runnable {
    //--- API ADDRESS.
    private static final String REPO_URL = "https://api.travis-ci.com/repo/{repoName}/branch/{branchName}";

    //--- REPO AND BRANCH TO POLL.
    private String repoIdentifier;
    private String branchName;
    private long pollMs = 0;
    private boolean isRepeating;

    //AUTHORIZATION
    static Map<String, String> env = System.getenv();
    private static final String TRAVIS_AUTH_TOKEN = env.get("TRAVIS_TOKEN");

    //SLACK ROOM.
    SlackNotifier slackAPI;

    /**
     * For non-repeating polling.
     * @param repo the ID/Slug of the repo to poll.
     * @param branch the name of the branch to poll.
     */
    public MainService(String repo, String branch, SlackNotifier slack)
    {
        this.repoIdentifier = repo;
        this.branchName = branch;
        isRepeating = false;
        slackAPI = slack;
    }
    /**
     * For repeating polling.
     * @param repo the ID/Slug of the repo to poll.
     * @param branch the name of the branch to poll.
     * @param pollMin cool-down between polls.
     */
    public MainService(String repo, String branch, long pollMin, SlackNotifier slack)
    {
        this.repoIdentifier = repo;
        this.branchName = branch;
        this.pollMs = (pollMin * 60000);
        isRepeating = true;
        slackAPI = slack;
    }

    public void run()
    {
        boolean running = true;
        while (running)
        {
            running = isRepeating;
            try
            {
                String JsonObject = getAPIStringResponse(repoIdentifier, branchName);
                Branch branch = JsonUtils.deserializeJson(JsonObject);
                String buildURLTemplate = "https://travis-ci.com/%s/builds/%d";
                String branchURL = String.format(buildURLTemplate, branch.repository.slug, branch.lastBuild.id);
                if (branch.lastBuild.state.equals("passed")) {
                    slackAPI.sendPassed(branch.lastBuild.number, branch.repository.slug, branch.name, "Jack Gannon", branch.lastBuild.started_at.toString(), branchURL );
                }
                else if (branch.lastBuild.state.equals("failed")) {
                    slackAPI.sendFailed(branch.lastBuild.number, branch.repository.slug, branch.name, "Jack Gannon", branch.lastBuild.started_at.toString(), branchURL);
                }


            }
            catch(HttpClientErrorException e)
            {
                /**
                 * getApiStringResponse() returns client error if the content is unavailable. check for this when we put it in a loop.
                 */
                System.out.println(repoIdentifier + branchName + "\n" + e);
            }
            try
            {
                Thread.sleep(pollMs);
            }
            catch(InterruptedException e)
            {
                System.out.println("FATAL: Thread Interrupted during cool-off period. Terminating...");
                System.exit(1);
            }
        }

    }

    @Bean
    public String getAPIStringResponse(String repo, String branch) throws HttpClientErrorException
    {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        try
        {
            repo = URLEncoder.encode(repo, "UTF-8");
            branch = URLEncoder.encode(branch, "UTF-8");

        }
        catch (UnsupportedEncodingException e){}

        UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl("https://api.travis-ci.com").path("/repo/" + repo +"/branch/" + branch);
        UriComponents components = uri.build(true);

        //Set Headers for request.
        headers.set("Authorization", "token " + TRAVIS_AUTH_TOKEN);
        headers.set("Travis-API-Version","3");
        headers.set("User-Agent", "application/API Explorer");
        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<String> response = restTemplate.exchange(components.toUri(), HttpMethod.GET, entity, String.class);

        return response.getBody();
    }
}
