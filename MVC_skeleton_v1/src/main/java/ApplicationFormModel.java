import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class ApplicationFormModel implements ModelInterface{
    @Override
    public ResultSet select(Map<String, Object> viewParameters) throws Exception {
        Map<String, Object> tableList = (Map<String, Object>)(viewParameters);


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

        ApplicationForm.types = (Map<String, Object>)tableList;


        return null;
    }

    @Override
    public int insert(Map<String, Object> insertParameters) throws Exception {
        return 0;
    }

    @Override
    public int update(Map<String, Object> updateParameters, Map<String, Object> whereParameters) throws Exception {
        return 0;
    }

    @Override
    public int delete(Map<String, Object> whereParameters) throws Exception {
        return 0;
    }
}
