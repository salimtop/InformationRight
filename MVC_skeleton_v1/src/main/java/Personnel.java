public class Personnel {

    Integer personalId;
    String name;
    String surname;
    String title;
    Integer department;


    public Personnel(Integer personalId, String name, String surname, String title, Integer department) {
        this.personalId = personalId;
        this.name = name;
        this.surname = surname;
        this.title = title;
        this.department = department;
    }

    public static String getFieldName(){
        return "name, surname, title, department";
    }

    public Object getByName(String field){
        switch(field){

            case "personalId" : return personalId;
            case "name" : return name;
            case "surname" : return surname;
            case "title" : return title;
            case "department" : return department;
            default: return null;
        }
    }

    public void setPersonalId(Integer personalId) {
        this.personalId = personalId;
    }
}
