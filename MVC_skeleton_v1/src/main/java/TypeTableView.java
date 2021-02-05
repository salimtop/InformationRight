import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TypeTableView implements ViewInterface{

    @Override
    public ViewData create(ModelData modelData, String functionName, String operationName) throws Exception {

        switch (operationName){
            case "loadChoice" : return retrieveApplication(modelData);
            case "select" : return new ViewData("Applier","registerApplier");

        }


        return null;

    }


    private ViewData retrieveApplication(ModelData modelData) {

        System.out.println("Application Form is loading...");

        Map<String,Object> types = new HashMap<>();

        HashMap<String,Object> telephoneType = new HashMap<>();
        HashMap<String,Object> addressType = new HashMap<>();
        HashMap<String,Object> applierType = new HashMap<>();
        HashMap<String,Object> deliveryType = new HashMap<>();
        HashMap<String,Object> statusType = new HashMap<>();
        HashMap<String,Object> dataType = new HashMap<>();

        types.put("TelephoneType",telephoneType);
        types.put("AddressType",addressType);
        types.put("ApplierType",applierType);
        types.put("DeliveryType",deliveryType);
        types.put("StatusType",statusType);
        types.put("DataType",dataType);


        return new ViewData("TypeTable", "select", types);

    }
}
