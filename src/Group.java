/*
 * Group class
 * 
 * Group class used for grouping (DEFINITION #3)
 * Creates Group objects as a result of grouping from the query
 * Group objects stored as part of each Attribute object
 * Group object stores data about the group as requested by the query
 * 
 */

public class Group {
	
	String name;		// name of group <THIS-RIGHT-HERE, attr1, attr2>
						// if this is UNNAMED (null) not grouped, just compressed
	boolean compFlag;	// flags the group as compressed
	Attribute compTo;	// the attribute information for the group compression
	
	// empty constructor left for testing purposes
	Group() {
		
	}
	
	Group(String name) {
		this.name = name;
	}
	
	public void addCompression(Attribute compTo) {
		this.compTo = compTo;
		this.compFlag = true;
	}
	
	public void changeName(String name) {
		this.name = name;
	}
}