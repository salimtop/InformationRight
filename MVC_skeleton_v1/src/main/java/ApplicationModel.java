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

        sql.append(" AP.ApplicationNumber, S.StatusType, MandatoryFlag, ExpireDate , ApplicationDate,I.InstitutionName AS AdmittedBy ");
        sql.append(" FROM Application AS AP LEFT OUTER JOIN Admission AS AD \n" +
                "            ON AP.ApplicationNumber = AD.ApplicationNumber \n" +
                "        INNER JOIN StatusType AS S \n" +
                "            ON AP.StatusType = S.StatusTypeID \n" +
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
    public int update(Map<String, Object> updateParameters, Map<String, Object> whereParameters) throws Exception {
        return 0;
    }

    @Override
    public int delete(Map<String, Object> whereParameters) throws Exception {
        return 0;
    }
}
