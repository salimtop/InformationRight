import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

public class RespondModel implements ModelInterface{
    @Override
    public ResultSet select(Map<String, Object> viewParameters) throws Exception {
        return null;
    }

    @Override
    public Integer insert(Map<String, Object> insertParameters) throws Exception {
        String respondFieldName = (String)insertParameters.get("RespondFieldName");

        //get table objects
        Respond respond = (Respond)insertParameters.get("Respond");

        // construct Respond SQL statement
        StringBuilder sqlRespond = new StringBuilder();
        sqlRespond.append(" INSERT INTO Respond (" + respondFieldName + ") " );
        sqlRespond.append(" VALUES ");

        String[] fieldList = respondFieldName.split(",");

        sqlRespond.append("(");
        for (int j=0; j<fieldList.length; j++) {
            String fieldName = fieldList[j].trim();
            if(fieldName.equalsIgnoreCase("respondDate"))
                sqlRespond.append(" CURRENT_TIMESTAMP ");
            else
                sqlRespond.append(DatabaseUtilities.formatField(respond.getByName(fieldName)));
            if (j < fieldList.length - 1) {
                sqlRespond.append(", ");
            }
        }
        sqlRespond.append(")");

        if(DatabaseUtilities.monitoring)
            System.out.println(sqlRespond.toString());


        // execute constructed SQL statement

        //Insert respond and get an ID
        Connection connection = DatabaseUtilities.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlRespond.toString(), Statement.RETURN_GENERATED_KEYS);
        Integer result = preparedStatement.executeUpdate();
        preparedStatement.close();

        if(result > 0)
            return respond.applicationNumber;
        return -1;
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
