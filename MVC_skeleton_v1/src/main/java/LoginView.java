import javax.swing.*;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class LoginView implements ViewInterface{

    @Override
    public ViewData create(ModelData modelData, String functionName, String operationName) throws Exception {

        switch(operationName){
            case "login.gui": return loginGUI(modelData);
        }


        return null;
    }

    private ViewData loginGUI(ModelData modelData) throws Exception {
        System.out.println("\nLogin");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("whereParameters", getWhereParameters());
        String username = getString("Username: ",false);
        String password = getString("Password: ",false);

        loginParameters.put("username", username);
        loginParameters.put("password", password);
        return new ViewData("Login","select",loginParameters);
    }
}
