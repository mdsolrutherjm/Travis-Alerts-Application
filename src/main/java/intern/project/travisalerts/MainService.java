package intern.project.travisalerts;

import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
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
     * constructor
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
     * constructor
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
                //String JsonObject = getAPIStringResponse(repoIdentifier, branchName);
//                Branch branch = JsonUtils.deserializeJson(JsonObject);
//                System.out.println(branch.name);
                slackAPI.sendText(getAPIStringResponse(repoIdentifier, branchName));
            }
            catch(HttpClientErrorException e)
            {
                /**
                 * getApiStringResponse() returns client error if the content is unavailable. check for this when we put it in a loop.
                 */
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

        /**
         * REPO_IDENTIFIER currently cannot be the repo slug as it will return a 404 not found.
         * must be an ID for now (look at later?)
         */
        UriComponents uri = UriComponentsBuilder
                .fromHttpUrl(REPO_URL)
                .buildAndExpand(repo, branch);

        //Set Headers for request.
        headers.set("Authorization", "token " + TRAVIS_AUTH_TOKEN);
        headers.set("Travis-API-Version","3");
        headers.set("User-Agent", "application/API Explorer");
        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<String> response = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, String.class);

        return response.getBody();
    }
}
