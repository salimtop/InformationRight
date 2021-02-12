import com.github.freva.asciitable.AsciiTable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ApplicationView implements ViewInterface{
    @Override
    public ViewData create(ModelData modelData, String functionName, String operationName) throws Exception {

        switch(operationName){
            case "createApplication" : return createApplication(modelData);
            case "listAllApplication" : return sendAllListRequest(modelData);
            case "select" : return listApplication(modelData);
            case "insert" : return showApplicationNumber(modelData);
            case "update" : return update(modelData);
        }

        System.out.println("No screen found");
        return null;
    }

    private ViewData update(ModelData modelData) {
        //redirect if another process requests
        if(modelData.transferData.containsKey("redirectFunction")){
            modelData.transferData.put("applicationUpdate",true);
            String function = (String) modelData.transferData.get("redirectFunction");
            String operation = (String) modelData.transferData.get("redirectOperation");
            return new ViewData(function,operation,modelData.transferData);
        }

        return new ViewData("Screen","screen.gui");
    }

    private ViewData showApplicationNumber(ModelData modelData) {

        //dbResponse is last inserted row
        Integer dbResponse = (Integer) modelData.transferData.get("lastId");

        if(dbResponse != -1){
            System.out.println("The application has saved successfully!");
            System.out.println("Application Inquiry number : "+dbResponse);
        }
        else
            System.out.println("Error!, application could not saved.");
        if(Login.active())
            return new ViewData("Screen","screen.gui");
        return new ViewData("MainMenu","");
    }


    private ViewData listApplication(ModelData modelData) throws SQLException, ParseException {
        ResultSet resultSet = modelData.resultSet;

        if (resultSet != null) {

            HashMap<String, Object> viewParameters = new HashMap<>();
            //AD.ApplicationNumber, AP.Status, MandatoryFlag, ExpireDate , ApplicationDate

            ArrayList<String[]> table = new ArrayList<>();
            table.add(new String[]{"Application Number","Status","Mandatory","Expire Date","Application Date","Admitted By"});
            while (resultSet.next()) {
                String[] row = new String[table.get(0).length];
                // Retrieve by column name
               Integer applicationNumber = resultSet.getInt("ApplicationNumber");
               String status = resultSet.getString("StatusType");
               Boolean mandatory = resultSet.getBoolean("MandatoryFlag");
               Date expireDate = resultSet.getDate("ExpireDate");
               Date applicationDate = resultSet.getDate("ApplicationDate");
               String admittedBy = resultSet.getString("AdmittedBy");

                row[0] = String.valueOf(applicationNumber);
                row[1] = status;
                row[2] = String.valueOf(mandatory);
                row[3] = String.valueOf(expireDate);
                row[4] = String.valueOf(applicationDate);
                row[5] = String.valueOf(admittedBy);

                table.add(row);

            }
            DatabaseUtilities.printTable(table,false);

            getString("Press Enter to continue",true);

            }

        if(modelData.transferData.containsKey("redirectFunction")){
            String function = (String) modelData.transferData.get("redirectFunction");
            String operation = (String) modelData.transferData.get("redirectOperation");
            return new ViewData(function,operation,modelData.transferData);
        }


        return new ViewData("Screen", "screen.gui");
    }

    private ViewData sendAllListRequest(ModelData modelData) throws Exception {
        HashMap<String,Object> parameters = (HashMap<String, Object>) modelData.transferData;
        if(modelData.transferData == null)
            parameters = new HashMap<>();

        Integer filter = null;
        boolean askOrder = !parameters.containsKey("ORDER BY");

        if(askOrder){
            System.out.println("Order Condition");
            Application.orderList();
            filter = getInteger("Enter your order choice (Press enter to continue)",true);
        }
        if(filter != null && askOrder)
            parameters.put("ORDER BY",Application.getOrderColumn(filter));

        return new ViewData("Application", "select", parameters);

    }





    private ViewData createApplication(ModelData modelData) throws ParseException {

        Map<String,Object> parameters = (Map<String, Object>) modelData.transferData;

        Integer lastId = (Integer) parameters.get("lastId");

        Application application = createApplicationGUI();

        application.setApplicationNumber(lastId);

        parameters.put("ApplicationFieldName",Application.getFieldNames());

        parameters.put("Application",application);

        return new ViewData("Application","insert",parameters);
    }

    private Application createApplicationGUI() throws ParseException {


        Integer applicationNumber = null;
        int status = 1;
        HashMap<Integer,Object> statusType = (HashMap<Integer,Object>)TypeTable.types.get("StatusType");

        if(!statusType.get(1).equals("Open"))
            System.out.println("Application could not initialize as 'Open'");
        Date applicationDate = null;

        Integer admissionDeliveryType;
        if(Login.active()){
            HashMap<Integer,Object> deliveryTypes = TypeTable.showType("DeliveryType");
            do {
                admissionDeliveryType = getInteger("How the application is delivered ?:",false);
            }while(!deliveryTypes.containsKey(admissionDeliveryType));

        }
        else
            admissionDeliveryType = 1;


        boolean	mandatory = false;

        Integer relatedApplication = getInteger("Enter related application number if it is rejected before :\n",true);
        if(relatedApplication != null)
            mandatory = true;

        Date expireDate = null;
        Integer paymentNumber = null;
        Date paymentExpire = null;

        Application application = new Application(applicationNumber,status,mandatory,admissionDeliveryType,applicationDate,expireDate,paymentNumber,paymentExpire,relatedApplication);

        return application;

    }






}
