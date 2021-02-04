import java.util.HashMap;
import java.util.Map;

public class ApplicationFormView implements ViewInterface {


    @Override
    public ViewData create(ModelData modelData, String functionName, String operationName) throws Exception {
        switch (operationName){
            case "loadForm" : return loadApplication(modelData);
            case "select" : return printType(modelData);
        }

        return null;
    }

    private ViewData printType(ModelData modelData) {

        Map<String, Object> tableList = (Map<String, Object>)(ApplicationForm.types);

        for (Map.Entry<String, Object> entry : tableList.entrySet()){
            System.out.println(entry.toString());
        }

        return new ViewData("Application","createApplication");
    }

    private ViewData loadApplication(ModelData modelData) {
        Map<String,Object> types = new HashMap<>();

        HashMap<String,Object> telephoneType = new HashMap<>();
        HashMap<String,Object> addressType = new HashMap<>();
        HashMap<String,Object> applierType = new HashMap<>();
        HashMap<String,Object> deliveryType = new HashMap<>();
        HashMap<String,Object> statusType = new HashMap<>();

        types.put("TelephoneType",telephoneType);
        types.put("AddressType",addressType);
        types.put("ApplierType",applierType);
        types.put("DeliveryType",deliveryType);
        types.put("StatusType",statusType);


        return new ViewData("ApplicationForm", "select", types);

    }


}
