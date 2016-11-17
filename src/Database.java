import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Database {
	private String driver, username, password;
	private Connection connection;
	
	Database(String driver, String username, String password) {
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
		input.close();
		this.driver = driver;
		this.username = username;
		this.password = password;
	}
	
	public ResultSet query(String query) {
		ResultSet result = null;
		try {
			//connection = DriverManager.getConnection(driver, username, password);
			connection = DriverManager.getConnection(driver);
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
