import java.util.Scanner;

// main class wrapper for entire project
public class SQLtoXMLConverter {
	
	public static void main(String[] args){
		
		SQLParser parser = new SQLParser();
		menu();
		
	}
	
	private static void menu(){
		
		Scanner s = new Scanner(System.in);
		String selection = "NONE";
		
		while(!selection.equals("")
			&& !selection.equals("3")){
		
			// DBConnection is either passed or executed within
			System.out.println("\n-----------MAIN MENU------------");
			
			System.out.println("1. Input Query to for execution.");
			System.out.println("2. View example queries.");
			System.out.println("3. Quit program (or press enter)");
			
			System.out.print("Enter your selection: ");
			selection = s.nextLine();
			
		}
		
		System.out.println("\n\nTERMINATING");
		
		
	}

}
