import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InstitutionView implements ViewInterface {


    @Override
    public ViewData create(ModelData modelData, String functionName, String operationName) throws Exception {

        switch (operationName){
            case "select" : return showInstitution(modelData);
        }

        return null;
    }


    private ViewData showInstitution(ModelData modelData) throws SQLException, ParseException {
        Map<String,Object> parameters = new HashMap<String,Object>();

        ResultSet result = modelData.resultSet;

        Map<Integer,Institution> institutionList = new HashMap<Integer,Institution>();

        Integer institutionId = null;
        String institutionName = null;
        String address = null;
        BigDecimal telephone = null;
        String superior = null;

        ArrayList<String[]> table = new ArrayList();
        table.add(new String[]{"Institution Id","Institution Name","Telephone","Superior","Address"});


        while(result.next()){
            String[] row = new String[table.get(0).length];

            institutionId = result.getInt("InstitutionId");
            institutionName = result.getString("InstitutionName");
            address = result.getString("Address");
            telephone = result.getBigDecimal("Telephone");
            superior = result.getString("Superior");

            row[0] = String.valueOf(institutionId);
            row[1] = String.valueOf(institutionName);
            row[2] = String.valueOf(telephone);
            row[3] = String.valueOf(superior);
            row[4] = String.valueOf(address);

            institutionList.put(institutionId,new Institution(institutionId,institutionName,address,telephone,superior));

            table.add(row);
        }

        DatabaseUtilities.printTable(table,false);
        getString("Press enter to continue",true);

        if(modelData.transferData.containsKey("redirectFunction")){
            String function = (String) modelData.transferData.get("redirectFunction");
            String operation = (String) modelData.transferData.get("redirectOperation");
            modelData.transferData.put("institutionFlag",true);
            modelData.transferData.put("institutionList",institutionList);
            return new ViewData(function,operation,modelData.transferData);
        }

        return new ViewData("Screen","screen.gui");
    }


}
