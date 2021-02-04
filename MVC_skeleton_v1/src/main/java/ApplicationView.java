import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ApplicationView implements ViewInterface{
    @Override
    public ViewData create(ModelData modelData, String functionName, String operationName) throws Exception {

        switch(operationName){
            case "newApplication" : return new ViewData("ApplicationForm","loadForm");
            case "createApplication" : return createApplication(modelData);
            case "listAllApplication" : return sendAllListRequest(modelData);
            case "listApplication" : return sendListRequest(modelData);
            case "select" : return listApplication(modelData);
            case "insert" : return showApplicationNumber(modelData);
        }

        System.out.println("No screen found");
        return null;
    }

    private ViewData showApplicationNumber(ModelData modelData) {

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
        String ss = getString("",false);
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


    Map<String, Object> getWhereParameters() throws Exception {

        Map<String, Object> whereParameters = new HashMap<>();
        whereParameters.put("AdmittedFrom",Login.getInstitutionId());
        return whereParameters;
    }

    private ViewData createApplication(ModelData modelData) throws ParseException {

        Map<String,Object> parameters = new HashMap<String,Object>();

        Applier person = createApplierGUI();
        ApplicationForm applicationForm = createApplicationFormGUI();
        Application application = createApplicationGUI();

        parameters.put("ApplicationFieldName",Application.getFieldNames());
        parameters.put("ApplicationFormFieldName",ApplicationForm.getFieldNames());
        parameters.put("ApplierFieldName",Applier.getFieldNames());

        parameters.put("Applier",person);
        parameters.put("ApplicationForm",applicationForm);
        parameters.put("Application",application);

        return new ViewData("Application","insert",parameters);
    }

    private Application createApplicationGUI() throws ParseException {


        Integer applicationNumber = null;
        int status = 1;
        HashMap<Integer,Object> statusType = (HashMap<Integer,Object>)ApplicationForm.types.get("StatusType");

        if(!statusType.get(1).equals("Open"))
            System.out.println("Application could not initialize as 'Open'");
        String applicationDate = LocalDate.now().toString();

        Integer admissionDeliveryType;
        if(Login.active()){
            HashMap<Integer,Object> deliveryTypes = ApplicationForm.showType("DeliveryType");
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

        String expireDate = null;
        Integer paymentNumber = null;
        String paymentExpire = null;

        Application application = new Application(applicationNumber,status,mandatory,admissionDeliveryType,applicationDate,expireDate,paymentNumber,paymentExpire,relatedApplication);

        return application;

    }

    private ApplicationForm createApplicationFormGUI() throws ParseException {
        String request = getString("Request : ",false);

        HashMap<Integer,Object> list = ApplicationForm.showType("DeliveryType");

        Integer desiredDeliveryType;
        do {
            desiredDeliveryType = getInteger("How is response desired to receive :",false);
        }while(!list.containsKey(desiredDeliveryType));

        ApplicationForm application = new ApplicationForm(null,request,desiredDeliveryType);
        return application;
    }



    public Applier createApplierGUI() throws ParseException {

        Integer applicationNumber = null;

        int choice;

        do {
            choice = getInteger("1 - Citizen\n2 - Foreign\nChoice :", false);
        }while(choice != 1 && choice != 2);

        Double turkishIdentity = null;
        Boolean citizenFlag = false;

        if(choice == 1){
            turkishIdentity = getDouble("Enter Turkish ID : ",false);
            citizenFlag = true;
        }

        HashMap<Integer, Object> applierTypes = ApplicationForm.showType("ApplierType");

        Integer applierType;
        do {
            applierType = getInteger("Enter Applier Type :",false);
        }while(!applierTypes.containsKey(applierType));

        String title = null;
        if(applierType == 2)
            title = getString("Title : ",false);


        String name = getString("Enter Name: ", false);
        String middleName = getString("Enter Middle Name : ",true);
        String lastName = getString("Enter Surname : ",false);

        String address = getString("Address: ",true);

        HashMap<Integer, Object> addressTypes = ApplicationForm.showType("AddressType");

        Integer addressType;
        do {
            addressType = getInteger("Enter Address Type :",false);
        }while(!addressTypes.containsKey(addressType));

        Double telephone = getDouble("Telephone: ",true);

        HashMap<Integer, Object> telephoneTypes = ApplicationForm.showType("TelephoneType");

        Integer telephoneType;
        do {
            telephoneType = getInteger("Enter Telephone Type :",false);
        }while(!telephoneTypes.containsKey(telephoneType));


        String email = getString("E-mail:",true);
        String fax = getString("Fax: ",true);

        Applier person = new Applier();
        person.setApplicationNumber(applicationNumber);
        person.setName(name);
        person.setMiddleName(middleName);
        person.setLastName(lastName);
        person.setTurkishIdentity(turkishIdentity);
        person.setCitizenFlag(citizenFlag);
        person.setApplierType(applierType);
        person.setTitle(title);
        person.setTelephone(telephone);
        person.setTelephoneType(telephoneType);
        person.setEmail(email);
        person.setAddress(address);
        person.setAddressType(addressType);
        person.setFax(fax);

        return person;
    }

}
