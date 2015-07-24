import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by VardhmanMehta on 23/07/15.
 */
public class ValidateLine {
    private static Pattern basicInitialization = Pattern.compile("\\[WizRocket[\\s]*notifyApplicationLaunchedWithOptions[\\s]*:[\\s]*[\\w]*[\\s]*\\][\\s]*;");
    private static Pattern deepLinkIntrgration = Pattern.compile("\\[WizRocket[\\s]*handleOpenURL[\\s]*:[\\s]*url[\\s]*sourceApplication[\\s]*:[\\s]*[\\w]*[\\s]*\\][\\s]*;");
    private static Pattern pushSupportHandler = Pattern.compile("\\[WizRocket[\\s]*handleNotificationWithData[\\s]*:[\\s]*[\\w]*[\\s]*\\][\\s]*;");
    private static Pattern pushSupport = Pattern.compile("\\[WizRocket[\\s]*setPushToken[\\s]*:[\\s]*[\\w]*[\\s]*\\][\\s]*;");
    private static Pattern accoundID = Pattern.compile("(\\w\\w\\w)-(\\w\\w\\w)-(\\w\\w\\w\\w)");
    private static Pattern accountToken = Pattern.compile("(\\w\\w\\w)-(\\w\\w\\w)");

    private boolean gotBasicInitialization = false;
    private boolean gotDeepLinkIntegration = false;
    private boolean gotPushSupportHandler = false;
    private boolean gotPushSupport = false;
    private boolean gotAccountID = false;
    private boolean gotAccountToken = false;

    private int count = 0;

    public void checkLines(String line) {
        Matcher m;
        m = basicInitialization.matcher(line);
        if (m.find()) {
            gotBasicInitialization = true;
        }

        m.reset();

        m = deepLinkIntrgration.matcher(line);
        if (m.find()) {
            gotDeepLinkIntegration = true;
        }

        m.reset();

        m = pushSupport.matcher(line);
        if (m.find()) {
            gotPushSupport = true;
        }

        m.reset();

        m = pushSupportHandler.matcher(line);
        if (m.find()) {
            count++;
            if (count == 4) {
                gotPushSupportHandler = true;
            }
        }
    }
    
    public void checkAccountID(String line) {
        Matcher m;
        m = accoundID.matcher(line);
        if(m.find()) {
            gotAccountID = true;
        }

        m.reset();

        m = accountToken.matcher(line);
        if(m.find()) {
            gotAccountToken = true;
        }
    }

    public boolean isBasicInitialized() {
        return gotBasicInitialization;
    }

    public boolean isDeepLinkInitialized() {
        return gotDeepLinkIntegration;
    }

    public boolean isPushSupportHandler() {
        return gotPushSupportHandler;
    }

    public boolean isPushSupportConfigured() {
        return gotPushSupport;
    }

    public boolean isAccountID() {
        return gotAccountID;
    }

    public boolean isAccountToken() {
        return gotAccountToken;
    }

}
