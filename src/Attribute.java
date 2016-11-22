
public class Attribute {
	
	String name;		// attribute's original name (attribute name in table)
	String alias;		// attribute's alias (attribute name upon query)
	Group group;		// group object if the attribute is to be grouped
	String tableName;	// tableName that the attribute belongs to
	
	boolean compFlag;	// is this attribute compressed alone?
	Attribute compTo;	// the attribute information for the single compression

}
