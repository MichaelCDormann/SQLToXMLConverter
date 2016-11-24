import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Database {
	private Connection connection;
	
	Database(String driver, String username, String password) {
		// If the driver, username, and password haven't been provided ask the user for them
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		if (driver.equals("")) {
			System.out.println("Enter a driver for the DB connection:");
			driver = input.nextLine();
		}
		if (username.equals("")) {
			System.out.println("Enter a username:");
			username = input.nextLine();
		}
		if (password.equals("")) {
			System.out.println("Enter a password:");
			password = input.nextLine();
		}
		
		//input.close(); <-- commented out problematic closing of System's input stream
		
		// Create the connection to the Database
		try {
			connection = DriverManager.getConnection(driver, username, password);
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
			System.exit(0);
		}
	}
	
	public ResultSet query(String query) {
		// Execute a query and return the result
		ResultSet result = null;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			
			if (query.toLowerCase().contains("select"))
				result = statement.executeQuery(query);
			else
				statement.executeUpdate(query);
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		} 
		return result;
	}
	
	public void close() {
		// Close the connection to the Database
		try {
			if (connection != null) {
				connection.close();
				connection = null;
			}
		} catch(SQLException ex) {
			System.err.println(ex);
		}
	}
}
