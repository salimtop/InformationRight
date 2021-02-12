import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TypeTableView implements ViewInterface{

    @Override
    public ViewData create(ModelData modelData, String functionName, String operationName) throws Exception {

        switch (operationName){
            case "loadChoice" : return retrieveApplication(modelData);
            case "select" : return redirect(modelData);

        }


        return null;

    }

    private ViewData redirect(ModelData modelData) {
        //redirect if another process requests
        if(modelData.transferData.containsKey("redirectFunction")){
            modelData.transferData.put("typeTableFlag",true);
            String function = (String) modelData.transferData.get("redirectFunction");
            String operation = (String) modelData.transferData.get("redirectOperation");
            return new ViewData(function,operation,modelData.transferData);
        }
        return new ViewData("MainMenu","");
    }


    private ViewData retrieveApplication(ModelData modelData) {

        System.out.println("Application Form is loading...");
        Map<String,Object> parameters = modelData.transferData == null ?  new HashMap<String, Object>() : modelData.transferData;
        Map<String,Object> types = new HashMap<>();

        parameters.put("types",types);

        HashMap<String,Object> telephoneType = new HashMap<>();
        HashMap<String,Object> addressType = new HashMap<>();
        HashMap<String,Object> applierType = new HashMap<>();
        HashMap<String,Object> deliveryType = new HashMap<>();
        HashMap<String,Object> statusType = new HashMap<>();
        HashMap<String,Object> dataType = new HashMap<>();
        HashMap<String,Object> rejectionType = new HashMap<>();
        HashMap<String,Object> screenType = new HashMap<>();

        types.put("TelephoneType",telephoneType);
        types.put("AddressType",addressType);
        types.put("ApplierType",applierType);
        types.put("DeliveryType",deliveryType);
        types.put("StatusType",statusType);
        types.put("DataType",dataType);
        types.put("RejectionType",rejectionType);
        types.put("ScreenType",screenType);


        return new ViewData("TypeTable", "select", parameters);

    }
}
