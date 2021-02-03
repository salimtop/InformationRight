
import java.util.HashMap;

public class Login {

    static private String username;
    static private String name;
    static private String surname;
    static private Integer personalId;
    static private HashMap<Integer,String> screen;

   private Login(){

   }

   public Login(String username,String name,String surname, Integer personalId) {
       if(this.username == null){
           this.username = username;
           this.name = name;
           this.surname = surname;
           this.personalId = personalId;
       }
       else
           System.out.println("The user is already logged id");
    }

    public static String getUsername() {
        return username;
    }

    public static void setScreen(HashMap<Integer, String> screen) {

        Login.screen = screen;
    }

    public static HashMap<Integer, String> getScreen() {
        return screen;
    }
}
