import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

public class ApplierModel implements ModelInterface {
    @Override
    public ResultSet select(Map<String, Object> viewParameters) throws Exception {
        return null;
    }

    @Override
    public Integer insert(Map<String, Object> insertParameters) throws Exception {
        String applierFieldName = (String)insertParameters.get("ApplierFieldName");

        //get table objects
        Applier applier = (Applier)insertParameters.get("Applier");

        // construct Applier SQL statement
        StringBuilder sqlApplier = new StringBuilder();
        sqlApplier.append(" INSERT INTO Applier (" + applierFieldName + ") " );
        sqlApplier.append(" VALUES ");

        String[] fieldList = applierFieldName.split(",");

        sqlApplier.append("(");
        for (int j=0; j<fieldList.length; j++) {
            String fieldName = fieldList[j].trim();
            sqlApplier.append(DatabaseUtilities.formatField(applier.getByName(fieldName)));
            if (j < fieldList.length - 1) {
                sqlApplier.append(", ");
            }
        }
        sqlApplier.append(")");

        if(DatabaseUtilities.monitoring)
            System.out.println(sqlApplier.toString());


        // execute constructed SQL statement

        //Insert applier and get an ID
        Connection connection = DatabaseUtilities.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlApplier.toString(), Statement.RETURN_GENERATED_KEYS);
        preparedStatement.executeUpdate();

        ResultSet result = preparedStatement.getGeneratedKeys();

        Integer lastId = null;
        if (result.next()) {
            lastId = result.getInt(1);
        }

        preparedStatement.close();

        applier.setApplicationNumber(lastId);


        return lastId;
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
