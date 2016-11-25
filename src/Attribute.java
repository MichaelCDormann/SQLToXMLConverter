/*
 * Attribute class
 * 
 * Creates Attribute objects from the data from the SQL database
 * Attribute objects stored in ArrayList passed to XML Formatter
 * Attribute stores data about each attribute retrieved from SQL database
 * 
 */

public class Attribute {
	
	String name;		// attribute's original name (attribute name in table)
	String alias;		// attribute's alias (attribute name upon query)
	Group group;		// group object if the attribute is to be grouped
	String tableName;	// tableName that the attribute belongs to
	boolean compFlag;	// is this attribute compressed alone?
	Attribute compTo;	// the attribute information for the single compression

	// empty constructor left for testing purposes
	Attribute(){
		
	}
	
	// minimal constructor for Attribute object
	Attribute(String name, String tableName) {
		
		this.name = name;	// initialize Attribute identifying name
		compFlag = false;	// initialize compression flag to false
	}
	
	// Attribute constructor with table name and alias
	Attribute(String name, String tableName, String alias){
		
		this.name = name;					// initialize Attribute identifying name
		this.tableName = tableName;			// initialize Attribute table name
		this.alias = alias;					// initialize alias field for [attribute AS name]
		compFlag = false;					// initialize compression flag to false
	}
	
	// ------------------------------------------------------------
	
	// modifies the Attribute object to be a child of a group
	public void addGroup(Group group) {
		
		this.group = group;		// adds the Group object to the Attribute object
	}
	
	// modifies the Attribute object to be compressed to the passed Attribute
	public void addCompression(Attribute compTo) {
		
		this.compFlag = true;	// sets the compression flag to true for the Attribute
		this.compTo = compTo;	// sets the passed Attribute as the compression Attribute
	}
}