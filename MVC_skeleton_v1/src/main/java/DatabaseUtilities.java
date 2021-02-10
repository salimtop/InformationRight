import java.sql.*;
import java.util.*;
import java.util.stream.Stream;


class DatabaseUtilities {
	
	private static Connection connection = null;
	public static String host;
	public static String databaseName;
	public static boolean monitoring = true;
	
	private DatabaseUtilities() {
		
	}

	// database connection
	public static Connection getConnection() throws SQLException {
		if (connection == null) {
			String conUrl =   "jdbc:sqlserver://" + host + ";"
							+ "databaseName=" + databaseName + ";"
							+ "integratedSecurity=true";

			connection = DriverManager.getConnection(conUrl);
		}
		
		return connection;
	}
	
	public static void disconnect() throws SQLException
	{
		if (connection == null) {
			connection.close();
		}
	}
	
	// common functions
	public static List<Map.Entry<String, Object>> createWhereParameterList(Map<String, Object> parameters)
	{
		List<Map.Entry<String, Object>> whereParameters = new ArrayList<Map.Entry<String, Object>>();

		if (parameters != null) {
			for (Map.Entry<String, Object> entry : parameters.entrySet()) { 
				whereParameters.add(entry);
			}
		}
		
		return whereParameters;
	}

	public static String prepareWhereStatement(List<Map.Entry<String, Object>> whereParameters)
	{
		StringBuilder whereStatement = new StringBuilder();
		
		if (whereParameters != null) {
			for (int i=0; i<whereParameters.size(); i++) {
				Map.Entry<String, Object> entry = whereParameters.get(i);
				String key = entry.getKey();
				Object value = entry.getValue();	
				
				String pref = (i == 0 ? " WHERE " : " AND ");
				if (value instanceof String && value.toString().contains("%"))
					whereStatement.append(pref + key + " LIKE ? ");
				else if(value instanceof String && value.toString().contains("NULL"))
					whereStatement.append(pref+ key +" " +value+" ");
				else
					whereStatement.append(pref + key + " = ? ");				
			}
		}
		
		return whereStatement.toString();
	}
	
	public static void setWhereStatementParameters(PreparedStatement preparedStatement, List<Map.Entry<String, Object>> whereParameters) throws Exception 
	{
		if (whereParameters != null) {
			for (int i=0; i<whereParameters.size(); i++) {
				Map.Entry<String, Object> entry = whereParameters.get(i);
				Object value = entry.getValue();	
	
				if (value instanceof Integer) {
					preparedStatement.setInt(i + 1, (Integer)value);
				}
				
				if (value instanceof String && !value.toString().contains("NULL")) {
					preparedStatement.setString(i + 1, (String)value);
				}
			}
		}
	}

	public static String formatField(Object value) {
		if(value == null)
			return "NULL";

		if (value instanceof String) {
			return "'" + value + "'"; 
		}
		else if(value instanceof Boolean)
			return (Boolean)value ? "1" : "0";
		
		return value.toString();
	}


	public static void printTable(ArrayList<String[]> table, boolean rightJustified){


		Map<Integer, Integer> columnLengths = new HashMap<>();

		table.stream().forEach(a -> Stream.iterate(0, (i -> i < a.length), (i -> ++i)).forEach(i -> {
			if (columnLengths.get(i) == null) {
				columnLengths.put(i, 0);
			}
			if (columnLengths.get(i) < a[i].length()) {
				columnLengths.put(i, a[i].length());
			}
		}));

		/*
		 * Prepare format String
		 */
		final StringBuilder formatString = new StringBuilder("");
		String flag = rightJustified ? "" : "-";
		columnLengths.entrySet().stream().forEach(e -> formatString.append("| %" + flag + e.getValue() + "s "));
		formatString.append("|\n");

		/*
		 * Prepare line for top, bottom & below header row.
		 */
		String line = columnLengths.entrySet().stream().reduce("", (ln, b) -> {
			String templn = "+-";
			templn = templn + Stream.iterate(0, (i -> i < b.getValue()), (i -> ++i)).reduce("", (ln1, b1) -> ln1 + "-",
					(a1, b1) -> a1 + b1);
			templn = templn + "-";
			return ln + templn;
		}, (a, b) -> a + b);
		line = line + "+\n";


		 // Print table
		String[] columns = table.remove(0);
		System.out.print(line);
		System.out.printf(formatString.toString(),columns);
		System.out.print(line);
		table.forEach(a -> System.out.printf(formatString.toString(), a));
		System.out.print(line);



	}

}
