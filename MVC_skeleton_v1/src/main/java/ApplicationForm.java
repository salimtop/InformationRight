import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ApplicationForm {

    Integer applicationNumber;
    String request;
    Integer desiredDeliveryType;

    ArrayList<InformationAndDocument> dataList = new ArrayList<>();

    static class InformationAndDocument{
        public InformationAndDocument(Integer applicationNumber, String data, Boolean isInformation, Integer dataType) {
            this.applicationNumber = applicationNumber;
            this.data = data;
            this.isInformation = isInformation;
            this.dataType = dataType;
        }

        Integer applicationNumber;
        Integer informationDocumentNum;
       String data;
       Boolean isInformation;
       Integer dataType;

        public Object getByName(String attributeName) {
            switch (attributeName) {
                case "applicationNumber" : return applicationNumber;
                case "data": return data;
                case "IsInformation": return isInformation;
                case "dataType": return dataType;

                default: return null;
            }
        }


        public static String getFieldNames(){
            return "applicationNumber, data, IsInformation, dataType";
        }

    }




    public ApplicationForm(Integer applicationNumber, String request, Integer desiredDeliveryType) {
        this.applicationNumber = applicationNumber;
        this.request = request;
        this.desiredDeliveryType = desiredDeliveryType;
    }

    public Object getByName(String attributeName) {
        switch (attributeName) {
            case "applicationNumber": return applicationNumber;
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


}
