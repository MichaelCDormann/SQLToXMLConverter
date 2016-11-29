import java.sql.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;
import java.util.regex.Pattern;

public class SQLParser {
	
	Database db;							// database connection object
	ArrayList<String> tokenList;			// list of tokens from SQL Query
	ArrayList<Attribute> attrList;			// list of attributes to be passed to XMLFormat
	ArrayList<String> tableList;			// list of tables populated in the from loop
	Stack<Group> groupStack;				// list of groups that the attributes belong to
	String generatedQuery;					// query that is generated throughout the states
	Hashtable<String, ArrayList<String>> attributes;	// attribute names stored with table names
	
	
	// constructor
	SQLParser(Database db){
		
		// set local pointer for passed Database object list 
		this.db = db;
		this.tableList = new ArrayList<String>();
		
	}
	
	private void createMetaData() throws SQLException {
		attributes = new Hashtable<String, ArrayList<String>>();
		ArrayList<String> tmpTableNames  = new ArrayList<String>();
		ArrayList<String> tableNames  = new ArrayList<String>();
		ArrayList<String> attrList;
		ResultSet result;
		
		// create a list of tables from the catalog
		result = this.db.query("Select * From cat");
		while(result.next()) {
			tmpTableNames.add(result.getString(1));
		}
		
		int fromIndex = this.tokenList.indexOf("from");
		fromIndex++;
		for(int i = fromIndex; i < this.tokenList.size(); i++) {
			if(tmpTableNames.contains(this.tokenList.get(i).toUpperCase())) {
				tableNames.add(this.tokenList.get(i).toUpperCase());
			}
		}
		
		// for each table query the database for the attributes, then store the attributes and tables
		// as key value pairs... So attributes.get(table_name) will return an arraylist of attributes that 
		// belong to that table
		for(int i = 0; i < tableNames.size(); i++) {
			attrList = new ArrayList<String>();
			result = this.db.query("Select column_name From USER_TAB_COLUMNS Where table_name = '" + tableNames.get(i) + "'");
			while(result.next()) {
				attrList.add(result.getString(1));
			}
			this.attributes.put(tableNames.get(i), attrList);
		}
	}
	
