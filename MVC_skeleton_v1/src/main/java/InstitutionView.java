import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class InstitutionView implements ViewInterface {


    @Override
    public ViewData create(ModelData modelData, String functionName, String operationName) throws Exception {

        switch (operationName){
            case "select" : return admitToInstitution(modelData);
        }

        return null;
    }

    private ViewData admitToInstitution(ModelData modelData) throws SQLException, ParseException {
        Map<String,Object> parameters = new HashMap<String,Object>();

        ResultSet result = modelData.resultSet;
        Integer institutionId = null;
        String institutionName = null;
        String address = null;
        BigDecimal telephone = null;
        String superior = null;
        System.out.println("Institution Id\tInstitution Name\t\t\t\tAddress\tTelephone\tSuperior");
        while(result.next()){
            institutionId = result.getInt("InstitutionId");
            institutionName = result.getString("InstitutionName");
            address = result.getString("Address");
            telephone = result.getBigDecimal("Telephone");
            superior = result.getString("Superior");
            System.out.println(""+institutionId+"\t\t\t\t"+institutionName+"\t\t\t\t"+address+"\t"+telephone+"\t"+superior);
        }

        return new ViewData("Screen","screen.gui");
    }


}
