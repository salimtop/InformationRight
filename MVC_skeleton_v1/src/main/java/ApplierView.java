import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class ApplierView implements ViewInterface{
    @Override
    public ViewData create(ModelData modelData, String functionName, String operationName) throws Exception {

        switch (operationName){
            case "registerApplier" : return registerApplier(modelData);
            case "insert" : return openApplicationForm(modelData);
        }

        return null;
    }

    private ViewData openApplicationForm(ModelData modelData) {

        Integer lastID = (Integer)modelData.transferData.get("lastId");

        if(lastID == null)
            System.out.println("Applier ID could not be retrieved");

        HashMap<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("lastId",lastID);

        return new ViewData("ApplicationForm","fillForm",parameters);
    }

    private ViewData registerApplier(ModelData modelData) throws ParseException {

        Map<String, Object> parameters = modelData.transferData == null ?  new HashMap<String, Object>() : modelData.transferData;

        if(TypeTable.types == null){
            parameters.put("redirectFunction","Applier");
            parameters.put("redirectOperation","registerApplier");
            return new ViewData("TypeTable","loadChoice",parameters);
        }

        Applier applier = createApplierGUI();

        parameters.put("Applier",applier);
        parameters.put("ApplierFieldName",Applier.getFieldNames());

        return new ViewData("Applier","insert",parameters);

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

        HashMap<Integer, Object> applierTypes = TypeTable.showType("ApplierType");

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

        String address = getString("Address: ",false);

        HashMap<Integer, Object> addressTypes = TypeTable.showType("AddressType");

        Integer addressType;
        do {
            addressType = getInteger("Enter Address Type :",false);
        }while(!addressTypes.containsKey(addressType));

        Double telephone = getDouble("Telephone: ",true);

        HashMap<Integer, Object> telephoneTypes = TypeTable.showType("TelephoneType");

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
