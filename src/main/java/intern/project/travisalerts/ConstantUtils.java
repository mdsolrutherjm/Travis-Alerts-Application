package intern.project.travisalerts;

public class ConstantUtils {
    //usages
    public static final String USAGE_START_POLLING = "/startpolling [repo] [branch] [minutes]";

    public static final String FIRST_TIME_CONFIG_RESPONSE = "Hello! I'm Travis Alerts. I keep this channel notified of any specified GitHub branches that are in failed state on Travis-CI. To get started, type the command below.";
    public static final String ENV_CLIENT_ID = "TRAVIS_ALERTS_CLIENT_ID";
    public static final String ENV_CLIENT_SECRET = "TRAVIS_ALERTS_CLIENT_SECRET";
    public static final String ENV_TRAVIS_TOKEN = "TRAVIS_TOKEN";
    public static final String MISSING_ENV_VARIABLE = ("CRITICAL ERROR!This application is missing environment variables. We require " + ENV_CLIENT_ID + ", " + ENV_CLIENT_SECRET + "," + ENV_TRAVIS_TOKEN);
    public static final String INVALID_TIME_PARAMETER = "The [minute] parameter was not valid. \n" + USAGE_START_POLLING;
    public static final String TERMINATING_POLLING = "Terminating %s@%s";
    public static final String TERMINATING_POLLING_ERROR = "Could not terminate %s@%s. Are you sure it was configured to be polled? ";

}