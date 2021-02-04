import java.util.HashMap;
import java.util.Map;

public class ApplicationForm {

    Integer applicationNumber;
    String request;
    Integer desiredDeliveryType;

    public static Map<String, Object> types = new HashMap<>();


    public ApplicationForm(Integer applicationNumber, String request, Integer desiredDeliveryType) {
        this.applicationNumber = applicationNumber;
        this.request = request;
        this.desiredDeliveryType = desiredDeliveryType;
    }

    public Object getByName(String attributeName) {
        switch (attributeName) {
            case "ApplicationNumber": return applicationNumber;
            case "request": return request;
            case "desiredDeliveryType": return desiredDeliveryType;

            default: return null;
        }
    }

    public static String getFieldNames(){
        return " applicationNumber, request, desiredDeliveryType";
    }

    public void setApplicationNumber(Integer applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public static HashMap<Integer,Object> showType(String type){
        HashMap<Integer,Object> typeList = (HashMap<Integer, Object>) ApplicationForm.types.get(type);

        for(Map.Entry<Integer, Object> entry : typeList.entrySet()){
            System.out.println(""+entry.getKey()+" - "+entry.getValue());
        }

        return typeList;
    }

}
