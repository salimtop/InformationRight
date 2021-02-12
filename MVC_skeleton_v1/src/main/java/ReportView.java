import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReportView implements ViewInterface{
    @Override
    public ViewData create(ModelData modelData, String functionName, String operationName) throws Exception {

        switch(operationName){
            case "select" : return report(modelData);

        }
        return null;
    }

    private ViewData report(ModelData modelData) throws SQLException, ParseException {
        ResultSet result = modelData.resultSet;
        Map<String,Object> parameters = modelData.transferData == null ? new HashMap<String,Object>() : modelData.transferData;

        if( ! parameters.containsKey("statisticType")){
            Map<String,Object> reportParameters = new HashMap<String,Object>();
            reportParameters.put("statisticType","generalStatistic");
            return new ViewData("Report","select",reportParameters);
        }

        if(result != null){
            ArrayList<String[]> table = new ArrayList<String[]>();

            switch ((String)parameters.get("statisticType")){
                case "generalStatistic" :{
                    table.add(new String[]{"Total Application Count","Finalized Application","Positive","Rejected","Objection Count"});

                    while(result.next()){
                        String[] row = new String[table.get(0).length];

                        row[0] = String.valueOf(result.getInt("TotalApplication"));
                        row[1] = String.valueOf(result.getInt("Finalized"));
                        row[2] = String.valueOf(result.getInt("Positive"));
                        row[3] = String.valueOf(result.getInt("Rejected"));
                        row[4] = String.valueOf(result.getInt("Objection"));
                        table.add(row);
                    }
                    System.out.println("General Statistics");
                    DatabaseUtilities.printTable(table,false);
                    parameters.put("statisticType","rejectionStatistic");
                    return new ViewData("Report","select",parameters);
                }
                case "rejectionStatistic" : {
                    table.add(new String[]{"Rejection Type","Count"});
                    while(result.next()){
                        String[] row = new String[table.get(0).length];

                        row[0] = String.valueOf(result.getString("RejectionType"));
                        row[1] = String.valueOf(result.getInt("Count"));

                        table.add(row);
                    }

                    System.out.println("Rejection Statistics");
                    DatabaseUtilities.printTable(table,false);
                    parameters.put("statisticType","admissionStatistic");
                    return new ViewData("Report","select",parameters);
                }

                case "admissionStatistic" : {
                    table.add(new String[]{"Institution Name","Received Application Count"});
                    while(result.next()){
                        String[] row = new String[table.get(0).length];

                        row[0] = String.valueOf(result.getString("InstitutionName"));
                        row[1] = String.valueOf(result.getInt("Received"));

                        table.add(row);
                    }

                    System.out.println("Admission Number of Institutions");
                    DatabaseUtilities.printTable(table,false);
                    getString("Press enter to continue",true);
                }
            }


        }

        return new ViewData("Screen","screen.gui");
    }


}
