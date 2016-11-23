import java.sql.*;
import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Pattern;

public class SQLParser {
	
	Connection conn;				// database connection object
	ArrayList<String> tokenList;	// list of tokens from SQL Query
	String generatedQuery;			// query that is generated throughout the states
	
	
	public void parseQuery(String query){
		
		generatedQuery = "";
		
		Stack<Group> groupStack = new Stack<Group>();
		
		this.tokenList = new ArrayList<String>();
		Tokenizer t = new Tokenizer(this.tokenList);
		
		t.tokenize(query);	// tokenizes query and updates tokenList with tokens
		
		// call start method -- state 1 of FSM
		startState();
		
		System.out.println(generatedQuery);
		
	}
	
	
	
	
	// update query with new element
	public void updateQuery(String s){
		this.generatedQuery += " " + s + " ";
	}
	
	
	
	
	
	// verifies nextToken matches and removes from the list
	public boolean nextTokenMatch(String s){
		
		if(this.tokenList.get(0).equals(s)){
			this.tokenList.remove(0);
			return true;
		}else{
			return false;
		}
		
	}
	
	
	
	
	
	// verifies if nextToken matches an ID
	// used for attribute and tables, including attributes with table names preceding
	public boolean isNextID(){
		
		if(this.tokenList.get(0).matches("[A-Za-z][A-Za-z0-9_$#]*(.[A-Za-z0-9_$#]+)?")){
			return true;
		}
		
		return false;
		
	}
	
	
	
	
	
	// verifies if nextToken matches a group name
	// used for group name checks within group markers
	public boolean isNextGroupName(){
		
		if(this.tokenList.get(0).matches("")){
			return true;
		}
		
		return false;
		
	}
	
	
	
	
	/*
	 *  STATES      |   METHOD
	 * -------------|-----------
	 * State  1		|    startState
	 * State  2		|    attributeState
	 * State  3		| 
	 * State  4		|
	 * State  5		|
	 * State  6		|
	 * State  7		|
	 * State  8		|
	 * State  9		|
	 * State 10		|
	 * State 11		|
	 * State 12		|
	 * State 13		|
	 * State 14		|
	 * State 15		|
	 */
	
	// within state 3, 7, 12 handle AS keywords
	// after as keyword is handled, create attribute and
	// add to ArrayList then remove from tokenList
	
	// initial start state 
	public void startState(){
		
		if(nextTokenMatch("select")){
			updateQuery("select");
			//call 2
			attributeState();
		}else{
			
			try {
				throw new ParseException("SELECT statement not found");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	} // end start state
	
	// state 2
	public void attributeState(){
		
		if(nextTokenMatch("*")){
			updateQuery("*");
			// call 5
		}else if(isNextID()){
			// call 3
		}else{
			
			try {
				throw new ParseException("Attributes not specified");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
}