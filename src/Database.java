import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Database {
	private String driver, username, password;
	
	Database(String driver, String username, String password) {
		Scanner input = new Scanner(System.in);
		if (driver == null) {
			System.out.println("Enter a driver for the DB connection:");
			this.driver = input.nextLine();
		}
		if (username == null) {
			System.out.println("Enter a username:");
			this.username = input.nextLine();
		}
		if (password == null) {
			System.out.println("Enter a password:");
			this.password = input.nextLine();
		}
		input.close();
	}
	
	public ResultSet query(String query) {
		Connection connection = null;
		ResultSet result = null;
		try {
			connection = DriverManager.getConnection(driver);
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			
			result = statement.executeQuery(query);
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		} finally {
			try {
				if (connection != null);
					connection.close();
			} catch(SQLException ex) {
				System.err.println(ex);
			}
		}
		return result;
	}
}
