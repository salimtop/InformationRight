import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginView implements ViewInterface{

    @Override
    public ViewData create(ModelData modelData, String functionName, String operationName) throws Exception {

        switch(operationName){
            case "login.gui": return loginGUI(modelData);
            case "select": return userMatch(modelData);
        }


        return null;
    }



    private ViewData userMatch(ModelData modelData) throws Exception {
        ResultSet resultSet = modelData.resultSet;

        if (resultSet != null) {

            HashMap<String,Object> viewParameters = new HashMap<>();

            while (resultSet.next()) {
                // Retrieve by column name
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                String title = resultSet.getString("title");
                String department = resultSet.getString("InstitutionName");
                Integer departmentId = resultSet.getInt("InstitutionId");
                String lastLogin = resultSet.getTimestamp("LastLogin").toString();
                Integer personalId = resultSet.getInt("PersonalId");

                // Display values
                if(username != null || password != null){
                    System.out.println("Welcome "+name+" "+surname);
                    System.out.println("Personal ID: "+personalId);
                    System.out.println(""+title+"/"+department);
                    System.out.println("Last login: "+lastLogin.toString());
                    resultSet.close();

                    //singleton class
                    Login.instance(username, name, surname, personalId,departmentId);

                    getString("\nPress enter to continue",true);
                    return new ViewData("Screen", "loadScreen");
                }


            }
            resultSet.close();
        }

        System.out.println("Incorrect Username or Password.");
        return new ViewData("Login", "login.gui");

}



    Map<String, Object> getWhereParameters() throws Exception {
        String username = getString("Username : ", false);
        String password = getString("Password : ", false);

        Map<String, Object> whereParameters = new HashMap<>();
        if (username != null) whereParameters.put("username", username);
        if (password != null) whereParameters.put("password", password);

        return whereParameters;
    }


    private ViewData loginGUI(ModelData modelData) throws Exception {
        System.out.println("Login");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("whereParameters", getWhereParameters());

        return new ViewData("Login","select",parameters);
    }
}
