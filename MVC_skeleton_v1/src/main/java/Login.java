
import java.util.HashMap;

public class Login {

    private static volatile Login instance = null;

    private String username;
    private String name;
    private String surname;
    private Integer personalId;
    private HashMap<Integer,String> screen;

    private static Object lock = new Object();

   private Login(){


   }

    public static Login instance(String username, String name, String surname, Integer personalId) {
        if (instance == null) {
            // Double checked locking
            synchronized (lock) {
                if (instance == null) {
                    instance = new Login();
                    instance.username = username;
                    instance.name = name;
                    instance.surname = surname;
                    instance.personalId = personalId;


                }
            }
        }
        return instance;
    }

    public static Login getInstance() {

        if(instance.username == null){
            System.out.println("User is not initialized");
            return null;
        }
        return instance;
    }


    public static String getUsername() {
        return instance.username;
    }

    public static void setScreen(HashMap<Integer, String> screen) {

        instance.screen = screen;
    }

    public static HashMap<Integer, String> getScreen() {
        return instance.screen;
    }
}
