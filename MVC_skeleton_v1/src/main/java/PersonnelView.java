import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PersonnelView implements ViewInterface{
    @Override
    public ViewData create(ModelData modelData, String functionName, String operationName) throws Exception {

        switch (operationName){

            case "addPersonnel" : return addUserGUI(modelData);
            case "insert" : return insertResponse(modelData);
            case "select" : return listPersonnel(modelData);

        }

        return null;
    }

    private ViewData insertResponse(ModelData modelData) throws ParseException {
        Integer lastId = null;
        if(modelData != null)
            lastId = (Integer) modelData.transferData.get("lastId");

        if(lastId != null)
            System.out.println("Personnel is saved successfully!\nPersonnel ID :"+lastId);
        else
            System.out.println("Error while saving personnel");
        getString("Press enter to continue",true);
        return new ViewData("Screen","screen.gui");
    }


    private ViewData addUserGUI(ModelData modelData) throws ParseException {

        Map<String, Object> parameters = modelData.transferData == null ? new HashMap<String, Object>() : modelData.transferData;
        String personnelName;
        String personnelSurname;
        String personnelTitle;
        Personnel personnel;

        if (! parameters.containsKey("institutionFlag")) {
            personnelName = getString("Enter personnel name :", true);
            if (personnelName == null)
                return new ViewData("Login", "login.gui");

            personnelSurname = getString("Enter personnel surname :", false);
            personnelTitle = getString("Enter personnel title :", false);
            personnel = new Personnel(null, personnelName, personnelSurname, personnelTitle,null);

            parameters.put("personnel",personnel);
            parameters.put("redirectFunction", "Personnel");
            parameters.put("redirectOperation", "addPersonnel");
            return new ViewData("Institution", "select", parameters);
        }
        Integer departmentId = getInteger("Enter department id", false);
        personnel = (Personnel) parameters.get("personnel");
        personnel.department = departmentId;

        parameters.put("Personnel",personnel);
        parameters.put("PersonnelFieldName",Personnel.getFieldName());
        return new ViewData("Personnel","insert",parameters);
    }


    private ViewData listPersonnel(ModelData modelData) throws SQLException, ParseException {
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
                parameters.put("personnelList",personnelList);
                parameters.put("listPersonnelFlag",true);
                return new ViewData(function,operation,parameters);
            }
        }
        return new ViewData("Screen","screen.gui");
    }
}
