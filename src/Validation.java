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
    private Map<String, String> ra;
    private String userPackage;

    public Validation(String userPackage) {
        usesSet = new HashSet<>();
        nodeValidator = new NodeValidator();
        ra = new HashMap<>();
        this.userPackage = userPackage;
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

    public void validateRequiredReceiver(Node applicationNode) {
        ra.clear();
        ra.put("android:name", "com.wizrocket.android.sdk.InstallReferrerBroadcastReceiver");
        ra.put("android:exported", "true");
        Node receiver = nodeValidator.contains(applicationNode, "receiver", ra);

        if (receiver == null) {
            System.out.println("referral tracking receiver tag not configured correctly");
            return;
        }
//        if(receiver!=null) {
//            System.out.println("Receiver tag configured correctly");
//        } else {
//            System.out.println("Receiver tag configured correctly");
//        }
        NodeList receiverChildren = receiver.getChildNodes();
        ra.clear();
        ra.put("android:name", "com.android.vending.INSTALL_REFERRER");
        Node actionNode = getNode("intent-filter", "action", ra, receiverChildren);


        if (actionNode != null) {
            System.out.println("Referral tracking receiver configured correctly");
        } else {
            System.out.println("Action tag configured incorrectly");
        }

    }

    public void validateGcmReceiver(Node applicationNode, NodeList usesPermList, NodeList gcmPermList) {
        ra.clear();
        ra.put("android:name", "com.wizrocket.android.sdk.GcmBroadcastReceiver");
        ra.put("android:permission", "com.google.android.c2dm.permission.SEND");
        Node gcmReceiver = nodeValidator.contains(applicationNode, "receiver", ra);

        if (gcmReceiver == null) {
            System.out.println("Push Notifications receiver incorrect");
            return;
        }

        NodeList gcmReceiverChildren = gcmReceiver.getChildNodes();

        ra.clear();
        ra.put("android:name", "com.google.android.c2dm.intent.RECEIVE");
        Node firstActionNode = getNode("intent-filter", "action", ra, gcmReceiverChildren);


        ra.clear();
        ra.put("android:name", "com.google.android.c2dm.intent.REGISTRATION");
        Node secondActionNode = getNode("intent-filter", "action", ra, gcmReceiverChildren);

        ra.clear();
        ra.put("android:name", userPackage);
        Node category = getNode("intent-filter", "category", ra, gcmReceiverChildren);
        
        boolean matchedAll = false;

        for (int i = 0; i < usesPermList.getLength(); i++) {
            Node tempUsesItem = usesPermList.item(i);
            NamedNodeMap usesMap = tempUsesItem.getAttributes();
            usesSet.add(usesMap.getNamedItem("android:name").getNodeValue());
        }

        for (int i = 0; i < gcmPermList.getLength(); i++) {
            Node tempGcmPermItem = gcmPermList.item(i);
            NamedNodeMap gcmPermMap = tempGcmPermItem.getAttributes();
            usesSet.add(gcmPermMap.getNamedItem("android:name").getNodeValue());
            usesSet.add(gcmPermMap.getNamedItem("android:protectionLevel").getNodeValue());
        }


        if (usesSet.contains("signature") && usesSet.contains(userPackage + ".permission.C2D_MESSAGE") && usesSet.contains("com.google.android.c2dm.permission.RECEIVE")) {
            matchedAll = true;
        } else {
            System.out.println("Uses permissions for GCM not configured correctly");
        }
        ra.clear();
        ra.put("android:name", "GCM_SENDER_ID");
        Node firstMeta = nodeValidator.contains(applicationNode, "meta-data", ra);
        if (firstMeta == null) {
            System.out.println("GCM_ID not configured");
        }
        ra.clear();
        ra.put("android:name", "com.google.android.gms.version");
        ra.put("android:value", "@integer/google_play_services_version");

        Node secondMeta = nodeValidator.contains(applicationNode, "meta-data", ra);
        if (secondMeta == null) {
            System.out.println("Second Meta not configured correctly");
        }

        if (matchedAll && firstActionNode != null && secondActionNode != null && category != null) {
            System.out.println("GCM configured");
        } else {
            System.out.println("GCM not configured");
        }
    }


    private Node getNode(String innerTag, String tagName, Map<String, String> newRa, NodeList nodeList) {
        Node node = null;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childrenItem = nodeList.item(i);
            if (!childrenItem.getNodeName().equals(innerTag)) continue;
            node = nodeValidator.contains(childrenItem, tagName, newRa);
        }
        return node;
    }
}
