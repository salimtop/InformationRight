import java.util.HashMap;
import java.util.Map;

public class TypeTable {

    public static Map<String, Object> types;


    public static HashMap<Integer,Object> showType(String type){
        HashMap<Integer,Object> typeList = (HashMap<Integer, Object>) TypeTable.types.get(type);

        for(Map.Entry<Integer, Object> entry : typeList.entrySet()){
            System.out.println(""+entry.getKey()+" - "+entry.getValue());
        }

        return typeList;
    }

}
