import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InquiryView implements ViewInterface{
    @Override
    public ViewData create(ModelData modelData, String functionName, String operationName) throws Exception {
        switch (operationName){
            case "inquiry" : return inquiryGUI(modelData);
            case "select" : return showInquiry(modelData);
        }

        return new ViewData("MainMenu","");
    }

    private ViewData inquiryGUI(ModelData modelData) throws ParseException {

        Integer applicationNumber = getInteger("Enter Application Number : ",true);

        if (applicationNumber==null)
            return new ViewData("MainMenu","");

        Map<String,Object> parameters = new HashMap<>();

        parameters.put("applicationNumber",applicationNumber);
        parameters.put("inquiryType","applicationDetails");

        return new ViewData("Inquiry","select",parameters);
    }

    private ViewData showInquiry(ModelData modelData) throws ParseException, SQLException {

        ResultSet result = modelData.resultSet;
        Map<String,Object> parameters = modelData.transferData;

        if(result != null){
            ArrayList<String[]> table = new ArrayList<>();

            if(parameters.get("inquiryType").equals("applicationDetails")){
                table.add(new String[]{"Application Code", "Status" , "Mandatory", "Delivered",
                        "Application Date", "Expire Date", "Payment Amount", "Payment Expire"});

                System.out.println("Application Details");
                while( result.next()){
                    String[] row = new String[table.get(0).length];
                    row[0] = result.getString("ApplicationNumber");
                    row[1] = result.getString("StatusType");
                    row[2] = String.valueOf(result.getBoolean("MandatoryFlag"));
                    row[3] = result.getString("DeliveryType");
                    row[4] = String.valueOf(result.getDate("ApplicationDate"));
                    row[5] = String.valueOf(result.getDate("ExpireDate"));
                    row[6] = String.valueOf(result.getDouble("PaymentAmount"));
                    row[7] = String.valueOf(result.getDate("PaymentExpire"));

                    table.add(row);
                }
                DatabaseUtilities.printTable(table,false);
                getString("Press enter to continue",true);
                parameters.put("inquiryType","respondDetails");

                return new ViewData("Inquiry","select",parameters);
            }
            else if(parameters.get("inquiryType").equals("respondDetails")){
                System.out.println("Application progress ");
                table.add(new String[]{"Response" , "Response Date", "Admitted By", "Admission Date"});
                while( result.next()){
                    String[] row = new String[table.get(0).length];
                    row[0] = result.getString("Respond");
                    row[1] = String.valueOf(result.getDate("RespondDate"));
                    row[2] = result.getString("InstitutionName");
                    row[3] = String.valueOf(result.getDate("AdmissionDate"));

                    table.add(row);
                }
                DatabaseUtilities.printTable(table,false);
                getString("Press enter to continue",true);
            }


            return new ViewData("MainMenu","");
        }
        return null;

    }
}
