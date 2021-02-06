import java.sql.ResultSet;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class ScreenView implements ViewInterface {

    @Override
    public ViewData create(ModelData modelData, String functionName, String operationName) throws Exception {

        Map<String,Object> transferData =modelData.transferData;

        switch(operationName){
            case "screen.gui": return screenMenu(modelData);
            case "loadScreen" : return loadScreen(modelData);
            case "select": return getScreen(modelData);

            //Database screens
            case "newApplication" : return new ViewData("Applier","registerApplier",modelData.transferData);
            case "listAssigned" : return new ViewData("Admission","listAssigned");
            case "listAllApplication" : return new ViewData("Application","listAllApplication",modelData.transferData);
            case "showApplication" : return new ViewData("ApplicationForm","showApplicationForm");
            case "showInstitution" : //transfer data contains arrive point of data
                return new ViewData("Institution","select",transferData == null ? new HashMap<>() : transferData );
            case "admitApplication" : return new ViewData("Admission","admit", (Map<String, Object>) modelData.transferData);


        }


        return new ViewData(null,null);
    }

    private void newApplicationGUI() {
    }

    private ViewData loadScreen(ModelData modelData) throws Exception {

        String username =  Login.getUsername();
        Map<String, Object> whereParameters = new HashMap<>();
        whereParameters.put("whereParameters",getWhereParameters());

        System.out.println("Redirecting to personnel panel...");

        return new ViewData("Screen", "select",whereParameters);

    }

    private ViewData getScreen(ModelData modelData) throws Exception {
        ResultSet resultSet = modelData.resultSet;

        if (resultSet != null) {
            int screenNumber = 1;
            HashMap<Integer, String> allScreens = new HashMap<>();
            while (resultSet.next()) {
                // Retrieve by column name
                String screen = resultSet.getString("ScreenType");

                allScreens.put(screenNumber++,screen);
            }
            allScreens.putIfAbsent(screenNumber,"Quit");
            resultSet.close();

            Login.setScreen(allScreens);
            return new ViewData("Screen", "screen.gui");
        }

        System.out.println("You do not have any authority.\nExiting...");
        return new ViewData("MainMenu", "");
    }


    private ViewData screenMenu(ModelData modelData) throws ParseException {
        HashMap<Integer,String> screen = Login.getScreen();

        if(screen == null) {
            System.out.println("Authentication error! ");
            return new ViewData("Login", "loadScreen");
        }
        int i = 1;
        for(i = 1; i <= screen.size(); i++)
            System.out.println(""+i+" "+Screen.getByColumnName(screen.get(i)));

        Integer choice;
        do{
            choice = getInteger("Enter choice:",false);
        }
        while(choice < 0 || choice > screen.size());


        return new ViewData("Screen", screen.get(choice));
    }

    Map<String, Object> getWhereParameters() throws Exception {
        String username = Login.getUsername();
        Map<String, Object> whereParameters = new HashMap<>();

        if (username != null) whereParameters.put("username", username);

        return whereParameters;
    }

}
