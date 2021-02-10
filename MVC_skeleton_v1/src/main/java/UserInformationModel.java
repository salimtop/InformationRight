import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

public class UserInformationModel implements ModelInterface{
    @Override
    public ResultSet select(Map<String, Object> viewParameters) throws Exception {
        // construct SQL statement
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT \n");
        sql.append(" PersonnelId, Name, Surname, Title, I.InstitutionName,Department \n");
        sql.append(" FROM Personnel AS P INNER JOIN Institution AS I\n" +
                "ON P.Department = I.InstitutionId\n " +
                "WHERE PersonnelId NOT IN (SELECT PersonnelId FROM UserInformation) \n");

        if(DatabaseUtilities.monitoring)
            System.out.println(sql.toString() + "\n");

        // execute constructed SQL statement
        Connection connection = DatabaseUtilities.getConnection();
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(sql.toString());

        return result;
    }

    @Override
    public Integer insert(Map<String, Object> insertParameters) {
        // construct SQL statement
        StringBuilder sql = new StringBuilder();
        sql.append(" BEGIN TRAN INSERT INTO UserInformation  (PersonnelId,Username,Password,LastLogin) \n"+
                "VALUES (?,?,?,CONVERT(smalldatetime,CURRENT_TIMESTAMP)) \n" );



        //Add authorities
        if(insertParameters.containsKey("authority")){
            ArrayList<Integer> authorities = (ArrayList<Integer>) insertParameters.get("authority");

            for(int i = 0; i < authorities.size();i++){
                sql.append("INSERT INTO Authority (Username,ScreenType) \n"+
                        "VALUES('"+insertParameters.get("username")+"',"+authorities.get(i)+") \n");
            }
        }

        sql.append("COMMIT");

        if(DatabaseUtilities.monitoring)
            System.out.println(sql.toString());

        // execute constructed SQL statement
        //Insert
        Connection connection = null;
        Integer response;
        try {
            connection = DatabaseUtilities.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
            preparedStatement.setInt(1, (Integer) insertParameters.get("personnelId"));
            preparedStatement.setString(2, (String) insertParameters.get("username"));
            preparedStatement.setString(3,(String) insertParameters.get("password"));

            response = preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.toString());
           return -1;
        }
        return (Integer) insertParameters.get("personnelId");
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
