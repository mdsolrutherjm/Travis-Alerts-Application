package intern.project.travisalerts;

import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class MainService implements Runnable {



    //TRAVIS AUTHORIZATION
    static Map<String, String> env = System.getenv();
    private static String TRAVIS_AUTH_TOKEN = env.get(ConstantUtils.ENV_TRAVIS_TOKEN);

    //SLACK ROOM. This is used to send messages to the Slack channel associated with this MainService thread.
    PollingRecord pollingRecord;
    //ENCODING
    private final String URL_ENCODING = "UTF-8";

    //BRANCH/REPO
    String repo, branch = "";
    SlackNotifier sn;

    /**
     * this constructor is called for get_status commands
     * @param repo
     * @param branch
     * @param sn
     */
    public MainService(String repo, String branch, SlackNotifier sn)
    {
        this.repo = repo;
        this.branch = branch;
        this.sn = sn;
    }
    /**
     * For repeating polling.
     */
    public MainService(PollingRecord pr)
    {
        this.pollingRecord = pr;
        repo = pr.repo;
        branch = pr.branch;
        sn = pr.sn;

        //Check if we have a Travis token. If not, send an error message.
        if (TRAVIS_AUTH_TOKEN == null)
        {
            System.out.println(
                    String.format(ConstantUtils.MISSING_ENV_VARIABLE, ConstantUtils.ENV_TRAVIS_TOKEN));
        }
    }

    /**
     * Ran when the thread is started.
     * Will keep polling until 'running' is false.
     */
    public void run()
    {

        while (pollingRecord.active)
        {
            try
            {
                pollingRecord.active = pollAndNotify();
                Thread.sleep(pollingRecord.poll);
            }
            catch(InterruptedException e)
            {
                System.out.println("FATAL: Thread Interrupted during cool-off period. Terminating...");
                System.exit(1);
            }
        }
    }

    /**
     * Polls Travis, returns to Slack with passed/failed state.
     * @return false if a fatal error occurred.
     */
    public boolean pollAndNotify()
    {
        try
        {
            String JsonObject = getAPIStringResponse(repo, branch);
            Branch branch = JsonUtils.deserializeJson(JsonObject);
            String buildURLTemplate = "https://travis-ci.com/%s/builds/%d";
            String branchURL = String.format(buildURLTemplate, branch.repository.slug, branch.lastBuild.id);
            if (branch.lastBuild.state.equals("passed")) {
                sn.sendPassed(branch.lastBuild.number, branch.repository.slug, branch.name, "Jack Gannon", branch.lastBuild.started_at.toString(), branchURL );
            }
            else if (branch.lastBuild.state.equals("failed")) {
                sn.sendFailed(branch.lastBuild.number, branch.repository.slug, branch.name, "Jack Gannon", branch.lastBuild.started_at.toString(), branchURL);
            }
        }
        catch(HttpClientErrorException|UnsupportedEncodingException e)
        {
            sn.sendRepoBranchNotFound(repo, branch, e.toString());
            return false;
        }
        return true;
    }
    /**
     * Uses the parameters to get the JSON String representing the state of the identified build.
     * @param repo repo to poll.
     * @param branch branch to poll
     * @return JSON response
     * @throws HttpClientErrorException thrown when an error (e.g. 404) occurs whilst trying to find the build.
     */
    @Bean
    public String getAPIStringResponse(String repo, String branch) throws HttpClientErrorException, UnsupportedEncodingException, ResourceAccessException
    {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();


        repo = URLEncoder.encode(repo, URL_ENCODING);
        branch = URLEncoder.encode(branch, URL_ENCODING);

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
