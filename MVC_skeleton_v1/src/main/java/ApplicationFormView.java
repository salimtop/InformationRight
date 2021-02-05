import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class ApplicationFormView implements ViewInterface {


    @Override
    public ViewData create(ModelData modelData, String functionName, String operationName) throws Exception {
        switch (operationName){
            case "fillForm" : return openApplicationForm(modelData);
            case "showApplicationForm" : return showApplicationForm(modelData);
            case "insert" : return redirectSaveApplication(modelData);
            case "select" : return printForm(modelData);

        }

        return null;
    }

    private ViewData printForm(ModelData modelData) throws SQLException {
        ResultSet result = modelData.resultSet;
        Integer applicationNumber = null;

        if(result.next())
            applicationNumber = result.getInt("ApplicationNumber");
        System.out.println("Application Number : "+applicationNumber);

        System.out.println("Name\t\tLast Name\tInformation&Document\trequest\t\t\tDelivery Type");
        while(result.next()){
            String name = result.getString("Name");
            String lastName = result.getString("LastName");
            Boolean isInformation = result.getBoolean("IsInformation");
            String request = result.getString("Request");
            String deliveryType = result.getString("DeliveryType");
            String data = result.getString("Data");
            String dataType = result.getString("DataType");

            System.out.println(name+"\t"+lastName+"\t"+(isInformation ? "Information" : "Document")+"\t"+request+"\t"+deliveryType+"\t"+data+"\t"+dataType);

        }


        return new ViewData("Screen","screen.gui");
    }

    private ViewData showApplicationForm(ModelData modelData) throws ParseException {
        Integer applicationNumber = getInteger("Enter application number :",true);
        if(applicationNumber == null)
            return new ViewData("Screen","screen.gui");

        Integer departmentId = Login.getInstitutionId();
        HashMap<String,Object> parameters = new HashMap<String,Object>();
        HashMap<String,Object> whereParameters = new HashMap<String,Object>();

        parameters.put("whereParameters",whereParameters);

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
        Integer lastId = (Integer) modelData.transferData;

        HashMap<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("LastID",lastId);

        return new ViewData("Application","createApplication",parameters);
    }

    private ViewData openApplicationForm(ModelData modelData) throws ParseException {
        HashMap<String,Object> parameters = (HashMap<String, Object>) modelData.transferData;

        Integer lastId = (Integer) parameters.get("LastID");

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
