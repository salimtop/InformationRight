import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApplicationFormModel implements ModelInterface{
    @Override
    public ResultSet select(Map<String, Object> viewParameters) throws Exception {
        Map<String, Object> whereParameters = (Map<String, Object>)(viewParameters.get("whereParameters"));

        // construct SQL statement
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append(" Name,LastName,AF.ApplicationNumber,IsInformation,Request,DT.DeliveryType,Data,DaT.DataType ");
        sql.append(" FROM ApplicationForm AS AF INNER JOIN InformationAndDocument AS IAD\n" +
                "        ON IAD.ApplicationNumber = AF.ApplicationNumber\n" +
                "        INNER JOIN Applier AS APL\n" +
                "        ON APL.ApplicationNumber = AF.ApplicationNumber\n" +
                "        INNER JOIN DeliveryType AS DT\n" +
                "        ON DT.DeliveryTypeId = AF.DesiredDeliveryType\n "+
                "        INNER JOIN DataType AS DaT\n" +
                "        ON DaT.DataTypeId = IAD.DataType\n");


        if((boolean) viewParameters.get("justAdmitted"))
            sql.append("        INNER JOIN Admission AS ADM\n" +
                    "        ON ADM.ApplicationNumber = AF.ApplicationNumber ");


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
    public Integer insert(Map<String, Object> insertParameters) throws Exception {
        String applicationFormFieldName = (String)insertParameters.get("ApplicationFormFieldName");
        String informationAndDocumentFieldName = ApplicationForm.InformationAndDocument.getFieldNames();

        //get table objects
        ApplicationForm applicationForm = (ApplicationForm)insertParameters.get("ApplicationForm");

        // construct Applier SQL statement
        StringBuilder sqlApplicationForm = new StringBuilder();
        sqlApplicationForm.append("BEGIN TRAN INSERT INTO ApplicationForm (" + applicationFormFieldName + ") " );
        sqlApplicationForm.append(" VALUES ");

        String[] fieldList = applicationFormFieldName.split(",");

        sqlApplicationForm.append("(");
        for (int j=0; j<fieldList.length; j++) {
            String fieldName = fieldList[j].trim();
            sqlApplicationForm.append(DatabaseUtilities.formatField(applicationForm.getByName(fieldName)));
            if (j < fieldList.length - 1) {
                sqlApplicationForm.append(", ");
            }
        }
        sqlApplicationForm.append(")");


        //Insert requested data;
        ArrayList<ApplicationForm.InformationAndDocument> requestedData = applicationForm.dataList;

        for(int i = 0; i < requestedData.size(); i++){

            sqlApplicationForm.append("\n INSERT INTO InformationAndDocument ( "+
                    ApplicationForm.InformationAndDocument.getFieldNames() + ")");
            sqlApplicationForm.append(" VALUES ");

            fieldList = informationAndDocumentFieldName.split(",");

            sqlApplicationForm.append("(");
            for (int j=0; j<fieldList.length; j++) {
                String fieldName = fieldList[j].trim();
                sqlApplicationForm.append(DatabaseUtilities.formatField(requestedData.get(i).getByName(fieldName)));
                if (j < fieldList.length - 1) {
                    sqlApplicationForm.append(", ");
                }
            }
            sqlApplicationForm.append(") \n");

        }

        sqlApplicationForm.append(" COMMIT");


        if(DatabaseUtilities.monitoring)
            System.out.println(sqlApplicationForm.toString());

        // execute constructed SQL statement

        //Insert applier and get an ID
        Connection connection = DatabaseUtilities.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlApplicationForm.toString());
        preparedStatement.executeUpdate();
        preparedStatement.close();



        return (Integer) insertParameters.get("lastId");
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
