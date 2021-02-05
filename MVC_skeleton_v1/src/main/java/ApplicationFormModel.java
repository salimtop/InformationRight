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


        return null;
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



        return (Integer) insertParameters.get("LastID");
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
