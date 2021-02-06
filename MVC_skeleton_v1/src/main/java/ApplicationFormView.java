import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class ApplicationFormView implements ViewInterface {


    @Override
    public ViewData create(ModelData modelData, String functionName, String operationName) throws Exception {
        switch (operationName){
            case "fillForm" : return openApplicationForm(modelData);
            case "showApplicationForm" : return showApplicationFormRequest(modelData);
            case "insert" : return redirectSaveApplication(modelData);
            case "select" : return showApplicationForm(modelData);

        }

        return null;
    }

    private ViewData showApplicationForm(ModelData modelData) throws SQLException, ParseException {
        ResultSet result = modelData.resultSet;
        Integer applicationNumber = null;
        Integer payment = null;
        Date paymentExpire = null;


        ArrayList<String[]> table = new ArrayList<String[]>();


        table.add(new String[]{"Name","Last Name","Information&Document","request","Delivery Type","DataType","Data"});

        while(result.next()){
            String[] row = new String[table.get(0).length];

            applicationNumber = result.getInt("ApplicationNumber");
            String name = result.getString("Name");
            String lastName = result.getString("LastName");
            Boolean isInformation = result.getBoolean("IsInformation");
            String request = result.getString("Request");
            String deliveryType = result.getString("DeliveryType");
            String data = result.getString("Data");
            String dataType = result.getString("DataType");
            payment = result.getInt("PaymentAmount");
            paymentExpire = result.getDate("PaymentExpire");

            row[0] = name;
            row[1] = lastName;
            row[2] = (isInformation  ? "Information" : "Document");
            row[3] = request;
            row[4] = deliveryType;
            row[5] = dataType;
            row[6] = data;


            table.add(row);

        }
        System.out.println("Application number :"+applicationNumber);
        System.out.println("Charge :"+payment+(payment == 0 ? "" : "Expire :"+paymentExpire));
        DatabaseUtilities.printTable(table,true);
        getString("Press enter to continue",true);

        //redirect if another process requests
        if(modelData.transferData.containsKey("redirectFunction")){
            modelData.transferData.put("applicationNumber",applicationNumber);
            String function = (String) modelData.transferData.get("redirectFunction");
            String operation = (String) modelData.transferData.get("redirectOperation");
            return new ViewData(function,operation,modelData.transferData);
        }

        return new ViewData("Screen","screen.gui");
    }

    private ViewData showApplicationFormRequest(ModelData modelData) throws ParseException {
        Integer applicationNumber = null;

        applicationNumber = getInteger("Enter application number :",true);

        if(applicationNumber == null)
            return new ViewData("Screen","screen.gui");

        Integer departmentId = Login.getInstitutionId();
        HashMap<String,Object> parameters = (HashMap<String, Object>) modelData.transferData;

        //if no process request data create by self
        if(parameters == null)
            parameters = new HashMap<String,Object>();
        HashMap<String,Object> whereParameters = new HashMap<String,Object>();

        parameters.put("whereParameters",whereParameters);

        //If user have authority to see all application forms
        if(Login.getScreen().containsValue("listAllApplication"))
            parameters.put("justAdmitted",false);
        else{
            parameters.put("justAdmitted",true);
            whereParameters.put("admittedBy",departmentId);
        }

        whereParameters.put("AF.ApplicationNumber",applicationNumber);

        return new ViewData("ApplicationForm","select",parameters);
    }


    private ViewData redirectSaveApplication(ModelData modelData) {
        Integer lastId = (Integer) modelData.transferData.get("lastId");

        HashMap<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("lastId",lastId);

        return new ViewData("Application","createApplication",parameters);
    }

    private ViewData openApplicationForm(ModelData modelData) throws ParseException {
        HashMap<String,Object> parameters = (HashMap<String, Object>) modelData.transferData;

        Integer lastId = (Integer) parameters.get("lastId");

        ApplicationForm applicationForm = createApplicationFormGUI(lastId);

        applicationForm.setApplicationNumber(lastId);

        parameters.put("ApplicationFormFieldName",ApplicationForm.getFieldNames());
        parameters.put("ApplicationForm",applicationForm);


        return new ViewData("ApplicationForm","insert",parameters);
    }



    private ApplicationForm createApplicationFormGUI(Integer lastId) throws ParseException {
        String request = getString("Request : ",false);

        HashMap<Integer,Object> list = TypeTable.showType("DeliveryType");

        Integer desiredDeliveryType;
        do {
            desiredDeliveryType = getInteger("How is response desired to receive :",false);
        }while(!list.containsKey(desiredDeliveryType));

        ApplicationForm applicationForm = new ApplicationForm(lastId,request,desiredDeliveryType);

        dataRequestGUI(applicationForm);

        return applicationForm;
    }

    private void dataRequestGUI(ApplicationForm applicationForm) throws ParseException {
        String data;
        Boolean isInformation;
        Integer dataType;
        HashMap<Integer, Object> list;
        String addRow = "";

        do{
            list = TypeTable.showType("DataType");
            do{

                dataType = getInteger("DataType : ",false);
            }while(!list.containsKey(dataType));

            int choice;

            do{choice = getInteger("1 - Information\n2 - Document\nChoice : ",false);
            }while(choice < 1 || choice > 2 );

            isInformation = choice == 1 ? true : false;

            data = getString("Requested data : ", true);

            //adds requested data to list of ApplicationForm
            applicationForm.dataList.add(new ApplicationForm.InformationAndDocument(applicationForm.applicationNumber,data,isInformation,dataType));

            addRow = getString("press A to add more or Enter to continue:",true);
        }while (addRow != null && addRow.equalsIgnoreCase("A"));

    }


}
