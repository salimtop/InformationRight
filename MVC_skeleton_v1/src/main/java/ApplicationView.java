import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ApplicationView implements ViewInterface{
    @Override
    public ViewData create(ModelData modelData, String functionName, String operationName) throws Exception {

        switch(operationName){
            case "newApplication" : return newApplication(modelData);
            case "listAllApplication" : return sendAllListRequest(modelData);
            case "listApplication" : return sendListRequest(modelData);
            case "select" : return listApplication(modelData);
        }

        System.out.println("No screen found");
        return null;
    }

    private ViewData listApplication(ModelData modelData) throws SQLException {
        ResultSet resultSet = modelData.resultSet;

        if (resultSet != null) {

            HashMap<String, Object> viewParameters = new HashMap<>();
            //AD.ApplicationNumber, AP.Status, MandatoryFlag, ExpireDate , ApplicationDate
            System.out.println("Application Number\tStatus\tMandatory\tExpire Date\tApplication Date");
            System.out.println("----------------------------------------------------------------------------------------");
            while (resultSet.next()) {
                // Retrieve by column name
               Integer applicationNumber = resultSet.getInt("ApplicationNumber");
               String status = resultSet.getString("Status");
               Boolean mandatory = resultSet.getBoolean("MandatoryFlag");
               Date expireDate = resultSet.getDate("ExpireDate");
               Date applicationDate = resultSet.getDate("ApplicationDate");

                // Display values
                System.out.println(applicationNumber+"\t\t\t\t"+status+"\t"+mandatory+"\t"+expireDate+"\t"+applicationDate);
                }


            }

        return new ViewData("Screen", "screen.gui");
    }

    private ViewData sendAllListRequest(ModelData modelData) throws Exception {

        HashMap<String,Object> parameters = new HashMap<>();

        System.out.println("Order Condition");
        Application.orderList();
        Integer filter = getInteger("Enter your order choice (Press enter to continue)",true);

        if(filter != null)
            parameters.put("ORDER BY",Application.getOrderColumn(filter));

        return new ViewData("Application", "select", parameters);

    }

    private ViewData sendListRequest(ModelData modelData) throws Exception {
        HashMap<String,Object> parameters = new HashMap<>();

        System.out.println("Filter Conditions");
        Application.orderList();
        Integer filter = getInteger("Enter your order choice (Press enter to continue)",true);

        if(filter != null){
            parameters.put("ORDER BY",Application.getOrderColumn(filter));
        }

        parameters.put("whereParameters",getWhereParameters());

        return new ViewData("Application", "select", parameters);
    }

    private ViewData newApplication(ModelData modelData) {
        return null;
    }

    Map<String, Object> getWhereParameters() throws Exception {

        Map<String, Object> whereParameters = new HashMap<>();
        whereParameters.put("AdmittedFrom",Login.getInstitutionId());
        return whereParameters;
    }
}
