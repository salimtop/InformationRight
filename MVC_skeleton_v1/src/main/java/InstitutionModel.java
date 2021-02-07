import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class InstitutionModel implements ModelInterface{

    @Override
    public ResultSet select(Map<String, Object> viewParameters) throws Exception {
         // construct SQL statement
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append("I.InstitutionId, \n" +
                "I.InstitutionName,\n" +
                "I.Address,I.Telephone,\n" +
                "(CASE WHEN IPS.InstitutionName IS NULL THEN '-' ELSE IPS.InstitutionName END) AS Superior\n");
        sql.append("FROM Institution AS I INNER JOIN InstitutionType AS IT\n" +
                "ON I.InstitutionType = IT.InstitutionTypeId\n" +
                "LEFT OUTER JOIN Institution AS IPS\n" +
                "ON IPS.InstitutionId = I.Superior\n");


        if(DatabaseUtilities.monitoring)
            System.out.println(sql.toString() + "\n");


        // execute constructed SQL statement
        Connection connection = DatabaseUtilities.getConnection();
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(sql.toString());

        return result;
    }

    @Override
    public Integer insert(Map<String, Object> insertParameters) throws Exception {
        return null;
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
