import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

// main class wrapper for entire project
public class SQLtoXMLConverter {
	
	public static void main(String[] args){
				
		// Create Database and SQLParser objects to be used in the rest of the program
		Database db = new Database("jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl", "ozzieosprey", "mytastytacos");
		SQLParser parser;
		ParseResult parseResult;
		ResultSet queryResult;
		String generatedQuery;
		xmlFormat xmlResult;
		
		// Create strings to keep track of menu results
		String menuState = "NONE";
		String query;
		String[] result = new String[2];
		
		// Main loop for the program
		while(!menuState.equals("3") && !menuState.equals("")) {
			// Print the menu and capture the result
			result = menu();
			
			// Set menuState and query to what we get back from the menu
			menuState = result[0];
			query = result[1];
			
			// Parse the query
			if(query != null) {
				parser = new SQLParser(db);
				parseResult = parser.parseQuery(query);
				
				// Execute query
				generatedQuery = parseResult.query;
				queryResult = db.query(generatedQuery);
				
				// Generate XML - pass queryResult and parseResult.attrList
				xmlResult = new xmlFormat(queryResult, parseResult.attrList);
				
			}
			
		}
		
		db.close();
		
		System.out.println("\n\nTERMINATING");
		
	}
	
	private static String[] menu(){
		@SuppressWarnings("resource")
		Scanner s = new Scanner(System.in);
		String selection;
		String[] result = new String[2];
		
		// DBConnection is either passed or executed within
		System.out.println("\n-----------MAIN MENU------------");
			
		System.out.println("1. Input Query for execution.");
		System.out.println("2. View example queries.");
		System.out.println("3. Quit program (or press enter)");
			
		System.out.println("Enter your selection:");
		selection = s.nextLine();
		result[0] = selection;
			
		// if the selection is "1" prompt for query and parse
		if(selection.equals("1")){
			System.out.println("Enter your query: ");
			result[1] = s.nextLine();
		}
			
		if(selection.equals("2")){
			System.out.println("\n---------EXAMPLES----------");
				
			System.out.println("* Definition 2 - SELECT attribute AS newAttrName FROM..");
				
			System.out.println("* Definition 3 - SELECT <GroupName, attr1, attr2> FROM..");
				
			System.out.println("* Definition 4 - SELECT attr1,<+attr2> FROM..\n");
				
			System.out.println("NOTE: These definitions may be nested within one another.\n"
						+ "EG: SELECT attr1,<+GroupName, attr1 AS newAttr, attr2> FROM..");
		}
				
		return result;
	}

}
