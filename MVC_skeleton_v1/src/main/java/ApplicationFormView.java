import java.text.ParseException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class ApplicationFormView implements ViewInterface {


    @Override
    public ViewData create(ModelData modelData, String functionName, String operationName) throws Exception {
        switch (operationName){
            case "fillForm" : return openApplicationForm(modelData);
            case "insert" : return redirectSaveApplication(modelData);

        }

        return null;
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
