import java.util.*;

class MainMenuView implements ViewInterface {

	@Override
	public ViewData create(ModelData modelData, String functionName, String operationName) throws Exception {

		Integer choice;
		do {
			System.out.println("1. New Application");
			System.out.println("2. Application Inquiry");
			System.out.println("3. Personnel Login");
			System.out.println("4. Quit");
			System.out.println();

			choice = getInteger("Enter your choice : ", false);
		} 
		while (choice == null || choice < 1 || choice > 4);
		
		
		Map<String, Object> userInput = new HashMap<>();
		userInput.put("mainMenuChoice", choice);
		
		switch (choice.intValue()) {
			case 1: return new ViewData("Application","newApplication");
			case 2: operationName = "select.gui";	break;
			case 3: operationName = "login.gui";
				return new ViewData("Login", operationName, new HashMap<>());
			case 4: operationName = "update.gui";	break;
			case 5: operationName = "delete.gui";	break;
			default: return new ViewData(null, null);
		}
		
		return new ViewData("Department", operationName, new HashMap<>());
	}

	@Override
	public String toString() {
		return "Main Menu View";
	}		
}
