import java.sql.*;

public class SQLParser {
	
	Connection conn;	// database connection object

	// parser constructor
	SQLParser(){
		
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e){
			System.out.println("Could not load the driver.");
		}
		
		try {
			this.conn = DriverManager.getConnection(
					"jdbc:oracle:thin@olympia.unfcsd.unf.edu:1521:dworcl",
					"user", "password");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/*
	 * effectively close the Database connection
	 * should be called when connection is no longer needed
	 * closing the application, for example
	 */
	protected void close(){
		try {
			this.conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}