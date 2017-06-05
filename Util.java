package gMigrate;

import java.util.List;
import java.util.Random;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public class Util {
	public String getColumnType(String tablename, String columnName) {
		Session session = initialiseSession();
		StringBuilder query = new StringBuilder("SELECT ").append(columnName).append(" FROM ").append(tablename)
				.append(";");
		ResultSet data = session.execute(query.toString());
		return data.getColumnDefinitions().getType(columnName).toString();
	}

	public Session initialiseSession() {
		Cluster cluster = Cluster.builder().withClusterName("myCluster").addContactPoint("127.0.0.1").build();
		Session session = cluster.connect();
		return session;
	}

	public String getColumnTypeLocal(String tablename, String columnName) {
		System.out.println(tablename + "::" + columnName);
		final String[] listofTypes = { "String", "Long", "Int" };
		Random random = new Random();
		int index = random.nextInt(listofTypes.length);
		return listofTypes[index];
	}

	public String[] getColumnList(String table) {
		Session session = initialiseSession();
		StringBuilder query = new StringBuilder("SELECT * FROM ").append(table).append(";");
		ResultSet data = session.execute(query.toString());
		List<ColumnDefinitions.Definition> list = data.getColumnDefinitions().asList();
		String[] columnList = new String[list.size()];
		int index = 0;
		for (ColumnDefinitions.Definition value : list) {
			columnList[index] = value.getName();
			index++;
		}
		return columnList;
	}
	
	public String[] getColumnListLocal(String table, int length) {
		String[] columnList = new String[length];
		for(int index = 0; index<length;index++){
			columnList[index] = "sample";
		}
		return columnList;
	}
}
