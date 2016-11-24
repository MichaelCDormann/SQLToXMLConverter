
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
	
	// minimal constructor
	Attribute(String name, String tableName){
		
		this.name = name;
		compFlag = false;
		
	}
	
	// attribute constructor w/ name, and alias
	Attribute(String name, String tableName, String alias){
		
		this.name = name;
		this.tableName = tableName;
		this.alias = alias;
		compFlag = false;
		
	}
	
	// ------------------------------------------------------------
	
	// modifies the attribute to be a child of a group
	public void addGroup(Group group){
		
		this.group = group;
		
	}
	
	
	// modifies the attribute to be compressed to the passed attribute
	public void addCompression(Attribute compTo){
		
		this.compFlag = true;
		this.compTo = compTo;
		
	}

}
