import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class ReportModel implements ModelInterface{
    @Override
    public ResultSet select(Map<String, Object> viewParameters) throws Exception {

        if( ! viewParameters.containsKey("statisticType")) {
            System.out.println("Missing parameter while getting statistics");
            return null;
        }
        // construct SQL statement
        StringBuilder sql = new StringBuilder();

        switch((String)viewParameters.get("statisticType")){
            case "generalStatistic" : sql.append("SELECT\n" +
                    "(SELECT\n" +
                    "COUNT(*) AS NUMBER FROM Application) AS TotalApplication,\n" +
                    "(SELECT COUNT(*) \n" +
                    "FROM Application AS A INNER JOIN StatusType AS ST\n" +
                    "ON A.StatusType = ST.StatusTypeId\n" +
                    "WHERE ST.StatusType NOT IN ('Rejected','Closed') ) AS Finalized,\n" +
                    "\n" +
                    "(SELECT COUNT(*) \n" +
                    "FROM Application AS A INNER JOIN StatusType AS ST\n" +
                    "ON A.StatusType = ST.StatusTypeId\n" +
                    "WHERE ST.StatusType = 'Closed') AS Positive,\n" +
                    "(SELECT COUNT(*) \n" +
                    "FROM Application AS A INNER JOIN StatusType AS ST\n" +
                    "ON A.StatusType = ST.StatusTypeId\n" +
                    "WHERE ST.StatusType = 'Rejected') AS Rejected,\n" +
                    "\n" +
                    "(SELECT COUNT(*) \n" +
                    "FROM Application AS A INNER JOIN StatusType AS ST\n" +
                    "ON A.StatusType = ST.StatusTypeId\n" +
                    "WHERE MandatoryFlag = 1) AS Objection\n" +
                    "\n" +
                    "(SELECT\n" +
                    "COUNT(*) AS COUNT,\n" +
                    "RT.RejectionType\n" +
                    "FROM Application AS A INNER JOIN RejectionType AS RT\n" +
                    "ON A.RejectionType = RT.RejectionTypeId\n" +
                    "GROUP BY RT.RejectionType)");
                    break;
            case "admissionStatistic" : sql.append("\n" +
                    "SELECT \n" +
                    "I.InstitutionName,\n" +
                    "COUNT(*) AS Received\n" +
                    "FROM Admission AS A INNER JOIN Institution AS I\n" +
                    "ON A.AdmittedBy = I.InstitutionId\n" +
                    "GROUP BY I.InstitutionName");
                    break;
            case "rejectionStatistic" : sql.append("SELECT\n" +
                    "COUNT(*) AS COUNT,\n" +
                    "RT.RejectionType\n" +
                    "FROM Application AS A INNER JOIN RejectionType AS RT\n" +
                    "ON A.RejectionType = RT.RejectionTypeId\n" +
                    "GROUP BY RT.RejectionType");
                    break;
        }


        if(DatabaseUtilities.monitoring)
            System.out.println(sql.toString() + "\n");


        // execute constructed SQL statement
        Connection connection = DatabaseUtilities.getConnection();
        Statement stmt = connection.createStatement();;
        ResultSet result = stmt.executeQuery(sql.toString());

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
