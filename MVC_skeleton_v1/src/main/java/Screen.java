public class Screen {


    public static String getByColumnName(String columnName){

        switch(columnName){
            case "newApplication" : return "New Application";
            case "listApplication" : return "List Applications";
            case "listAllApplication" : return "List All Applications";
        }

        return "Invalid Column";
    }

}
