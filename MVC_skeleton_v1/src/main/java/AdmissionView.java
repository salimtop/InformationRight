import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AdmissionView implements ViewInterface {
    @Override
    public ViewData create(ModelData modelData, String functionName, String operationName) throws Exception {
        switch(operationName){
            case "listApplication" : return sendListRequest(modelData);
            case "select" : return listApplication(modelData);
            case "admit" : return admitApplication(modelData);
        }

        return null;
    }

    private ViewData admitApplication(ModelData modelData) throws ParseException {
        Integer applicationNumber = getInteger("Enter application number", true);
        if(applicationNumber == null)
            return new ViewData("Screen","screen.gui");
        return null;
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

            System.out.println("Application Number\tStatus\tMandatory\tExpire Date\tApplication Date\tAdmitted By");
            System.out.println("----------------------------------------------------------------------------------------");
            while (resultSet.next()) {
                // Retrieve by column name
                Integer applicationNumber = resultSet.getInt("ApplicationNumber");
                String status = resultSet.getString("StatusType");
                Boolean mandatory = resultSet.getBoolean("MandatoryFlag");
                Date expireDate = resultSet.getDate("ExpireDate");
                Date applicationDate = resultSet.getDate("ApplicationDate");
                String admittedBy = resultSet.getString("AdmittedBy");



                // Display values
                System.out.println(applicationNumber+"\t\t\t\t"+status+"\t"+mandatory+"\t\t"+expireDate+"\t\t"+applicationDate+
                        "\t\t\t"+admittedBy);


            }

        }
        return new ViewData("Screen", "screen.gui");
    }


    }
