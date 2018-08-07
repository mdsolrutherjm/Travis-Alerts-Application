package intern.project.travisalerts;

import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainService {
    //--- API ADDRESS.
    private static final String REPO_URL = "https://api.travis-ci.com/repo/{repoName}/branch/{branchname}";

    //--- REPO AND BRANCH TO POLL.
    private static final String REPO_IDENTIFIER = "3521753";
    private static final String BRANCH_NAME = "develop";

    //AUTHORISATION
    static Map<String, String> env = System.getenv();

    private static final String TRAVIS_AUTH_TOKEN = env.get("TRAVIS_TOKEN");

    public MainService()
    {
        System.out.println(getAPIStringResponse());

    }
    public String getAPIStringResponse()
    {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        /**
         * REPO_IDENTIFIER currently cannot be the repo slug as it will return a 404 not found.
         * must be an ID for now (look at later?)
         */
        UriComponents uri = UriComponentsBuilder
                .fromHttpUrl("https://api.travis-ci.com/repo/{repoName}/branch/{branchname}")
                .buildAndExpand(REPO_IDENTIFIER, BRANCH_NAME);

        //Set Headers for request.
        headers.set("Authorization", "token " + TRAVIS_AUTH_TOKEN);
        headers.set("Travis-API-Version","3");
        headers.set("User-Agent", "application/API Explorer");
        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity response = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, String.class);

        return response.toString();
    }
}
