import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdmissionView implements ViewInterface {
    @Override
    public ViewData create(ModelData modelData, String functionName, String operationName) throws Exception {
        switch(operationName){
            case "listApplication" : return sendListRequest(modelData);
            case "select" : return listApplication(modelData);
            case "admit" : return admitApplication(modelData);
            case "insert" : return insertResult(modelData);
        }

        return null;
    }

    private ViewData insertResult(ModelData modelData) {
        if((Integer)modelData.transferData.get("lastId") > 0)
            System.out.println("Application admitted successfully ");
        else
            System.out.println("Error occurred while admission ");
        return new ViewData("Screen","screen.gui");
    }

    private ViewData admitApplication(ModelData modelData) throws ParseException {

        Map<String,Object> parameters = modelData.transferData == null ? new HashMap() : modelData.transferData;

        boolean redirectedToMe = false;
        if( parameters.containsKey("redirectFunction") )
            redirectedToMe = parameters.get("redirectFunction").equals("Admission") && parameters.get("redirectOperation").equals("admit");


        //if application is not selected, list applications and turn back
        if(!redirectedToMe)
            return getApplicationList(parameters);

        //if application details are not prompted, prompt and turn back
        if( ! parameters.containsKey("applicationNumber"))
            return getApplicationForm(parameters);

        Integer applicationNumber = (Integer) parameters.get("applicationNumber");

        Boolean institutionsListed = (Boolean) parameters.get("institutionFlag");
        if(institutionsListed == null || !institutionsListed  )
            return getInstitutionList(parameters);

        Integer institutionNumber = getInteger("Admit to :",true);

        if(institutionNumber == null)
            return new ViewData("Screen","screen.gui");

        Admission admission = new Admission(institutionNumber, applicationNumber,null,null );

        Map<String,Object> insertParameters = new HashMap<String,Object>();

        insertParameters.put("AdmissionFieldName",Admission.getFieldNames());
        insertParameters.put("Admission",admission);
        return new ViewData("Admission","insert",insertParameters);
    }

    private ViewData getInstitutionList(Map<String, Object> parameters) {
        Map<String,Object> redirect = parameters;
        redirect.put("redirectFunction","Admission");
        redirect.put("redirectOperation","admit");
        redirect.put("institutionFlag",false);
        //print applications and turn back.
        return new ViewData("Institution","select",redirect);
    }

    private ViewData getApplicationForm(Map<String, Object> parameters) {
        Map<String,Object> redirect = parameters;
        redirect.put("redirectFunction","Admission");
        redirect.put("redirectOperation","admit");

        //print applications and turn back.
        return new ViewData("ApplicationForm","showApplicationForm",redirect);
    }

    private ViewData getApplicationList(Map<String, Object> parameters){
        Map<String,Object> redirect = parameters;
        redirect.put("redirectFunction","Admission");
        redirect.put("redirectOperation","admit");

        Map<String,Object> whereParameters = new HashMap<String, Object>();
        whereParameters.put("AdmittedBy","IS NULL");

        redirect.put("whereParameters",whereParameters);
        redirect.put("ORDER BY"," StatusType, ExpireDate ");

        //print applications and turn back.
        return new ViewData("Application","listAllApplication",redirect);
    }

    private ViewData sendListRequest(ModelData modelData) throws Exception {
        HashMap<String,Object> parameters = new HashMap<>();

        System.out.println("Filter Conditions");

        //Prompt order by choices
        Application.orderList();
        Integer filter = getInteger("Enter your order choice (Press enter to continue)",true);

        if(filter != null){
            parameters.put("ORDER BY",Application.getOrderColumn(filter));
        }

        parameters.put("whereParameters",getWhereParameters());

        return new ViewData("Application", "select", parameters);
    }


    Map<String, Object> getWhereParameters() throws Exception {

        Map<String, Object> whereParameters = new HashMap<>();
        whereParameters.put("AdmittedBy",Login.getInstitutionId());
        return whereParameters;
    }

    private ViewData listApplication(ModelData modelData) throws SQLException {
        ResultSet resultSet = modelData.resultSet;

        if (resultSet != null) {

            HashMap<String, Object> viewParameters = new HashMap<>();
            //AD.ApplicationNumber, AP.Status, MandatoryFlag, ExpireDate , ApplicationDate

            ArrayList<String[]> table = new ArrayList<>();

            table.add(new String[]{"Application Number","Status","Mandatory","Expire Date",	"Application Date",	"Admitted By"});

            while (resultSet.next()) {
                //row of table
                String[] row = new String[table.get(0).length];

                // Retrieve by column name
                row[0] = String.valueOf(resultSet.getInt("ApplicationNumber"));
                row[1] = resultSet.getString("StatusType");
                row[2] = String.valueOf(resultSet.getBoolean("MandatoryFlag"));
                row[3] = String.valueOf(resultSet.getDate("ExpireDate"));
                row[4] = String.valueOf(resultSet.getDate("ApplicationDate"));
                row[5] = resultSet.getString("AdmittedBy");

                table.add(row);
            }

            //Display table
            DatabaseUtilities.printTable(table,true);

        }
        return new ViewData("Screen", "screen.gui");
    }


    }
