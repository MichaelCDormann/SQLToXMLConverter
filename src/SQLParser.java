import java.sql.*;
import java.util.ArrayList;

public class SQLParser {
	
	Connection conn;	// database connection object

	// parser constructor
	SQLParser(){
		
	}
	
	
	public void parseQuery(String query){
		
		ArrayList<String> tokenList = new ArrayList<String>();
		Tokenizer t = new Tokenizer(tokenList);
		
		t.tokenize(query);	// tokenizes query and updates tokenList with tokens
		
	}	
	
}