public class Screen {


    public static String getByColumnName(String columnName){
        // Synchronise with DB
        switch(columnName){
            case "newApplication" : return "New Application";
            case "listAssigned" : return "List Assigned Applications";
            case "listAllApplication" : return "List All Applications";
            case "admitApplication" : return "Admit Application";
            case "showApplication" : return "Show Application";
            case "showInstitution" : return "Show Institutions";
            case "seeReport" : return "See Report";
            case "logout" : return "Log out";
            case "Quit" : return "Quit";
        }
        System.out.println(columnName);
        return "Invalid Column";
    }

}
