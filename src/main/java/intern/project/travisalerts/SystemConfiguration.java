package intern.project.travisalerts;

import java.util.Map;
/**
 * Holds all of the tokens required for the application to run properly.
 */
public class SystemConfiguration {
    private static String TRAVIS_AUTH_TOKEN, CLIENT_ID, CLIENT_SECRET;

    /**
     * Constructor. Loads in all of the environment variables within the system and pulls out the ones which are relevant to us.
     */
    public SystemConfiguration()
    {
        Map<String, String> env = System.getenv();
        TRAVIS_AUTH_TOKEN = env.get(ConstantUtils.ENV_TRAVIS_TOKEN);
        CLIENT_ID = env.get(ConstantUtils.ENV_CLIENT_ID);
        CLIENT_SECRET = env.get(ConstantUtils.ENV_CLIENT_SECRET);
    }

    /**
     * @return the configured Travis token.
     */
    public String travisToken()
    {
        return TRAVIS_AUTH_TOKEN;
    }

    /**
     * @return the client ID of the Slack application.
     */
    public String clientID()
    {
        return CLIENT_ID;
    }

    /**
     * @return the client secret code for the Slack application.
     */
    public String clientSecret()
    {
        return CLIENT_SECRET;
    }
    /**
     * Checks that all the required Environment Variables are loaded into the system.
     * @return true if we are missing any environment variables.
     */
    public static boolean areAnyEnvironmentVariablesMissing()
    {

        if ((TRAVIS_AUTH_TOKEN == null) || (CLIENT_ID == null) || (CLIENT_SECRET == null))
        {
            return true; //Missing environment variables.
        }
        return false; //No missing environment variables
    }
}
