import java.sql.*;
import java.util.*;


interface ModelInterface {

	abstract ResultSet select(Map<String, Object> viewParameters) throws Exception;
	
	abstract Integer insert(Map<String,Object> insertParameters) throws Exception;

	abstract int update(Map<String,Object> parameters) throws Exception;

	abstract int delete(Map<String,Object> whereParameters) throws Exception;
	
	default ModelData execute(ViewData viewData) throws Exception {
		if (viewData.viewParameters == null) {
			return new ModelData();
		}
	
		switch(viewData.operationName) {
			case "select":
			{
				ResultSet resultSet = select(viewData.viewParameters);
				
				return new ModelData(viewData.functionName, resultSet,viewData.viewParameters);
			}
			case "insert":
			{

				Integer lastID = insert(viewData.viewParameters);

				Map<String,Object> parameters = viewData.viewParameters;
				parameters.put("lastId",lastID);

				return new ModelData(viewData.functionName, parameters);
			}
			case "update":
			{

				int recordCount = update(viewData.viewParameters);

				Map<String,Object> parameters = viewData.viewParameters;
				parameters.put("recordCount",recordCount);
				
				return new ModelData(viewData.functionName, parameters);
			}
			case "delete":
			{
				Map<String, Object> whereParameters = (Map<String, Object>)(viewData.viewParameters.get("whereParameters"));
				
				int recordCount = delete(whereParameters);

				Map<String,Object> parameters = new HashMap<String,Object>();
				parameters.put("recordCount",recordCount);
				
				return new ModelData(viewData.functionName, parameters);
			}
		}
		
		return new ModelData(viewData.viewParameters);
	}
	
}
