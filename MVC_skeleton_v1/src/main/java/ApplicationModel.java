import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class ApplicationModel implements ModelInterface{
    @Override
    public ResultSet select(Map<String, Object> viewParameters) throws Exception {

        Map<String, Object> whereParameters = (Map<String, Object>)(viewParameters.get("whereParameters"));

        // construct SQL statement
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append("	AD.ApplicationNumber, S.Status, MandatoryFlag, ExpireDate , ApplicationDate ");
        sql.append(" FROM Admission AS AD INNER JOIN Application AS AP " +
                "ON AD.ApplicationNumber = AP.ApplicationNumber "+
                "INNER JOIN Status AS S " +
                "ON AP.Status = S.StatusNumber ");

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
    public int insert(Map<String, Object> insertParameters) throws Exception {

        String applicationFieldName = (String)insertParameters.get("ApplicationFieldName");
        String applicationFormFieldName = (String)insertParameters.get("ApplicationFormFieldName");
        String applierFieldName = (String)insertParameters.get("ApplierFieldName");

        //get table objects
        Applier applier = (Applier)insertParameters.get("Applier");
        Application application = (Application)insertParameters.get("Application");
        ApplicationForm applicationForm = (ApplicationForm)insertParameters.get("ApplicationForm");

        // construct Application SQL statement
        StringBuilder sqlApplication = new StringBuilder();
        sqlApplication.append(" INSERT INTO Application (" + applicationFieldName + ") " );
        sqlApplication.append(" VALUES ");

        String[] fieldList = applicationFieldName.split(",");

        sqlApplication.append("(");
        for (int j=0; j<fieldList.length; j++) {
            String fieldName = fieldList[j].trim();
            sqlApplication.append(DatabaseUtilities.formatField(application.getByName(fieldName)));
            if (j < fieldList.length - 1) {
                sqlApplication.append(", ");
            }
        }
        sqlApplication.append(")");

        sqlApplication.append(" SELECT SCOPE_IDENTITY() AS LastID");

        if(DatabaseUtilities.monitoring)
            System.out.println(sqlApplication.toString());

        //Insert Application and get an ID
        Connection connection = DatabaseUtilities.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlApplication.toString());
        ResultSet result = preparedStatement.executeQuery();
        int lastID = result.getInt("LastID");
        preparedStatement.close();

        applier.setApplicationNumber(lastID);
        applicationForm.setApplicationNumber(lastID);



        // construct ApplicationForm SQL statement
        StringBuilder sqlApplicationForm = new StringBuilder();
        sqlApplicationForm.append(" INSERT INTO ApplicationForm (" + applicationFieldName + ") " );
        sqlApplicationForm.append(" VALUES ");

        fieldList = applicationFieldName.split(",");

        sqlApplicationForm.append("(");
        for (int j=0; j<fieldList.length; j++) {
            String fieldName = fieldList[j].trim();
            sqlApplicationForm.append(DatabaseUtilities.formatField(applicationForm.getByName(fieldName)));
            if (j < fieldList.length - 1) {
                sqlApplicationForm.append(", ");
            }
        }
        sqlApplicationForm.append(")");

        if(DatabaseUtilities.monitoring)
            System.out.println(sqlApplicationForm.toString());

        // construct Applier SQL statement
        StringBuilder sqlApplier = new StringBuilder();
        sqlApplier.append(" INSERT INTO ApplicationForm (" + applicationFieldName + ") " );
        sqlApplier.append(" VALUES ");

        fieldList = applierFieldName.split(",");

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

        connection = DatabaseUtilities.getConnection();
        preparedStatement = connection.prepareStatement(sqlApplication.toString());
        preparedStatement.executeUpdate();
        preparedStatement.close();


        return lastID;
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
