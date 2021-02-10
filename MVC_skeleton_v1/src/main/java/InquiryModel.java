import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class InquiryModel implements ModelInterface{


    @Override
    public ResultSet select(Map<String, Object> viewParameters) throws Exception {
        Map<String, Object> whereParameters = (Map<String, Object>)(viewParameters.get("whereParameters"));

        // construct SQL statement
        StringBuilder sql = new StringBuilder();

        if(viewParameters.get("inquiryType").equals("applicationDetails")) {
            sql.append("UPDATE Application SET StatusType = 2\n" +
                            "WHERE PaymentExpire  < CURRENT_TIMESTAMP");
            sql.append(" SELECT ");
            sql.append("	A.ApplicationNumber, ST.StatusType, A.MandatoryFlag,\n" +
                    "    DT.DeliveryType, A.ApplicationDate, A.ExpireDate,\n" +
                    "    A.PaymentAmount, A.PaymentExpire , RT.RejectionType ");

            sql.append(" FROM Application AS A INNER JOIN StatusType AS ST\n" +
                    "   ON A.StatusType = ST.StatusTypeId\n" +
                    "   INNER JOIN DeliveryType AS DT\n" +
                    "   ON DT.DeliveryTypeId = A.AdmissionDeliveryType\n" +
                    "   LEFT OUTER JOIN RejectionType AS RT\n" +
                    "   ON A.RejectionType = RT.RejectionTypeId\n ");

            sql.append(" WHERE ApplicationNumber = ? \n");
        }
        else if(viewParameters.get("inquiryType").equals("respondDetails")) {
            sql.append("SELECT\n" +
                    "Respond,RespondDate,\n" +
                    "I.InstitutionName,AdmissionDate\n" +
                    "FROM RESPOND AS R\n" +
                    "LEFT OUTER JOIN Admission AS ADM\n" +
                    "ON R.ApplicationNumber = ADM.ApplicationNumber\n" +
                    "LEFT OUTER JOIN Institution AS I\n" +
                    "ON ADM.AdmittedBy = I.InstitutionId\n");

            sql.append(" WHERE R.ApplicationNumber = ?\n");
            sql.append(" ORDER BY RespondDate DESC\n");
        }

        if(DatabaseUtilities.monitoring)
            System.out.println(sql.toString() + "\n");

        // execute constructed SQL statement
        Connection connection = DatabaseUtilities.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
        preparedStatement.setInt(1, (Integer) viewParameters.get("applicationNumber"));
        ResultSet result = preparedStatement.executeQuery();

        return result;
    }

    @Override
    public Integer insert(Map<String, Object> insertParameters) throws Exception {
        return null;
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
