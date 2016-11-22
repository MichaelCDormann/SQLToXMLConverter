
public class Attribute {
	
	String name;		// attribute's original name (attribute name in table)
	String alias;		// attribute's alias (attribute name upon query)
	Group group;		// group object if the attribute is to be grouped
	String tableName;	// tableName that the attribute belongs to
	boolean compFlag;	// attribute compression
	Attribute compTo;	// store compression attribute

}
