package DBPack;

import java.sql.*;

public class DBStatement {
	private static final String url="jdbc:mysql://db4free.net:3306/yamatest1";
	private static final String driver = "com.mysql.jdbc.Driver";
	private static final String userName = "mrtensai"; 
	private static final String password = "123456";

	private static Connection connection = null;

	public static Connection getMainConnection(){
		if(connection != null)
			return connection;
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url,userName,password);
		} catch (ClassNotFoundException e) {
		} catch (SQLException e) {
		}
		return connection;
	}
	
	public static void closeConnection(){
		if(connection != null){
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}