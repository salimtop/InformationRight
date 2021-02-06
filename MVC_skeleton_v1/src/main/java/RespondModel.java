import java.sql.ResultSet;
import java.util.Map;

public class RespondModel implements ModelInterface{
    @Override
    public ResultSet select(Map<String, Object> viewParameters) throws Exception {
        return null;
    }

    @Override
    public Integer insert(Map<String, Object> insertParameters) throws Exception {
        return null;
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