	public String getTableName(String attrName) {
		Enumeration<String> names = this.attributes.keys();
		ArrayList<String> attrs;
		String name;
		
		String result = "null";
		
		// loop through the keys (table names) and search through each list of attributes for a match
		// return the table name where it matched
		while(names.hasMoreElements()) {
			name = names.nextElement();
			attrs = this.attributes.get(name);
			for(int i = 0; i < attrs.size(); i++) {
				if(attrs.get(i).equals(attrName.toUpperCase()))
					result = name;
			}
		}
		
		return result;
	}
	
	
	public ParseResult parseQuery(String query){
		generatedQuery = "";
		
		this.tokenList = new ArrayList<String>();
		this.attrList = new ArrayList<Attribute>();
		this.groupStack = new Stack<Group>();
		Tokenizer t = new Tokenizer(this.tokenList);
		
		t.tokenize(query);	// tokenizes query and updates tokenList with tokens
		
		// create the metadata... basically just the attributes hashtable
		try{
			createMetaData();
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.exit(0);
		}
		
		// call start method -- state 1 of FSM
		startState();
		
		System.out.println(generatedQuery);
		for(int i = 0; i < this.attrList.size(); i++) {
			Attribute attr = this.attrList.get(i);
			System.out.println("" + attr.name + " " + attr.tableName + " " + attr.alias + " ");
			if(attr.compTo != null)
				System.out.println("\t" + attr.compTo.name);
			if(attr.group != null)
				System.out.println("\t" + attr.group.name + " " + attr.group.compTo);
		}
		ParseResult result = new ParseResult(this.generatedQuery, this.attrList);
		return result;
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
	 * State  4		|    fromLoop
	 * State  5		|    - placed inside of attributeState( state 2 )
	 * State  6		|    - part of attributeLoop ( state 3 )
	 * State  7		|    ** not needed?
	 * State  8		|    groupLoop
	 * State  9		|    - placed inside of groupLoop ( state 8 )
	 * State 10		|    - placed inside of groupLoop ( state 8 )
	 * State 11		|    groupPreAttributeState
	 * State 12		|    groupAttributeLoop
	 * State 13		|    - part of groupAttributeLoop ( state 12 )
	 * State 14		|    exitGroup
	 * State 15		|    - placed inside of attributeLoop( state 3 )
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
			if(nextTokenMatch("from")) {
				// state 5 handled right here
				updateQuery("from");
				//call 4
				fromLoop();
				
				//TODO handle build attributeList from tableList info
				for(int i = 0; i < this.tableList.size(); i++){
					// for each table grab attribute list
					ArrayList<String> tableAttr = this.attributes.get(this.tableList.get(i).toUpperCase());
					for(int j = 0; j < tableAttr.size(); j++){
						// for each attribute in list, create attribute and add to attrList
						Attribute tmpAttr = new Attribute(tableAttr.get(j), tableList.get(i));
						this.attrList.add(tmpAttr);
					}
					
				}
			} else {
				try {
					throw new ParseException("'From' was expected after '*'");
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}else if(isNextID()){
			// call 3
			attributeLoop();
		}else if(nextTokenMatch("<"))
			groupLoop();
		else{
			
			try {
				throw new ParseException("Attributes not specified");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
		
	} // end state 2
	
	
	// state 3
	public void attributeLoop(){
		
		if(nextTokenMatch(",")) {
			updateQuery(",");
			attributeLoop();
			
		} else if(isNextID() && !getNextVal().equals("from")){ // ingest from state 2 
																			// if next token is an attribute
			String tmpAttrName = getNextVal();
			// remove attribute ID from list
			getNextToken();
			updateQuery(tmpAttrName);
			
			// check if next matches AS for alias
			if(nextTokenMatch("as")){
				
				if(isNextID()){
					
					String tmpAlias = getNextVal();
					System.out.println(attributes.get(tmpAttrName));
					this.attrList.add(  new Attribute(tmpAttrName, getTableName(tmpAttrName), tmpAlias)  );
					getNextToken();
					
				}else{
					try {
						throw new ParseException("Alias for "+tmpAttrName+" expected");
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
					
			}else{
				// no alias
				this.attrList.add(  new Attribute(tmpAttrName, getTableName(tmpAttrName))  );
			}
			
			attributeLoop();
			
		} else if(nextTokenMatch("from")) {
			updateQuery("from");
			// call 4
			fromLoop();
			
		} else if(nextTokenMatch("+")) {
			// state 15 handled right here
			
			String tmpAttrName = getNextVal();
			// remove attribute ID from list
			getNextToken();
			updateQuery(tmpAttrName);
			
			// check if next matches AS for alias
			if(nextTokenMatch("as")){
				
				if(isNextID()){
					
					String tmpAlias = getNextVal();
					Attribute tmpAttr = new Attribute(tmpAttrName, getTableName(tmpAttrName), tmpAlias);
					tmpAttr.addCompression(this.attrList.get(this.attrList.size()-1));
					this.attrList.add(tmpAttr);
					getNextToken();
					
				}else{
					try {
						throw new ParseException("Alias for "+tmpAttrName+" expected");
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
					
			}else{
				// no alias
				Attribute tmpAttr = new Attribute(tmpAttrName, getTableName(tmpAttrName));
				tmpAttr.addCompression(this.attrList.get(this.attrList.size()-1));
				this.attrList.add(tmpAttr);
			}
			
			attributeLoop();
			
		} else if(nextTokenMatch("<")) {
			// call 8
			groupLoop();
			
		} else{
			try {
				throw new ParseException("Unexpected token in attribute loop: " + getNextVal());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	// state 4
	public void fromLoop() {
		// do we care about the rest from here?
		// if not:
		String remaining = "";
		for(int i = 0; i < this.tokenList.size(); i++) {
			remaining += (" " + this.tokenList.get(i));
			
			// if we have a table name add to the list
			if(this.attributes.containsKey(this.tokenList.get(i).toUpperCase())){
				this.tableList.add(this.tokenList.get(i));
			}
			
		}
		this.generatedQuery += remaining;
	}
	
	// state 8
	public void groupLoop() {
		Group currGroup = new Group("null");
		this.groupStack.push( currGroup );
		
		if(nextTokenMatch("+")) {
			// state 10 handled right here
			currGroup.addCompression(this.attrList.get(this.attrList.size() - 1));
			if(isNextGroupName() && !attributes.containsKey(getNextVal())) {
				// state 9 handed right here
				// if the next token is a group name and it isn't an existing attribute
				currGroup.changeName(getNextVal());
				getNextToken();
				if(nextTokenMatch(",")) {
					// call 11
					groupPreAttributeState();
				} else {
					try {
						throw new ParseException("Expected ',' after group name");
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			} else if(isNextID())
				// call 12
				groupAttributeLoop();
		} else if(isNextGroupName() && !attributes.containsKey(getNextVal())) {
			// state 9 handled right here
			// if the next token is a group name and it isn't an existing attribute
			currGroup.changeName(getNextVal());
			getNextToken();
			if(nextTokenMatch(",")) {
				//call 11
				groupPreAttributeState();
			}
		} else {
			try {
				throw new ParseException("Expected '+' or group name, got '" + getNextVal() + "'");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	
	// state 11
	public void groupPreAttributeState() {
		if(nextTokenMatch("<"))
			// call 8
			groupLoop();
		else if(isNextID())
			// call 12
			groupAttributeLoop();
		else {
			try {
				throw new ParseException("Expected '<' or ID, got '" + getNextVal() + "'");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	
	// state 12
	public void groupAttributeLoop(){
		
		if(nextTokenMatch(",")) {
			updateQuery(",");
			groupAttributeLoop();
			
		} else if(isNextID()){
			
			String tmpAttrName = getNextVal();
			// remove attribute ID from list
			getNextToken();
			updateQuery(tmpAttrName);
			
			// check if next matches AS for alias
			if(nextTokenMatch("as")){
				
				if(isNextID()){
					
					String tmpAlias = getNextVal();
					Attribute tmpAttr = new Attribute(tmpAttrName, getTableName(tmpAttrName), tmpAlias);
					tmpAttr.addGroup(this.groupStack.peek());
					this.attrList.add(tmpAttr);
					getNextToken();
					
				}else{
					try {
						throw new ParseException("Alias for "+tmpAttrName+" expected");
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
					
			}else{
				// no alias					
				Attribute tmpAttr = new Attribute(tmpAttrName, getTableName(tmpAttrName));
				tmpAttr.addGroup(this.groupStack.peek());
				this.attrList.add(tmpAttr);
			}
			
			groupAttributeLoop();
			
		} else if(nextTokenMatch("<")) {
			// call 8
			groupLoop();
			
		} else if(nextTokenMatch(">")) {
			// call 14	
			exitGroup();
			
		} else{
			try {
				throw new ParseException("Unexpected token in attribute loop: " + getNextVal());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	// state 14
	public void exitGroup() {
		this.groupStack.pop();
		
		if(!this.groupStack.isEmpty()) {
			// call 12
			groupAttributeLoop();
		} else {
			// call 3
			attributeLoop();
		}
	}
	
}