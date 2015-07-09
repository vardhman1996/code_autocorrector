import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Map;

/**
 * Created by VardhmanMehta on 09/07/15.
 */
public class NodeValidator {
    /**
     * @param root
     * @param tagName
     * @param ra
     * @return
     */
    public static Node contains(Node root, String tagName, Map<String, String> ra) {
        NodeList children = root.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node item = children.item(i);
            if (!item.getNodeName().equals(tagName)) continue;

            // We have found a possible node
            boolean matchedAll = true;
            for (String attr : ra.keySet()) {
                NamedNodeMap nodeAttributes = item.getAttributes();
                Node attributeValue = nodeAttributes.getNamedItem(attr);
                if (!ra.get(attr).equals(attributeValue.getNodeValue())) {
                    matchedAll = false;
                    break;
                }
            }

            if (matchedAll) {
                return item;
            }
        }
        return null;
    }
}
