import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;
import java.util.regex.Pattern;

public class SQLParser {
	
	Database db;							// database connection object
	ArrayList<String> tokenList;			// list of tokens from SQL Query
	ArrayList<Attribute> attrList;			// list of attributes to be passed to XMLFormat
	Stack<Group> groupStack;				// list of groups that the attributes belong to
	String generatedQuery;					// query that is generated throughout the states
	Hashtable<String, String> attributes;	// attribute names stored with table names
	
	
	// constructor
	SQLParser(Database db){
		
		// set local pointer for passed Database object list 
		this.db = db;
		
	}
	
	private void createMetaData() throws SQLException {
		this.attributes = new Hashtable<String, String>();
		
		ArrayList<String> tableNames  = new ArrayList<String>();
		ResultSet result;
		
		// create a list of tables from the catalog
		result = this.db.query("Select * From cat");
		while(result.next()) {
			tableNames.add(result.getString(1));
		}
		
		// for each table query the database for the attributes, then store the attributes and tables
		// as key value pairs... So attributes.get(attribute_name) will return the table it belongs to
		for(int i = 0; i < tableNames.size(); i++) {
			result = this.db.query("Select column_name From USER_TAB_COLUMNS Where table_name = '" + tableNames.get(i) + "'");
			while(result.next()) {
				this.attributes.put(result.getString(1), tableNames.get(i));
			}
		}
		
	}
	
	
	public void parseQuery(String query){
		// create the metadata... basically just the attributes hashtable
		try{
			createMetaData();
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.exit(0);
		}
		
		generatedQuery = "";
		
		this.tokenList = new ArrayList<String>();
		this.attrList = new ArrayList<Attribute>();
		this.groupStack = new Stack<Group>();
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
		
		if(this.tokenList.get(0).matches("[A-Za-z][A-Za-z0-9_$#]*(?:\\.[A-Za-z0-9_$#]+)?")){
			return true;
		}
		
		return false;
		
	}
	
	
	
	
	
	// verifies if nextToken matches a group name
	// used for group name checks within group markers
	public boolean isNextGroupName(){
		
		if(this.tokenList.get(0).matches("[A-Za-z0-9]+")){
			return true;
		}
		
		return false;
		
	}
	
	// returns the next token value or empty string
	public String getNextVal(){
		
		return (this.tokenList.get(0) == null)? "" : this.tokenList.get(0);
		
	}
	
	
	// removes the head of the tokenList
	public void getNextToken(){
		
		this.tokenList.remove(0);
		
	}
	
	
	
	
	/*
	 *  STATES      |   METHOD
	 * -------------|-----------
	 * State  1		|    startState
	 * State  2		|    attributeState
	 * State  3		|    attributeLoop
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
			attributeLoop();
		}else{
			
			try {
				throw new ParseException("Attributes not specified");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
		
	} // end state 2
	
	
	// state 3
	public void attributeLoop(){
		
		// ingest from state 2
		if(isNextID()){
			
			String tmpAttrName = getNextVal();
			// remove attribute ID from list
			getNextToken();
			updateQuery(tmpAttrName);
			
			// check if next matches AS for alias
			if(nextTokenMatch("as")){
				updateQuery("as");
				
				if(isNextID()){
					
					String tmpAlias = getNextVal();
					this.attrList.add( new Attribute(tmpAttrName, attributes.get(tmpAttrName), tmpAlias) );
					getNextToken();
					updateQuery(tmpAlias);
					
				}else{
					try {
						throw new ParseException("Alias for "+tmpAttrName+" expected");
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
					
			}else{
				// no alias
				this.attrList.add( new Attribute(tmpAttrName, attributes.get(tmpAttrName)) );
				updateQuery(tmpAttrName);
			}
			
		}else{
			try {
				throw new ParseException("Attribute ID expected after \"SELECT\"");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		
		
		// loop state 3 to state 6 begin
		while(nextTokenMatch(",")){
			
			// add comma to generated query
			updateQuery(",");
			// ----------- proceed to state 6 ------------
			if(isNextID()){
				
				String tmpAttrName = getNextVal();
				// remove attribute ID from list
				getNextToken();
				updateQuery(tmpAttrName);
				
				// check if next matches AS for alias
				if(nextTokenMatch("as")){
					updateQuery("as");
					
					if(isNextID()){
						
						String tmpAlias = getNextVal();
						this.attrList.add( new Attribute(tmpAttrName, attributes.get(tmpAttrName), tmpAlias) );//TODO handle table1.attributeName
						getNextToken();
						updateQuery(tmpAlias);
						
					}else{
						try {
							throw new ParseException("Alias for "+tmpAttrName+" expected");
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
						
				}else{
					// non alias
					this.attrList.add( new Attribute(tmpAttrName, attributes.get(tmpAttrName)) );//TODO handle table1.attributeName
					updateQuery(tmpAttrName);
				}
				
			}else{
				try {
					throw new ParseException("Attribute ID expected after \",\"");
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
		}	// loop state 3 to state 6 end
		
		
		
		//TODO implement remainder of state 3
		// eg, call 4, 15, 8
		
	}
	
	
	
	
	
	
	
	
}