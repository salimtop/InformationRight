import java.sql.*;
import java.util.*;


interface ModelInterface {

	abstract ResultSet select(Map<String, Object> viewParameters) throws Exception;
	
	abstract Integer insert(Map<String,Object> insertParameters) throws Exception;

	abstract int update(Map<String,Object> updateParameters, Map<String,Object> whereParameters) throws Exception;

	abstract int delete(Map<String,Object> whereParameters) throws Exception;
	
	default ModelData execute(ViewData viewData) throws Exception {
		if (viewData.viewParameters == null) {
			return new ModelData();
		}
	
		switch(viewData.operationName) {
			case "select":
			{
				ResultSet resultSet = select(viewData.viewParameters);
				
				return new ModelData(viewData.functionName, resultSet);
			}
			case "insert":
			{

				Integer lastID = insert(viewData.viewParameters);
				
				return new ModelData(viewData.functionName, lastID);
			}
			case "update":
			{
				Map<String, Object> updateParameters = (Map<String, Object>)(viewData.viewParameters.get("updateParameters"));
				Map<String, Object> whereParameters = (Map<String, Object>)(viewData.viewParameters.get("whereParameters"));
				
				int recordCount = update(updateParameters, whereParameters);
				
				return new ModelData(viewData.functionName, recordCount);
			}
			case "delete":
			{
				Map<String, Object> whereParameters = (Map<String, Object>)(viewData.viewParameters.get("whereParameters"));
				
				int recordCount = delete(whereParameters);
				
				return new ModelData(viewData.functionName, recordCount);
			}
		}
		
		return new ModelData(viewData.viewParameters);
	}
	
}
