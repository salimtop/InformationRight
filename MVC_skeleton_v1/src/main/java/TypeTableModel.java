import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

public class TypeTableModel implements ModelInterface{
    @Override
    public ResultSet select(Map<String, Object> viewParameters) throws Exception {
        Map<String, Object> tableList = (Map<String, Object>)(viewParameters).get("types");


        //retrieves all form attribute types: ex: ApplierType, TelephoneType...
        Connection connection = DatabaseUtilities.getConnection();
        ResultSet result;
        Statement statement;

        for (Map.Entry<String, Object> entry : tableList.entrySet()){
            StringBuilder sql = new StringBuilder();
            String columns = entry.getKey()+"Id , "+ entry.getKey();

            Map<Integer,Object> table = (Map<Integer, Object>)entry.getValue();

            sql.append(" SELECT ");
            sql.append(columns);
            sql.append(" FROM "+entry.getKey()+" ");



            statement = connection.createStatement();
            result = statement.executeQuery(sql.toString());

            while(result.next()){
                int id = result.getInt(entry.getKey()+"ID");
                String type = result.getString(entry.getKey());
                table.put(id,type);
            }

        }

        TypeTable.types = tableList;


        return null;
    }

    @Override
    public Integer insert(Map<String, Object> insertParameters) throws Exception {
        return 0;
    }

    @Override
    public int update(Map<String, Object> parameters) throws Exception {
        return 0;
    }

    @Override
    public int delete(Map<String, Object> whereParameters) throws Exception {
        return 0;
    }
}
