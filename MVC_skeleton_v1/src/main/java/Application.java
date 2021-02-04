public class Application {


    public static String getOrderColumn(Integer attribute) throws Exception {

        switch(attribute){
            case 1: return "ApplicationNumber";
            case 2: return "Status";
            case 3 : return "MandatoryFlag";
            case 4 : return "ApplicationDate";
            case 5 : return "ExpireDate";
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

    }
}
