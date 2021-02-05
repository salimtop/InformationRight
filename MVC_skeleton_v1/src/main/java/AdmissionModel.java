import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class AdmissionModel implements ModelInterface{
    @Override
    public ResultSet select(Map<String, Object> viewParameters) throws Exception {
        Map<String, Object> whereParameters = (Map<String, Object>)(viewParameters.get("whereParameters"));

        // construct SQL statement
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");

        sql.append(" AP.ApplicationNumber, S.StatusType, MandatoryFlag, ExpireDate , ApplicationDate,I.InstitutionName AS AdmittedBy ");
        sql.append(" FROM Application AS AP LEFT OUTER JOIN Admission AS AD \n" +
                "            ON AP.ApplicationNumber = AD.ApplicationNumber \n" +
                "        INNER JOIN StatusType AS S \n" +
                "            ON AP.Status = S.StatusTypeID \n" +
                "        LEFT OUTER JOIN Institution AS I \n" +
                "            ON AD.AdmittedBy = I.InstitutionId ");

        List<Map.Entry<String, Object>> whereParameterList = DatabaseUtilities.createWhereParameterList(whereParameters);
        sql.append(DatabaseUtilities.prepareWhereStatement(whereParameterList));

        if(viewParameters.containsKey("ORDER BY"))
            sql.append("ORDER BY "+ viewParameters.get("ORDER BY"));

        if(DatabaseUtilities.monitoring)
            System.out.println(sql.toString() + "\n");


        // execute constructed SQL statement
        Connection connection = DatabaseUtilities.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
        DatabaseUtilities.setWhereStatementParameters(preparedStatement, whereParameterList);
        ResultSet result = preparedStatement.executeQuery();

        return result;
    }

    @Override
    public Integer insert(Map<String, Object> insertParameters) throws Exception {
        return null;
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
