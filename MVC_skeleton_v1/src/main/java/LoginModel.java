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
        sql.append("	Username,  Password,Name,Surname, Title,InstitutionName,InstitutionId, U.PersonnelId, LastLogin  ");
        sql.append(" FROM UserInformation AS U "+
                    "INNER JOIN Personnel AS P "+
                    "ON U.PersonnelId = P.PersonnelId "+
                    "INNER JOIN Institution AS I " +
                    "ON P.Department = I.InstitutionId \n");

        List<Map.Entry<String, Object>> whereParameterList = DatabaseUtilities.createWhereParameterList(whereParameters);
        sql.append(DatabaseUtilities.prepareWhereStatement(whereParameterList));

        String lastLogin = " UPDATE UserInformation SET LastLogin = CONVERT(smalldatetime,CURRENT_TIMESTAMP) WHERE Username = ?";
        if(DatabaseUtilities.monitoring)
            System.out.println(sql.toString() + "\n");


        // execute constructed SQL statement
        Connection connection = DatabaseUtilities.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
        PreparedStatement lastLoginUpdate = connection.prepareStatement(lastLogin);
        DatabaseUtilities.setWhereStatementParameters(preparedStatement, whereParameterList);
        lastLoginUpdate.setString(1, (String) whereParameters.get("username"));
        ResultSet result = preparedStatement.executeQuery();
        lastLoginUpdate.executeUpdate();

        return result;
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
