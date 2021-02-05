import java.sql.ResultSet;

public class ModelData {
	final String modelName;
	final ResultSet resultSet;
	final Object transferData;
	
	ModelData() {
		this.modelName = null;
		this.resultSet = null;
		this.transferData = null;
	}

	ModelData(Object transferData){
		this.modelName = null;
		this.transferData = transferData;
		this.resultSet = null;
	}

	ModelData(String modelName, ResultSet resultSet) {
		this.modelName = modelName;
		this.resultSet = resultSet;
		this.transferData = null;
	}	
	
	ModelData(String modelName, int viewParameter) {
		this.modelName = modelName;
		this.resultSet = null;
		this.transferData = viewParameter;
	}	
	
	ModelData(String modelName, ResultSet resultSet, int viewParameter) {
		this.modelName = modelName;
		this.resultSet = resultSet;
		this.transferData = viewParameter;
	}

	@Override
	public String toString() {
		return "Model Data (" + modelName + ")";
	}	
	
}
