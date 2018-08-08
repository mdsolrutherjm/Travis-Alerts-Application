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
    private static final String REPO_URL = "https://api.travis-ci.com/repo/{repoName}/branch/{branchname}";

    //--- REPO AND BRANCH TO POLL.
    private String repoIdentifier;
    private String branchName;
    private long pollMs;
    //AUTHORISATION
    static Map<String, String> env = System.getenv();

    private static final String TRAVIS_AUTH_TOKEN = env.get("TRAVIS_TOKEN");

    /**
     * constructor
     * @param repo the ID/Slug of the repo to poll.
     * @param branch the name of the branch to poll.
     */
    public MainService(String repo, String branch, long pollMin)
    {
        this.repoIdentifier = repo;
        this.branchName = branch;
        this.pollMs = (pollMin * 60000);
    }

    public void run()
    {
        while (true)
        {
            try
            {
                System.out.println(getAPIStringResponse(repoIdentifier, branchName));
            }
            catch(HttpClientErrorException e)
            {
                /**
                 * getApiStringResponse() returns client error if the content is unavailable. check for this when we put it in a loop.
                 */
                System.out.println("Resource Unavailable.");
            }
            try
            {
                Thread.sleep(pollMs);
            }
            catch(InterruptedException e)
            {
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
