import java.util.Scanner;

// main class wrapper for entire project
public class SQLtoXMLConverter {
	
	public static void main(String[] args){
		// Create Database and SQLParser objects to be used in the rest of the program
		Database db = new Database("", "", "");
		SQLParser parser = new SQLParser(db);
		
		String menuState = "NONE";
		
		// Main loop for the program
		while(!menuState.equals("3") && !menuState.equals("")) {
			menuState = menu();
			parser.parseQuery(menuState);
			// Generate XML
		}
		
		db.close();
		
		System.out.println("\n\nTERMINATING");
		
	}
	
	private static String menu(){
		
		Scanner s = new Scanner(System.in);
		String result, selection;
		
		// DBConnection is either passed or executed within
		System.out.println("\n-----------MAIN MENU------------");
			
		System.out.println("1. Input Query to for execution.");
		System.out.println("2. View example queries.");
		System.out.println("3. Quit program (or press enter)");
			
		System.out.print("Enter your selection:");
		selection = s.nextLine();
			
		// if the selection is "1" prompt for query and parse
		if(selection.equals("1")){
			System.out.println("Enter your query: ");
			result = s.nextLine();
		}
			
		else if(selection.equals("2")){
			System.out.println("\n---------EXAMPLES----------");
				
			System.out.println("* Definition 2 - SELECT attribute AS newAttrName FROM..");
				
			System.out.println("* Definition 3 - SELECT <GroupName, attr1, attr2> FROM..");
				
			System.out.println("* Definition 4 - SELECT attr1,<+attr2> FROM..\n");
				
			System.out.println("NOTE: These definitions may be nested within one another.\n"
						+ "EG: SELECT attr1,<+GroupName, attr1 AS newAttr, attr2> FROM..");
		
			result = selection;
		}
		
		else {
			result = selection;
		}
		
		s.close();
		
		return result;
	}

}
