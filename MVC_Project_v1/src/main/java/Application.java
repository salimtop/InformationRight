import java.util.Date;

public class Application {


    Integer applicationNumber;
    Integer status;
    Boolean	mandatory;
    Integer admissionDeliveryType;
    Date applicationDate;
    Date expireDate;
    Integer paymentAmount;
    Date paymentExpire;
    Integer relatedApplication;
    Integer rejectionType;


    public Application(Integer applicationNumber, Integer status, Boolean mandatory, Integer admissionDeliveryType, Date applicationDate, Date expireDate, Integer paymentAmount, Date paymentExpire, Integer relatedApplication) {
        this.applicationNumber = applicationNumber;
        this.status = status;
        this.mandatory = mandatory;
        this.admissionDeliveryType = admissionDeliveryType;
        this.applicationDate = applicationDate;
        this.expireDate = expireDate;
        this.paymentAmount = paymentAmount;
        this.paymentExpire = paymentExpire;
        this.relatedApplication = relatedApplication;
    }

    public Application(Integer applicationNumber, Boolean mandatory, Date applicationDate, Date expireDate) {
        this.applicationNumber = applicationNumber;
        this.mandatory = mandatory;
        this.applicationDate = applicationDate;
        this.expireDate = expireDate;
    }

    public Object getByName(String attributeName) {
        switch (attributeName) {
            case "ApplicationNumber": return applicationNumber;
            case "statusType": return status;
            case "mandatoryFlag": return mandatory;
            case "admissionDeliveryType": return admissionDeliveryType;
            case "applicationDate": return applicationDate;
            case "expireDate": return expireDate;
            case "paymentAmount": return paymentAmount;
            case "paymentExpire": return paymentExpire;
            case "relatedApplication": return relatedApplication;

            default: return null;
        }
    }

    public static String getFieldNames(){

        return " ApplicationNumber, statusType , mandatoryFlag, admissionDeliveryType, applicationDate, expireDate, "+
                " paymentAmount, paymentExpire, relatedApplication ";
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

    public Integer getRejectionType() {
        return rejectionType;
    }

    public void setRejectionType(Integer rejectionType) {
        this.rejectionType = rejectionType;
    }

    public void setApplicationNumber(Integer applicationNumber) {
        this.applicationNumber = applicationNumber;
    }
}
