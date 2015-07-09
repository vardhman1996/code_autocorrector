import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.HashSet;
import java.util.regex.Pattern;

/**
 * Created by VardhmanMehta on 09/07/15.
 */
public class Validation {
    private static Pattern accoundID = Pattern.compile("^(\\w\\w\\w)-(\\w\\w\\w)-(\\w\\w\\w\\w)$");
    private static Pattern accountToken = Pattern.compile("^(\\w\\w\\w)-(\\w\\w\\w)$");
    private HashSet<String> usesSet;

    public Validation() {
        usesSet = new HashSet<>();
    }

    // Validate application node attributes
    public void validateAndroidName(Node applicationNode) {
        NamedNodeMap attributesMap = applicationNode.getAttributes();
        if (attributesMap.getNamedItem("android:name") != null && attributesMap.getNamedItem("android:name").getNodeValue().equals("com.wizrocket.android.sdk.Application")) {
            System.out.println("Android Name recognized");
        } else {
            System.out.println("Android Name not recognized");
        }
    }
    

}
