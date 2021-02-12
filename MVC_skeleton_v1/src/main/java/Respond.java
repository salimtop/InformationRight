import java.util.Date;

public class Respond {

    Integer applicationNumber;
    String respond;
    Date respondDate;
    Integer respondedBy;
    Boolean rejected;
    Integer rejectedType;


    public Respond(Integer applicationNumber, String respond, Date respondDate, Integer respondedBy) {
        this.applicationNumber = applicationNumber;
        this.respond = respond;
        this.respondDate = respondDate;
        this.respondedBy = respondedBy;

    }

    public static String getFieldNames(){
        return "applicationNumber, respond, respondDate, respondedBy ";
    }

    public Object getByName(String fieldName){
        switch (fieldName){
            case "applicationNumber" : return applicationNumber;
            case "respond" : return respond;
            case "respondDate" : return respondDate;
            case "respondedBy" : return respondedBy;

        }

        return null;
    }
}
