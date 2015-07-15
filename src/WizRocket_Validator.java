import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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
        Document dom;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse(file);
            Element docElm = dom.getDocumentElement();
            String userPackage = docElm.getAttribute("package");


            NodeList usesPermList = docElm.getElementsByTagName("uses-permission");
            NodeList gcmPermList = docElm.getElementsByTagName("permission");

            NodeList applicationList = docElm.getElementsByTagName("application");
            if (applicationList == null || applicationList.getLength() != 1) return;

            Node applicationNode = applicationList.item(0);

            Validation validateTags = new Validation(userPackage);
            validateTags.validateAndroidName(applicationNode, filePath);
            validateTags.validateUsesPermissions(usesPermList);
            validateTags.validateRequiredMeta(applicationNode);
            validateTags.validateRequiredReceiver(applicationNode);
            validateTags.validateGcmReceiver(applicationNode, usesPermList, gcmPermList);
            validateTags.validateInAppNotifications(applicationNode);

            if (usesPermList == null || usesPermList.getLength() < 1) return;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

    }

}
