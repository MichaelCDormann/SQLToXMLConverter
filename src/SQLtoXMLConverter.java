import java.util.Scanner;

// main class wrapper for entire project
public class SQLtoXMLConverter {
	
	public static void main(String[] args){
		
		Database db = new Database("jdbc:sqlite:sample.db", "", "");
		menu(db);
		db.close();
		
	}
	
	private static void menu(Database db){
		
		Scanner s = new Scanner(System.in);
		String selection = "NONE";
		SQLParser parser = new SQLParser();
		
		while(!selection.equals("3")
			&& !selection.equals("")){
		
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
				String query = s.nextLine();
				
				parser.parseQuery(query);
			}
			
			if(selection.equals("2")){
				System.out.println("\n---------EXAMPLES----------");
				System.out.println("* Definition 1");
				
				System.out.println("* Definition 2");
				
				System.out.println("* Definition 3");
				
				System.out.println("* Definition 4");
			}
			
		}
		
		System.out.println("\n\nTERMINATING");
		
		s.close();
		
		
	}

}
