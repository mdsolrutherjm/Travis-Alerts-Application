package intern.project.travisalerts;

public class ConstantUtils {
    //FILE NAMES
    public static final String FILE_POLLING_RECORD = "pollingRecords.csv";
    public static final String FILE_CHANNEL_RECORD = "channelRecords.csv";

    //POLLING LIMIT
    public static final int LOWER_POLLING_LIMIT = 2; //This sets the polling limit so that it cannot be any lower than this value.

    //BOOT-up polling services initation cooldown
    public static final int POLLING_SVC_START_COOLDOWN = 20; //milliseconds between starting each thread.

    //ENVIRONMENT VARIABLES - NAMES.
    public static final String ENV_CLIENT_ID = "TRAVIS_ALERTS_CLIENT_ID";
    public static final String ENV_CLIENT_SECRET = "TRAVIS_ALERTS_CLIENT_SECRET";
    public static final String ENV_TRAVIS_TOKEN = "TRAVIS_TOKEN";


    //ENVIRONMENT VARIABLES - ERRORS.
    public static final String MISSING_ENV_VARIABLE = ("CRITICAL ERROR!This application is missing environment variables. We require " + ENV_CLIENT_ID + ", " + ENV_CLIENT_SECRET + "," + ENV_TRAVIS_TOKEN);


    //COMMAND USAGES
    public static final String USAGE_START_POLLING = "/startpolling [repo] [branch] [minutes]";
    public static final String USAGE_STOP_POLLING = "/stoppolling [repo] [branch]";

    public static final String USAGE_GET_STATUS = "/getstatus [repo] [branch]";
    public static final String FIRST_TIME_CONFIG_RESPONSE = "Hello! I'm Travis Alerts. I keep this channel notified of any specified GitHub branches that are in failed state on Travis-CI. To get started, type the command below.";


    //FAILED MESSAGES.
    public static final String FAILED_IOEX_READING_FILE = "ERROR: Encountered IO Exception when reading from %s. Full error is provided below. \n(%s)";
    public static final String FAILED_IOEX_WRITING_FILE = "ERROR: Encountered IO Exception when writing to %s. Full error is provided below. \n(%s)";
    public static final String FAILED_MISSING_FILE = "ERROR: Could not find %s. This is OK if this is a clean-boot. ";
    public static final String TERMINATING_POLLING_ERROR = "Could not terminate %s@%s. Are you sure it was configured to be polled? ";
    public static final String INVALID_TIME_PARAMETER = "The [minute] parameter must be a numerical, whole value greater than '2'. \n" + USAGE_START_POLLING;
    public static final String FAILED_INTERVAL_START_UP_POLLING_SVCS = "ERROR: Travis Alerts was unable to start up all of the polling service threads. \n%s";

    //OTHER
    public static final String TERMINATING_POLLING = "Terminating %s@%s";
    public static final String STARTING_LOGO = "\n" +
            "  _______                  _                  _              _        \n" +
            " |__   __|                (_)          /\\    | |            | |       \n" +
            "    | | _ __  __ _ __   __ _  ___     /  \\   | |  ___  _ __ | |_  ___ \n" +
            "    | || '__|/ _` |\\ \\ / /| |/ __|   / /\\ \\  | | / _ \\| '__|| __|/ __|\n" +
            "    | || |  | (_| | \\ V / | |\\__ \\  / ____ \\ | ||  __/| |   | |_ \\__ \\\n" +
            "    |_||_|   \\__,_|  \\_/  |_||___/ /_/    \\_\\|_| \\___||_|    \\__||___/\n" +
            "                                                                      \n" +
            "                                                                      \n";
    public static final String AUTHOR = "An intern project by Jack Gannon and James Rutherford\n\n\n";
}