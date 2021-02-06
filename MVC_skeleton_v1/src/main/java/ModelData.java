import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.Map;

public class ModelData {
	final String modelName;
	final ResultSet resultSet;
	final Map<String,Object> transferData;
	
	ModelData() {
		this.modelName = null;
		this.resultSet = null;
		this.transferData = null;
	}

	ModelData(Map<String,Object> transferData){
		this.modelName = null;
		this.transferData = transferData;
		this.resultSet = null;
	}

	ModelData(String modelName, ResultSet resultSet) {
		this.modelName = modelName;
		this.resultSet = resultSet;
		this.transferData = null;
	}	
	
	ModelData(String modelName, Map<String,Object> viewParameter) {
		this.modelName = modelName;
		this.resultSet = null;
		this.transferData = viewParameter;
	}	
	
	ModelData(String modelName, ResultSet resultSet, Map<String,Object> viewParameter) {
		this.modelName = modelName;
		this.resultSet = resultSet;
		this.transferData = viewParameter;
	}

	@Override
	public String toString() {
		return "Model Data (" + modelName + ")";
	}	
	
}
