public class Validator{
	public static void main(String[] args) {
		String filePath = null;
		try {
			filePath = args[1];
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Enter the file Path");
			System.exit(0);
		}

		if(args[0].equals("-android")) {
			WizRocket_Validator validator = new WizRocket_Validator(filePath);
			validator.validate();
		} else if(args[0].equals("-ios")) {
			CodeValidator validator = new CodeValidator(filePath);
			validator.validate();
            try{
                validator.idValidation(args[2]);
            } catch(ArrayIndexOutOfBoundsException e) {
                System.out.println("[  FAIL  ] Failed to enter the info.plist file.");
                System.exit(0);
            }
		} else {
            System.out.println("Please specify the operating system");
        }
	}
}