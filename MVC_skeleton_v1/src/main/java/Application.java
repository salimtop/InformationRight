import java.util.Date;

public class Application {


    Integer applicationNumber;
    Integer status;
    Boolean	mandatory;
    Integer admissionDeliveryType;
    Date applicationDate;
    Date expireDate;
    Integer paymentNumber;
    Date paymentExpire;
    Integer relatedApplication;


    public Application(Integer applicationNumber, Integer status, Boolean mandatory, Integer admissionDeliveryType, Date applicationDate, Date expireDate, Integer paymentNumber, Date paymentExpire, Integer relatedApplication) {
        this.applicationNumber = applicationNumber;
        this.status = status;
        this.mandatory = mandatory;
        this.admissionDeliveryType = admissionDeliveryType;
        this.applicationDate = applicationDate;
        this.expireDate = expireDate;
        this.paymentNumber = paymentNumber;
        this.paymentExpire = paymentExpire;
        this.relatedApplication = relatedApplication;
    }

    public Object getByName(String attributeName) {
        switch (attributeName) {
            case "ApplicationNumber": return applicationNumber;
            case "statusType": return status;
            case "mandatoryFlag": return mandatory;
            case "admissionDeliveryType": return admissionDeliveryType;
            case "applicationDate": return applicationDate;
            case "expireDate": return expireDate;
            case "paymentNumber": return paymentNumber;
            case "paymentExpire": return paymentExpire;
            case "relatedApplication": return relatedApplication;

            default: return null;
        }
    }

    public static String getFieldNames(){

        return " ApplicationNumber, statusType , mandatoryFlag, admissionDeliveryType, applicationDate, expireDate, "+
                " paymentNumber, paymentExpire, relatedApplication ";
    }
    public static String getOrderColumn(Integer attribute) throws Exception {

        switch(attribute){
            case 1: return "ApplicationNumber";
            case 2: return "StatusType";
            case 3 : return "MandatoryFlag";
            case 4 : return "ApplicationDate";
            case 5 : return "ExpireDate";
            case 6 : return "AdmittedBy";
        }

        if(attribute == null)
            return null;

        throw new Exception("Invalid column name");
    }

    public static void orderList(){
        System.out.println("1 - Application Number");
        System.out.println("2 - Application Status");
        System.out.println("3 - Mandatory Respond");
        System.out.println("4 - Application Date");
        System.out.println("5 - Application Expire Date");
        System.out.println("6 - Admitted By");

    }

    public void setApplicationNumber(Integer applicationNumber) {
        this.applicationNumber = applicationNumber;
    }
}
