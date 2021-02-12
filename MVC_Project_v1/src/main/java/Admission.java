import java.util.Date;

public class Admission  {

    Integer admittedBy;
    Date admissionDate;
    Integer applicationNumber;
    Integer forwardedTo;

    public Admission(Integer admittedBy, Integer applicationNumber,Date admissionDate,  Integer forwardedTo) {
        this.admittedBy = admittedBy;
        this.admissionDate = admissionDate;
        this.applicationNumber = applicationNumber;
        this.forwardedTo = forwardedTo;
    }

    public Object getByName(String fieldName){

        switch(fieldName){
            case "admittedBy" : return admittedBy;
            case "admissionDate" : return admissionDate;
            case "applicationNumber" : return applicationNumber;
            case "forwardedTo" : return forwardedTo;
        }

        return null;
    }
    public static String getFieldNames(){
        return " admittedBy, admissionDate, applicationNumber, forwardedTo ";
    }
}
