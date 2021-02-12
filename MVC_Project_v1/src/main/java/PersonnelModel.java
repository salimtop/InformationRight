import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

public class PersonnelModel implements ModelInterface{
    @Override
    public ResultSet select(Map<String, Object> viewParameters) throws Exception {
        // construct SQL statement
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT \n");
        sql.append(" PersonnelId, Name, Surname, Title, I.InstitutionName,Department \n");
        sql.append(" FROM Personnel AS P INNER JOIN Institution AS I\n" +
                "ON P.Department = I.InstitutionId \n");

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
        Personnel personnel = (Personnel) insertParameters.get("Personnel");
        String personnelFieldName = (String)insertParameters.get("PersonnelFieldName");

        // construct Personnel SQL statement
        StringBuilder sqlPersonnel = new StringBuilder();
        sqlPersonnel.append(" INSERT INTO Personnel (" + personnelFieldName + ") " );
        sqlPersonnel.append(" VALUES ");

        String[] fieldList = personnelFieldName.split(",");

        sqlPersonnel.append("(");
        for (int j=0; j<fieldList.length; j++) {
            String fieldName = fieldList[j].trim();
            sqlPersonnel.append(DatabaseUtilities.formatField(personnel.getByName(fieldName)));
            if (j < fieldList.length - 1) {
                sqlPersonnel.append(", ");
            }
        }
        sqlPersonnel.append(")");



        if(DatabaseUtilities.monitoring)
            System.out.println(sqlPersonnel.toString());


        // execute constructed SQL statement

        //Insert personnel and get an ID
        Connection connection = DatabaseUtilities.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlPersonnel.toString(), Statement.RETURN_GENERATED_KEYS);
        preparedStatement.executeUpdate();

        ResultSet result = preparedStatement.getGeneratedKeys();

        Integer lastId = null;
        if (result.next()) {
            lastId = result.getInt(1);
        }

        preparedStatement.close();

        personnel.setPersonalId(lastId);


        return lastId;
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
