import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by VardhmanMehta on 23/07/15.
 */
public class CodeValidator {
    private String filePath;

    public CodeValidator(String filePath) {
        this.filePath = filePath;
    }

    public void validate() {
        Scanner scanner = null;
        try {
            File file = new File(filePath);
            scanner = new Scanner(file);
        } catch (FileNotFoundException | ArrayIndexOutOfBoundsException e) {
            System.out.println("File Not found");
            System.exit(0);
        }
        ValidateLine validateLine = new ValidateLine();
        while(scanner.hasNextLine()) {
            validateLine.checkLines(scanner.nextLine());
        }

        if(validateLine.isBasicInitialized()) {
            System.out.println("[  OKAY  ] Library initialization");
        } else {
            System.out.println("[  FAIL  ] Library initialization");
        }

        if(validateLine.isDeepLinkInitialized()) {
            System.out.println("[  OKAY  ] Deep link integration");
        } else {
            System.out.println("[  FAIL  ] Deep link integration");
        }

        if(validateLine.isPushSupportConfigured() && validateLine.isPushSupportHandler()) {
            System.out.println("[  OKAY  ] Push integration");
        } else {
            System.out.println("[  FAIL  ] Push integration");
        }
    }

    public void idValidation(String pFilePath) {
        Scanner scanner = null;
        try {
            File file = new File(pFilePath);
            scanner = new Scanner(file);
        } catch (FileNotFoundException | ArrayIndexOutOfBoundsException e) {
            System.out.println("File Not found");
            System.exit(0);
        }
        ValidateLine validateLine = new ValidateLine();
        while(scanner.hasNextLine()) {
            String curLine = scanner.nextLine();
            if(curLine.contains("WizRocketAccountID")) {
                validateLine.checkAccountID(scanner.nextLine());
            }
            if(curLine.contains("WizRocketToken")) {
                validateLine.checkAccountID(scanner.nextLine());
            }
        }

        if(validateLine.isAccountID() && validateLine.isAccountToken()) {
            System.out.println("[  OKAY  ] Account Credentials");
        } else {
            System.out.println("[  FAIL  ] Account Credentials");
        }
    }
}
