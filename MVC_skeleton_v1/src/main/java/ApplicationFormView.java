import java.math.BigDecimal;
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
        String name = null, middleName = null, lastName = null;
        BigDecimal tc = null;
        Integer applicationNumber = null, relatedApplication = null;
        String applierType = null, title = null;
        Double payment = null;
        Date paymentExpire = null;
        String request = null, telephone = null,telephoneType = null, fax = null;
        String address =null, addressType = null;
        
        
        ArrayList<String[]> table = new ArrayList<String[]>();

        table.add(new String[]{"Information&Document","Delivery Type","DataType","Data"});

        while(result.next()){
            String[] row = new String[table.get(0).length];

            applicationNumber = result.getInt("ApplicationNumber");
            name = result.getString("Name");
            lastName = result.getString("LastName");
            tc = result.getBigDecimal("turkishIdentity");
            applierType = result.getString("ApplierType");
            Boolean isInformation = result.getBoolean("IsInformation");
            request = result.getString("Request");
            String deliveryType = result.getString("DeliveryType");
            String data = result.getString("Data");
            String dataType = result.getString("DataType");
            payment = result.getDouble("PaymentAmount");
            paymentExpire = result.getDate("PaymentExpire");
            relatedApplication = result.getInt("RelatedApplication");
            address = result.getString("Address");
            addressType = result.getString("AddressType");
            fax = result.getString("fax");
            telephone = result.getBigDecimal("Telephone").toString();
            telephoneType = result.getString("TelephoneType");



            row[0] = (isInformation  ? "Information" : "Document");
            row[1] = deliveryType;
            row[2] = dataType;
            row[3] = data;
            table.add(row);

        }

        System.out.println("Application number :"+applicationNumber+(relatedApplication == 0 ? "" : "\tRelated Application :"+ relatedApplication));
        System.out.println((tc != null ? "Turkish Identity :"+tc : "Foreign"));
        System.out.println("Name Surname : "+ name+" "+(middleName == null ? "" : middleName)+" "+lastName);
        System.out.println(applierType + (title == null ? "" : "/"+title));
        System.out.println("Telephone/Fax:\t"+ telephone+"("+telephoneType+") "+ (fax == null?"":fax));
        System.out.println(addressType+" Address :"+address);
        System.out.println("Charge :"+payment+(payment == 0 ? "" : "\tExpire :"+paymentExpire));
        System.out.println("Request :"+request);
        DatabaseUtilities.printTable(table,false);
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

            data = getString("Requested data : ", false);

            //adds requested data to list of ApplicationForm
            applicationForm.dataList.add(new ApplicationForm.InformationAndDocument(applicationForm.applicationNumber,data,isInformation,dataType));

            addRow = getString("press A to add more or Enter to continue:",true);
        }while (addRow != null && addRow.equalsIgnoreCase("A"));

    }


}
