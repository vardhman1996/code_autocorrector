import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Created by VardhmanMehta on 09/07/15.
 */
public class WizRocket_Validator {
    public static void main(String[] args) {
        String filePath = args[0];
        File file = new File(filePath);
        System.out.println(filePath);
        Document dom;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        String userPackage;
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse(file);
            Element docElm = dom.getDocumentElement();
            userPackage = docElm.getAttribute("package");
            NodeList usesPermList = docElm.getElementsByTagName("uses-permission");
            if (usesPermList == null || usesPermList.getLength() < 1) return;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

    }
}
