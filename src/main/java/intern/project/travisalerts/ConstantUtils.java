package intern.project.travisalerts;

public class ConstantUtils {
    public static final String FIRST_TIME_CONFIG_RESPONSE = "Hello! Im Travis Alerts. I keep this channel notified of any specified GitHub branches that are in failed state on Travis-CI. To get started, type addbranch repo branch to add a new branch.";
    public static final String MISSING_ENV_VARIABLE = "CRITICAL ERROR!\nCRITICAL ERROR!\nCRITICAL ERROR!\nCRITICAL ERROR!\n This application is missing %s. Check your environment variables! This application will not function as intended without this. ";
    public static final String ENV_CLIENT_ID = "TRAVIS_ALERTS_CLIENT_ID";
    public static final String ENV_CLIENT_SECRET = "TRAVIS_ALERTS_CLIENT_SECRET";
    public static final String ENV_TRAVIS_TOKEN = "TRAVIS_TOKEN";

}