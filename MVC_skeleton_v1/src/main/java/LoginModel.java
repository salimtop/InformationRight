import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class LoginModel implements ModelInterface {


    @Override
    public ResultSet select(Map<String, Object> viewParameters) throws Exception {

        Map<String, Object> whereParameters = (Map<String, Object>)(viewParameters.get("whereParameters"));

        // construct SQL statement
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append("	Username, Password,Name,Surname, Title,InstitutionName,InstitutionId, U.PersonalId, LastLogin  ");
        sql.append(" FROM UserInformation AS U "+
                    "INNER JOIN Personal AS P "+
                    "ON U.PersonalId = P.PersonalId "+
                    "INNER JOIN Institution AS I " +
                    "ON P.Department = I.InstitutionId ");

        List<Map.Entry<String, Object>> whereParameterList = DatabaseUtilities.createWhereParameterList(whereParameters);
        sql.append(DatabaseUtilities.prepareWhereStatement(whereParameterList));

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
