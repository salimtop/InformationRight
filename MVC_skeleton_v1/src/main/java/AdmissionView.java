import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AdmissionView implements ViewInterface {
    @Override
    public ViewData create(ModelData modelData, String functionName, String operationName) throws Exception {
        switch(operationName){
            case "listAssigned" : return personnelGUI(modelData);//
            case "listApplication" : return sendListRequest(modelData);
            case "forwardApplication" : return forwardApplication(modelData);
            case "select" : return listApplication(modelData);
            case "admit" : return admitApplication(modelData);
            case "insert" : return insertResult(modelData);
            case "update" : return updateResult(modelData);
        }

        return null;
    }

    private ViewData updateResult(ModelData modelData) {
        //redirect if another process requests
        if(modelData.transferData.containsKey("redirectFunction")){
            modelData.transferData.put("updateAdmissionFlag",true);
            String function = (String) modelData.transferData.get("redirectFunction");
            String operation = (String) modelData.transferData.get("redirectOperation");
            return new ViewData(function,operation,modelData.transferData);
        }
        return new ViewData("MainMenu","");
    }

    private ViewData personnelGUI(ModelData modelData) throws Exception {
        Map<String,Object> parameters = modelData.transferData == null ? new HashMap() : modelData.transferData;

        Map<String,Object> types = TypeTable.types;

        //go list application and go back here
        if(types == null) {
            parameters.put("redirectFunction", "Admission");
            parameters.put("redirectOperation", "listAssigned");
            return new ViewData("TypeTable","loadChoice",parameters);
        }

        //accept listed applications
        boolean redirectedToMe = false;
        if( parameters.containsKey("listApplicationFlag") )
            redirectedToMe = parameters.get("redirectFunction").equals("Admission") && parameters.get("redirectOperation").equals("listAssigned");
        parameters.remove("listApplicationFlag");
        if( ! redirectedToMe)
            return getApplicationList(parameters,"listAssigned");



        Integer choice;
        System.out.println("1 - Show Application Details");
        System.out.println("2 - Enter Respond");
        System.out.println("3 - Forward");
        System.out.println("4 - Main Menu");
        do {
            choice = getInteger("Enter choice : ",false);
        }while(choice < 1 || choice > 5);

        switch(choice){
            case 1 : return getApplicationForm(parameters,"listAssigned");
            case 2 : return respondApplication(modelData);
            case 3 : return forwardApplication(modelData);
            case 4 : return new ViewData("Screen","screen.gui");
        }

        return new ViewData("Admission","listAssigned");
    }


    private ViewData forwardApplication(ModelData modelData) throws ParseException {
        Application application = null;
        if(! modelData.transferData.containsKey("application")){
            application = getAssignedApplication(modelData);
            modelData.transferData.put("application",application);
        }
        else
            application = (Application) modelData.transferData.get("application");

        if(application == null)
            new ViewData("Admission","listAssigned");

        if( !modelData.transferData.containsKey("institutionFlag")){
            modelData.transferData.put("redirectFunction","Admission");
            modelData.transferData.put("redirectOperation","forwardApplication");
            return new ViewData("Institution","select",modelData.transferData);
        }

        Institution institution = getForwardInstitution(modelData);
        if(institution == null)
            return new ViewData("Admission","listAssigned");

        Map<String,Object> parameters = new HashMap<>();
        Map<String,Object> updateParameters = new HashMap<>();
        Map<String,Object> whereParameters = new HashMap<>();

        updateParameters.put("forwardedTo",institution.institutionId);
        whereParameters.put("admittedBy",Login.getInstitutionId());
        whereParameters.put("applicationNumber",application.applicationNumber);

        parameters.put("updateChoice","forwardUpdate");
        parameters.put("forwardInstitution",institution.institutionId);

        parameters.put("redirectFunction","Admission");
        parameters.put("redirectOperation","listAssigned");
        parameters.put("updateParameters",updateParameters);
        parameters.put("whereParameters",whereParameters);

        return new ViewData("Admission","update",parameters);
    }

    private Institution getForwardInstitution(ModelData modelData) throws ParseException {

        Map<Integer,Institution> institutionList = (Map<Integer, Institution>) modelData.transferData.get("institutionList");
        Integer institutionId;
        boolean isContain = false;
        do{
            institutionId = getInteger("Enter institution number : ",true);
            if(institutionId == null)
                return null;
            if(institutionList.containsKey(institutionId))
                isContain = true;
            else
                System.out.println("Institution not found!");
        }while( ! isContain);

        Institution institution = institutionList.get(institutionId);

        return institution;

    }

    private ViewData respondApplication(ModelData modelData) throws Exception {
        Application application = getAssignedApplication(modelData);
        if(application == null)
            return new ViewData("Admission","listAssigned");

        System.out.println("1 - Change Status");
        System.out.println("2 - Enter Respond");
        System.out.println("3 - Previous page");
        Integer choice;
        do{
            choice = getInteger("Enter choice : ",false);
        }while(choice < 1 || choice > 3);

        switch(choice){
            case 1 : return changeStatusGUI(modelData,application);
            case 2 : return enterRespondGUI(modelData,application);
            case 3 : return new ViewData("Admission","listAssigned",modelData.transferData);
        }

        return new ViewData("Screen","screen.gui");
    }

    private Application getAssignedApplication(ModelData modelData) throws ParseException {
        Map<Integer,Application> applicationList = (Map<Integer, Application>) modelData.transferData.get("applicationList");
        Integer applicationNumber;
        boolean isContain = false;
        do{
            applicationNumber = getInteger("Enter application number : ",true);
            if(applicationNumber == null)
                return null;
            if(applicationList.containsKey(applicationNumber))
                isContain = true;
            else
                System.out.println("Application not found!");
        }while( ! isContain);

        Application application = applicationList.get(applicationNumber);

        return application;
    }


    private ViewData enterRespondGUI(ModelData modelData, Application application) throws Exception {
        String response = null;

        Map<String,Object> parameters = modelData.transferData;

        response = getString("Enter response :",true);
        if(response == null)
            return new ViewData("Admission","listAssigned");


        Respond respond = new Respond(application.applicationNumber,response,null,Login.getInstitutionId());

        parameters.put("Respond",respond);
        parameters.put("RespondFieldName",Respond.getFieldNames());
        parameters.put("redirectFunction","Admission");
        parameters.put("redirectOperation","listAssigned");
        return new ViewData("Respond","insert", parameters);
    }

    private ViewData changeStatusGUI(ModelData modelData,Application application) throws ParseException {
        Map<Integer,Object> statusType = TypeTable.showType("StatusType");;
        Integer choice;
        do{
            choice = getInteger("Enter choice : ",false);
        }while( ! statusType.containsKey(choice));

        String newStatus = (String) statusType.get(choice);

        if(newStatus.equals("Rejected"))
            return rejectGUI(modelData,choice,application);
        else if(newStatus.equals("Payment Waiting"))
            return paymentGUI(modelData,choice,application);

        return setStatus(modelData,choice,application);
    }

    private ViewData paymentGUI(ModelData modelData, Integer choice, Application application) throws ParseException {
        Double charge = getDouble("Enter charge amount :",false);
        String expire = "";

        Map<String,Object> parameters = modelData.transferData;
        Map<String,Object> updateParameters = new HashMap<String,Object>();
        Map<String,Object> whereParameters = new HashMap<String,Object>();

        updateParameters.put("StatusType",choice);
        updateParameters.put("RejectionType",null);
        updateParameters.put("PaymentAmount",charge);
        updateParameters.put("PaymentExpire",expire);
        whereParameters.put("ApplicationNumber",application.applicationNumber);

        parameters.put("updateParameters",updateParameters);
        parameters.put("whereParameters",whereParameters);
        parameters.put("redirectFunction","Admission");
        parameters.put("redirectOperation","listAssigned");

        return new ViewData("Application","update",parameters);
    }

    private ViewData rejectGUI(ModelData modelData, Integer code, Application application) throws ParseException {
        if(application.mandatory){
            System.out.println("This application cannot be rejected");
            getString("Press enter to continue",true);
            return new ViewData("Admission","listAssigned",modelData.transferData);
        }
        Integer rejectionType;

        Map<Integer,Object> rejectTypes = TypeTable.showType("RejectionType");
        do{
              rejectionType = getInteger("Enter choice :",false);
        }while( ! rejectTypes.containsKey(rejectionType));
        application.setRejectionType(rejectionType);

        modelData.transferData.put("redirectFunction", "Admission");
        modelData.transferData.put("redirectOperation", "loadAssigned");

        return setStatus(modelData,code,application);
    }

    private ViewData setStatus(ModelData modelData,Integer code , Application application){
        Map<String,Object> parameters = modelData.transferData;
        Map<String,Object> updateParameters = new HashMap<String,Object>();
        Map<String,Object> whereParameters = new HashMap<String,Object>();

        updateParameters.put("StatusType",code);
        updateParameters.put("RejectionType",application.rejectionType);
        whereParameters.put("ApplicationNumber",application.applicationNumber);

        parameters.put("updateParameters",updateParameters);
        parameters.put("whereParameters",whereParameters);
        parameters.put("redirectFunction","Admission");
        parameters.put("redirectOperation","listAssigned");

        return new ViewData("Application","update",parameters);
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
            return getAllApplicationList(parameters,"admit");

        //if application details are not prompted, prompt and turn back
        if( ! parameters.containsKey("applicationNumber"))
            return getApplicationForm(parameters,"admit");

        Integer applicationNumber = (Integer) parameters.get("applicationNumber");

        Boolean institutionsListed = (Boolean) parameters.get("institutionFlag");
        if(institutionsListed == null || !institutionsListed  )
            return getInstitutionList(parameters,"admit");

        Integer institutionNumber = getInteger("Admit to :",true);

        if(institutionNumber == null)
            return new ViewData("Screen","screen.gui");

        Admission admission = new Admission(institutionNumber, applicationNumber,null,null );

        Map<String,Object> insertParameters = new HashMap<String,Object>();

        insertParameters.put("AdmissionFieldName",Admission.getFieldNames());
        insertParameters.put("Admission",admission);
        return new ViewData("Admission","insert",insertParameters);
    }

    private ViewData getInstitutionList(Map<String, Object> parameters,String operation) {
        Map<String,Object> redirect = parameters;
        redirect.put("redirectFunction","Admission");
        redirect.put("redirectOperation","admit");
        redirect.put("institutionFlag",false);
        //print applications and turn back.
        return new ViewData("Institution","select",redirect);
    }

    private ViewData getApplicationForm(Map<String, Object> parameters,String turnBack) {
        Map<String,Object> redirect = parameters;
        redirect.put("redirectFunction","Admission");
        redirect.put("redirectOperation",turnBack);

        //print applications and turn back.
        return new ViewData("ApplicationForm","showApplicationForm",redirect);
    }

    //Gets all applications, it is used for admitting
    private ViewData getAllApplicationList(Map<String, Object> parameters,String turnBack){
        Map<String,Object> redirect = parameters;
        redirect.put("redirectFunction","Admission");
        redirect.put("redirectOperation",turnBack);

        Map<String,Object> whereParameters = new HashMap<String, Object>();
        whereParameters.put("AdmittedBy","IS NULL");

        redirect.put("whereParameters",whereParameters);
        redirect.put("ORDER BY"," StatusType, ExpireDate ");

        //print applications and turn back.
        return new ViewData("Application","listAllApplication",redirect);
    }

    //it retrieves admitted lists.
    private ViewData getApplicationList(Map<String, Object> parameters,String turnBack) {
        Map<String,Object> redirect = parameters;
        redirect.put("redirectFunction","Admission");
        redirect.put("redirectOperation",turnBack);

        redirect.put("ORDER BY"," StatusType, ExpireDate ");

        //print applications and turn back.
        return new ViewData("Admission","listApplication",redirect);
    }

    private ViewData sendListRequest(ModelData modelData) throws Exception {
        Map<String,Object> parameters = modelData.transferData == null ? new HashMap() : modelData.transferData;

        if(! parameters.containsKey("ORDER BY")){
            System.out.println("Filter Conditions");

            //Prompt order by choices
            Application.orderList();
            Integer filter = getInteger("Enter your order choice (Press enter to continue)",true);
            if(filter != null)
                parameters.put("ORDER BY",Application.getOrderColumn(filter));
        }
        parameters.put("whereParameters",getWhereParameters());

        return new ViewData("Admission", "select", parameters);
    }


    Map<String, Object> getWhereParameters() {

        Map<String, Object> whereParameters = new HashMap<>();
        whereParameters.put("AdmittedBy",Login.getInstitutionId());
        whereParameters.put("forwardedTo","IS NULL");
        return whereParameters;
    }

    private ViewData listApplication(ModelData modelData) throws SQLException, ParseException {
        ResultSet resultSet = modelData.resultSet;

        if (resultSet != null) {

            HashMap<String, Object> viewParameters = new HashMap<>();
            HashMap<Integer, Application> applicationList = new HashMap<>();

            //AD.ApplicationNumber, AP.Status, MandatoryFlag, ExpireDate , ApplicationDate

            //to print
            ArrayList<String[]> table = new ArrayList<>();
            table.add(new String[]{"Application Number","Status","Mandatory","Expire Date",	"Application Date",	"Admitted By","Payment Amount","PaymentExpire"});

            Integer applicationNumber = null;
            Integer payment = null;
            Date paymentExpire = null;

            while (resultSet.next()) {
                //row of table
                String[] row = new String[table.get(0).length];


                applicationNumber = resultSet.getInt("ApplicationNumber");
                String statusType = resultSet.getString("StatusType");
                Boolean mandatory = resultSet.getBoolean("MandatoryFlag");
                Date expireDate = resultSet.getDate("ExpireDate");
                Date applicationDate = resultSet.getDate("ApplicationDate");
                String admittedBy = resultSet.getString("AdmittedBy");
                payment = resultSet.getInt("PaymentAmount");
                paymentExpire = resultSet.getDate("PaymentExpire");

                applicationList.put(applicationNumber, new Application(applicationNumber,mandatory,applicationDate,expireDate));

                // Retrieve by column name
                row[0] = String.valueOf(applicationNumber);
                row[1] = String.valueOf(statusType);
                row[2] = String.valueOf(mandatory);
                row[3] = String.valueOf(expireDate);
                row[4] = String.valueOf(applicationDate);
                row[5] = String.valueOf(admittedBy);
                row[6] = String.valueOf(payment == null ? "None" : payment + "₺");
                row[7] = String.valueOf(paymentExpire == null ? "None" : paymentExpire);

                table.add(row);
            }

            DatabaseUtilities.printTable(table,true);

            getString("Press Enter to continue",true);

            //redirect if another process requests
            if(modelData.transferData.containsKey("redirectFunction")){
                modelData.transferData.put("listApplicationFlag",true);
                modelData.transferData.put("applicationList",applicationList);
                String function = (String) modelData.transferData.get("redirectFunction");
                String operation = (String) modelData.transferData.get("redirectOperation");
                return new ViewData(function,operation,modelData.transferData);
            }

        }
        return new ViewData("Screen", "screen.gui");
    }


    }
