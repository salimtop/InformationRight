import java.util.*;


public class ModelViewControllerConsole {

	public static void main(String[] args)  {
		// Connect to database
		connectToDatabase();

		
		// Model View Controller (MVC)
		// Router knows all the controllers
		Map<String, Controller> router = new HashMap<>();

		router.put("MainMenu", new Controller(new MainMenuView(), new NopModel()));
		router.put("Department", new Controller(new DepartmentView(), new DepartmentModel()));
		router.put("Login", new Controller(new LoginView(), new LoginModel()));
		router.put("Screen",new Controller(new ScreenView(), new ScreenModel() ));
		router.put("Application",new Controller(new ApplicationView(),new ApplicationModel()));
		router.put("ApplicationForm",new Controller(new ApplicationFormView(),new ApplicationFormModel()));
		router.put("Applier",new Controller(new ApplierView(), new ApplierModel()));
		router.put("TypeTable",new Controller(new TypeTableView(), new TypeTableModel()));
		router.put("Admission",new Controller(new AdmissionView(), new AdmissionModel()));
		router.put("Institution", new Controller(new InstitutionView(), new InstitutionModel()));
		router.put("Respond",new Controller(new RespondView(),new RespondModel()));
		router.put("Inquiry",new Controller(new InquiryView(), new InquiryModel()));

		ViewData viewData = new ViewData("MainMenu", "");
		do {
			try {

				// Model, View, and Controller
				Controller controller = router.get(viewData.functionName);
				ModelData modelData = null;

				modelData = controller.executeModel(viewData);

				viewData = controller.getView(modelData, viewData.functionName, viewData.operationName);

				System.out.println();
				System.out.println("-------------------------------------------------");
				System.out.println();
			} catch (Exception e) {
				e.printStackTrace();
				viewData = new ViewData("MainMenu", "");

			}
		}
		while (viewData.functionName != null);

		System.out.println();
		System.out.println();
		System.out.println("Program is ended...");


		// Disconnect from database
		disconnectFromDatabase();
	}

	
	public static void connectToDatabase() {
		DatabaseUtilities.host = "localhost:63664";
		DatabaseUtilities.databaseName = "Group1";
		
		try {
			DatabaseUtilities.getConnection();
		}
		catch(Exception e) {
			System.out.println("Exception occured : " + e);
			return;
		}		
	}
	
	public static void disconnectFromDatabase() {
		try {
			DatabaseUtilities.disconnect();
		}
		catch(Exception e) {
			System.out.println("Error disconnecting from database : " + e);
			return;
		}		
	}
	
}
