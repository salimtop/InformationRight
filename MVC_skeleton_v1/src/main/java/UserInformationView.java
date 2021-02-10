import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserInformationView implements ViewInterface {
    @Override
    public ViewData create(ModelData modelData, String functionName, String operationName) throws Exception {

        switch (operationName){
            case "addUser" : return addUserGUI(modelData);
            case "select" : return listNonUser(modelData);
            case "insert" : return insertResponse(modelData);

        }
        return null;
    }

    private ViewData listNonUser(ModelData modelData) throws SQLException, ParseException {
        Map<String,Object> parameters = modelData.transferData == null ? new HashMap<String,Object>() : modelData.transferData;
        ResultSet result = modelData.resultSet;

        if(result != null){
            ArrayList<String[]> table = new ArrayList<String[]>();
            HashMap<Integer,Personnel> personnelList = new HashMap<>();

            table.add(new String[]{"Personnel Id", "Name", "Surname", "Title", "Works For"});
            Integer personnelId;
            String name, surname, title, institutionName;

            while(result.next()){
                String[] row = new String[table.get(0).length];

                personnelId = result.getInt("PersonnelId");
                name = result.getString("Name");
                surname = result.getString("Surname");
                title = result.getString("Title");
                institutionName = result.getString("InstitutionName");

                Integer departmentId = result.getInt("Department");

                personnelList.put(personnelId,new Personnel(personnelId,name,surname,title,departmentId));

                row[0] = String.valueOf(personnelId);
                row[1] = String.valueOf(name);
                row[2] = String.valueOf(surname);
                row[3] = String.valueOf(title);
                row[4] = String.valueOf(institutionName);

                table.add(row);

            }

            System.out.println("Personnel List");
            DatabaseUtilities.printTable(table,false);
            getString("Press enter to continue",true);

            if(parameters.containsKey("redirectFunction")){
                String function = (String) parameters.get("redirectFunction");
                String operation = (String) parameters.get("redirectOperation");
                parameters.put("nonUserList",personnelList);
                parameters.put("listNonUserFlag",true);
                return new ViewData(function,operation,parameters);
            }
        }
        return new ViewData("Login","login.gui");
    }

    private ViewData insertResponse(ModelData modelData) throws ParseException {
        Integer dbResponse = (Integer) modelData.transferData.get("lastId");

        if(dbResponse == null)
            System.out.println("Error occurred while creating user");
        else
            System.out.println("New account is created for id "+dbResponse);
        getString("Press enter to continue",true);

        return new ViewData("Screen","screen.gui");
    }


    private ViewData addUserGUI(ModelData modelData) throws ParseException {
        Map<String,Object> parameters = modelData.transferData == null ? new HashMap() : modelData.transferData;

        if(TypeTable.types == null){
            parameters.put("redirectFunction","UserInformation");
            parameters.put("redirectOperation","addUser");
            return new ViewData("TypeTable","loadChoice",parameters);
        }

        if( ! parameters.containsKey("listNonUserFlag")){
            parameters.put("redirectFunction","UserInformation");
            parameters.put("redirectOperation","addUser");
            return new ViewData("UserInformation","select",parameters);
        }

        Map<Integer,Personnel> personnelList = (Map<Integer, Personnel>) parameters.get("nonUserList");
        Integer personnelId;

        do{
            personnelId = getInteger("Enter personnel id :",true);
        }while(personnelId != null && ! personnelList.containsKey(personnelId));

        if(personnelId == null)
            return new ViewData("Screen","screen.gui");

        System.out.println("New User\n---------");
        String username = getString("Username :",false);
        String password, passwordMatch;
        boolean match = false;
        do{
            password = getString("Password :",false);
            passwordMatch = getString("Enter same password again :",false);
            if(password.equals(passwordMatch))
                match = true;
            else
                System.out.println("Passwords do not match. Enter again");
        }while(! match);

        Map<String,Object> insertParameters = new HashMap<String,Object>();

        String authorityChoice = getString("Press 'A' to add authority. (Enter to continue) :",true);

        if(authorityChoice.equalsIgnoreCase("A")){
            ArrayList<Integer> authority = giveAuthority();
            insertParameters.put("authority",authority);
        }

        insertParameters.put("personnelId",personnelId);
        insertParameters.put("username",username);
        insertParameters.put("password",password);
        return new ViewData("UserInformation","insert",insertParameters);
    }


    private ArrayList<Integer> giveAuthority() throws ParseException {
        ArrayList<Integer> authority = new ArrayList<Integer>();
        Map<Integer, Object> screenList = TypeTable.showType("ScreenType");
        Integer choice;
        String add = "A";
        do{
            choice = getInteger("Enter authority number :",false);
            if(screenList.containsKey(choice)){
                authority.add(choice);
                add = getString("Added! Press 'A' to add more : ",true);
            }
            else{
                choice = getInteger("Wrong!, Enter authority number :",false);
            }
        }while(add != null && add.equalsIgnoreCase("A"));

        return authority;
    }

}
