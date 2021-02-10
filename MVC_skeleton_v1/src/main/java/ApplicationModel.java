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
        sql.append(" WITH S1 AS(SELECT ");

        sql.append(" A.ApplicationNumber, S.StatusType, MandatoryFlag, ExpireDate , ApplicationDate,\n" +
                "(SELECT TOP 1 I.InstitutionName \n" +
                "FROM Admission AS AD INNER JOIN Institution AS I ON AD.AdmittedBy = I.InstitutionId\n" +
                "WHERE ApplicationNumber = A.ApplicationNumber ORDER BY AdmissionDate DESC) AS AdmittedBy\n");
        sql.append(" FROM Application AS A\n" +
                "INNER JOIN StatusType AS S\n" +
                "ON A.StatusType = S.StatusTypeID  )\n" +
                "SELECT * FROM S1\n");

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

        String applicationFieldName = (String)insertParameters.get("ApplicationFieldName");
        Application application = (Application)insertParameters.get("Application");

        Integer lastId = (Integer) insertParameters.get("lastId");

        // construct Application SQL statement
        StringBuilder sqlApplication = new StringBuilder();
        sqlApplication.append(" INSERT INTO Application (" + applicationFieldName + ") " );
        sqlApplication.append(" VALUES ");

        String[] fieldList = applicationFieldName.split(",");
// INSERT INTO Application (  status, MandatoryFlag, admissionDeliveryType, applicationDate, expireDate,  paymentNumber, paymentExpire, relatedApplication )  VALUES ( 1, 0, 1, '2021-02-05', (DATEADD(DAY, 15, CONVERT (DATE, CURRENT_TIMESTAMP))), NULL, NULL, NULL) SELECT SCOPE_IDENTITY() AS LastID
        sqlApplication.append("(");
        for (int j=0; j<fieldList.length; j++) {
            String fieldName = fieldList[j].trim();
            if(fieldName.equals("applicationDate"))
                sqlApplication.append(" CONVERT (DATE, CURRENT_TIMESTAMP) ");
            else if(fieldName.equals("expireDate"))
                sqlApplication.append("(DATEADD(DAY, 15 , CONVERT (DATE, CURRENT_TIMESTAMP)))");
            else
                sqlApplication.append(DatabaseUtilities.formatField(application.getByName(fieldName)));
            if (j < fieldList.length - 1) {
                sqlApplication.append(", ");
            }
        }
        sqlApplication.append(")");



        if(DatabaseUtilities.monitoring)
            System.out.println(sqlApplication.toString());

        //Insert Application and get an ID
        Connection connection = DatabaseUtilities.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlApplication.toString());
        Integer dbResponse = preparedStatement.executeUpdate();
        preparedStatement.close();

        if(dbResponse != 0)
            return lastId;
        return -1;
    }



    @Override
    public int update(Map<String, Object> parameters) throws Exception {

        Map<String, Object> updateParameters = (Map<String, Object>)(parameters.get("updateParameters"));
        Map<String, Object> whereParameters = (Map<String, Object>)(parameters.get("whereParameters"));


        // construct SQL statement
        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE Application SET ");
        int appendCount = 0;
        for (Map.Entry<String, Object> entry : updateParameters.entrySet()) {

            if(entry.getKey().toString().contains("Expire"))
                sql.append(entry.getKey() + " =  CONVERT(DATE, DATEADD(DAY,15, CURRENT_TIMESTAMP)) ");
            else
                sql.append(entry.getKey() + " = " + DatabaseUtilities.formatField(entry.getValue()));
            if (++appendCount < updateParameters.size()) {
                sql.append(", ");
            }
        }
        List<Map.Entry<String, Object>> whereParameterList = DatabaseUtilities.createWhereParameterList(whereParameters);
        sql.append(DatabaseUtilities.prepareWhereStatement(whereParameterList));

        if(DatabaseUtilities.monitoring)
            System.out.println(sql.toString());


        // execute constructed SQL statement
        Connection connection = DatabaseUtilities.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
        DatabaseUtilities.setWhereStatementParameters(preparedStatement, whereParameterList);
        int rowCount = preparedStatement.executeUpdate();
        preparedStatement.close();

        return rowCount;
    }

    @Override
    public int delete(Map<String, Object> whereParameters) throws Exception {
        return 0;
    }
}
