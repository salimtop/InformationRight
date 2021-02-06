public class Screen {


    public static String getByColumnName(String columnName){

        switch(columnName){
            case "newApplication" : return "New Application";
            case "listApplication" : return "List Applications";
            case "listAllApplication" : return "List All Applications";
            case "admitApplication" : return "Admit Application";
            case "showApplication" : return "Show Application";
            case "showInstitution" : return "Show Institutions";
            case "Quit" : return "Quit";
        }
        System.out.println(columnName);
        return "Invalid Column";
    }

}
