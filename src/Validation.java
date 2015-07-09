import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by VardhmanMehta on 09/07/15.
 */
public class Validation {
    private static Pattern accoundID = Pattern.compile("^(\\w\\w\\w)-(\\w\\w\\w)-(\\w\\w\\w\\w)$");
    private static Pattern accountToken = Pattern.compile("^(\\w\\w\\w)-(\\w\\w\\w)$");
    private HashSet<String> usesSet;
    private NodeValidator nodeValidator;

    public Validation() {
        usesSet = new HashSet<>();
        nodeValidator = new NodeValidator();
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

    public void validateUsesPermissions(NodeList usesPermList) {
        for (int i = 0; i < usesPermList.getLength(); i++) {
            Node tempUsesItem = usesPermList.item(i);
            NamedNodeMap usesMap = tempUsesItem.getAttributes();
            usesSet.add(usesMap.getNamedItem("android:name").getNodeValue());
        }


        if (usesSet.contains("android.permission.READ_PHONE_STATE") && usesSet.contains("android.permission.INTERNET")) {
            System.out.println("Contains required uses permissions");
        } else {
            System.out.println("Does not contain required uses permissions");
        }
        if (!usesSet.contains("android.permission.ACCESS_NETWORK_STATE") ||
                !usesSet.contains("android.permission.GET_ACCOUNTS") ||
                !usesSet.contains("android.permission.ACCESS_COARSE_LOCATION") ||
                !usesSet.contains("android.permission.WRITE_EXTERNAL_STORAGE")) {

            System.out.println("Does not contain recommended uses permissions");
        } else {
            System.out.println("Does contain recommended uses permissions");
        }
    }

    public void validateRequiredMeta(Node applicationNode) {
        NodeList children = applicationNode.getChildNodes();
        int length = children.getLength();
        for (int i = 0; i < length; i++) {
            Node item = children.item(i);
            Matcher m;
            if (!item.getNodeName().equals("meta-data")) continue;
            NamedNodeMap metaAttr = item.getAttributes();

            if (metaAttr.getNamedItem("android:name").getNodeValue().equals("WIZROCKET_ACCOUNT_ID")) {
                m = accoundID.matcher(metaAttr.getNamedItem("android:value").getNodeValue());
                if (m.find()) {
                    System.out.println("Correct format of Account ID");
                } else {
                    System.out.println("Account ID not in the correct format");
                }
            }

            if (metaAttr.getNamedItem("android:name").getNodeValue().equals("WIZROCKET_TOKEN")) {
                m = accountToken.matcher(metaAttr.getNamedItem("android:value").getNodeValue());
                if (m.find()) {
                    System.out.println("Correct format of Account Token");
                } else {
                    System.out.println("Account Token not in the correct format");
                }
            }
        }
    }

    public void validateRequiredReceiver (Node applicationNode) {
        Map<String, String> ra = new HashMap<>();
        ra.put("android:name", "com.wizrocket.android.sdk.InstallReferrerBroadcastReceiver");
        ra.put("android:exported", "true");
        Node receiver = nodeValidator.contains(applicationNode, "receiver", ra);
        if(receiver!=null) {
            System.out.println("Receiver tag configured correctly");
        } else {
            System.out.println("Receiver tag configured correctly");
        }
    }
}
