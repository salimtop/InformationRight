import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdmissionModel implements ModelInterface{
    @Override
    public ResultSet select(Map<String, Object> viewParameters) throws Exception {
        Map<String, Object> whereParameters = (Map<String, Object>)(viewParameters.get("whereParameters"));

        // construct SQL statement
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");

        sql.append("  AP.ApplicationNumber, S.StatusType, AP.MandatoryFlag, AP.ExpireDate , AP.ApplicationDate,I.InstitutionName AS AdmittedBy,AP.PaymentAmount,AP.PaymentExpire  ");
        sql.append(" FROM Application AS AP LEFT OUTER JOIN Admission AS AD \n" +
                "            ON AP.ApplicationNumber = AD.ApplicationNumber \n" +
                "        INNER JOIN StatusType AS S \n" +
                "            ON AP.StatusType = S.StatusTypeID \n" +
                "        LEFT OUTER JOIN Institution AS I \n" +
                "            ON AD.AdmittedBy = I.InstitutionId \n"+
                "        INNER JOIN Application A\n" +
                "        ON A.ApplicationNumber = AP.ApplicationNumber");

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
        String admissionFieldName = (String)insertParameters.get("AdmissionFieldName");
        
        //get table objects
        Admission admission = (Admission)insertParameters.get("Admission");

        // construct Applier SQL statement
        StringBuilder sqlAdmission = new StringBuilder();
        sqlAdmission.append("INSERT INTO Admission (" + admissionFieldName + ") " );
        sqlAdmission.append(" VALUES ");

        String[] fieldList = admissionFieldName.split(",");

        sqlAdmission.append("(");
        for (int j=0; j<fieldList.length; j++) {
            String fieldName = fieldList[j].trim();
            if(fieldName.equals("admissionDate"))
                sqlAdmission.append(" CONVERT(DATE, CURRENT_TIMESTAMP) ");
            else
                sqlAdmission.append(DatabaseUtilities.formatField(admission.getByName(fieldName)));

            if (j < fieldList.length - 1) {
                sqlAdmission.append(", ");
            }
        }
        sqlAdmission.append(")");

        if(DatabaseUtilities.monitoring)
            System.out.println(sqlAdmission.toString());

        // execute constructed SQL statement

        Connection connection = DatabaseUtilities.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlAdmission.toString());
        Integer result = preparedStatement.executeUpdate();
        preparedStatement.close();

        if(result > 0)
            return admission.applicationNumber;
        return -1;
    }

    @Override
    public int update(Map<String, Object> parameters) throws Exception {
        Map<String, Object> updateParameters = (Map<String, Object>)(parameters.get("updateParameters"));
        Map<String, Object> whereParameters = (Map<String, Object>)(parameters.get("whereParameters"));

        // construct SQL statement
        StringBuilder sql = new StringBuilder();

        if(parameters.containsKey("updateChoice") && parameters.get("updateChoice").equals("forwardUpdate")){
            sql.append("\n BEGIN TRAN" +
                    " INSERT INTO Admission (AdmittedBy,ApplicationNumber,AdmissionDate) \n"+
                    " VALUES ("+
                    parameters.get("forwardInstitution")+","+
                    whereParameters.get("applicationNumber")+", "+
                    "CONVERT(DATE, CURRENT_TIMESTAMP) "+")\n"
            );

            sql.append(
                    "UPDATE Application SET ExpireDate = CONVERT(DATE, DATEADD(DAY,15,CURRENT_TIMESTAMP))\n" +
                            "WHERE ApplicationNumber = "+whereParameters.get("applicationNumber")
            );
        }


        sql.append(" UPDATE Admission SET ");
        int appendCount = 0;
        for (Map.Entry<String, Object> entry : updateParameters.entrySet()) {

            sql.append(entry.getKey() + " = " + DatabaseUtilities.formatField(entry.getValue()));
            if (++appendCount < updateParameters.size()) {
                sql.append(", ");
            }
        }
        List<Map.Entry<String, Object>> whereParameterList = DatabaseUtilities.createWhereParameterList(whereParameters);
        sql.append(DatabaseUtilities.prepareWhereStatement(whereParameterList));

        sql.append(" COMMIT");
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
