/*
 * xmlFormat class
 * 
 * xmlFormat class formats the SQL data into proper XML output
 * Class is passed a ResultSet object from a selection query
 * Class is passed an ArrayList of Attribute objects from the SQLParser class
 * XML output is displayed to the console
 * 
 */

//import Java libraries
import java.sql.*;
import java.util.ArrayList;

public class xmlFormat {
	
	//for testing purposes
	public static void main(String[] args){
		ArrayList<Attribute> testList = new ArrayList<Attribute>();
   		
		Attribute temp = new Attribute();
		temp.name = "id";
		temp.tableName = "person";
			
		Attribute temp2 = new Attribute();
		Group gp = new Group();
		gp.name = "Tacos";
		gp.compTo=temp;
		temp2.compFlag=true;
		temp2.group=gp;
		temp2.name = "name";
		temp2.tableName = "person";
			
		testList.add(temp);
		testList.add(temp2);
	   		
		Database parser = new Database("jdbc:sqlite:sample.db","","");
		ResultSet adsf=	parser.query("SELECT * FROM person");
		
		XML(adsf,testList);
		parser.close();
	}
	
	static ResultSet rSet;					// make ResultSet global
	static ArrayList<Attribute> aList;		// make ArrayList global
	
	public static void XML(ResultSet ret, ArrayList<Attribute> lst) {
		
		rSet = ret;								// set the ResultSet to the global variable
		aList =lst;								// set the ArrayList to the global variable
		int colCount = 0; 						// used to keep track of the current column
		int groupCount = 0;						// used to keep track of the group scope
		int length = aList.size();				// used to track the length of the ArrayList
		int tabCnt = 1;							// used to format tab whitespace for console output
		int tagCnt = 0;							// used to track the <A Record> tags
		boolean groupFlag = false;				// used to keep track of grouping
		boolean compressionFlag = false;		// used to keep track of compression
		String[] gNames = new String[5];		// used to keep track of the name of the groups
		String tempGroup = "";					// used to track the name of the current group
		String tempAtname = "";					// used as a temporary holder for the Attribute object
		String prevAtName = "";					// used to track the previous Attribute object
		
		try {		// error handling for SQLException
			
			// display XML output to the console
			System.out.println("<?xml version ='1.0'?>");
			System.out.println("<This Query>");
			
			while (rSet.next()) {		// move ResultSet to the next data row
				
				if (compressionFlag == false) {		// checks if data is to be compressed
					
					System.out.println("<A Record>");	// display XML output to the console
					
					tagCnt++;		// increase the counter for the <A Record> tag
				}
				
				// initialize the variables for the current ResultSet data row
				String tabName = aList.get(colCount).tableName;
				String alias = aList.get(colCount).alias;
				String gName ="";
				
				colLoop: while (colCount < length) {		// move the ResultSet to the next data column
					
					tabName = aList.get(colCount).tableName;			// grabs the current Attribute's table
					alias = aList.get(colCount).alias;					// grabs the current Attribute's alias
					
					if (aList.get(colCount).compFlag == true) {

						if (compressionFlag == false)				// saves previous Attribute's name
							tempGroup = prevAtName;
							
						compressionFlag = true;						// flag to track if compression is processing
						tempAtname = aList.get(colCount-1).name;	// stores the previous column's name
					}
					
					else if (compressionFlag == true  && tempAtname.equals(aList.get(colCount).name)) {

						String f = aList.get(colCount).name;		// stores the current Attribute's name
						String d = rSet.getString(f);				// stores the current ResultSet's name
						
						if (tempGroup.equals(d)) {		// checks if the current group equals the previous group
						
							colCount++;					// increase the column counter for the inner loop
							continue colLoop;			// proceed to the next column
						}
						
						compressionFlag = false;		// mark compression flag; compression completed
					}
					
					if (aList.get(colCount).group != null && groupFlag == false) {		// checks Attribute for a group 

						groupCount++;										// increment the group counter
						groupFlag = true;									// flag to track grouping
						gName = aList.get(colCount).group.name;				// grabs Attribute's group name
						gNames[groupCount] = gName;							// saves the group name to an array
																			// used to close group tags

						// display XML output to the console
						System.out.print(String.format("%" + (4 * tabCnt) + "s", " "));
						System.out.println("<" + gName + ">");
						
						tabCnt++;		// increment the tab counter for whitespace format
					}
					
					else if (groupFlag == true && aList.get(colCount).group.name != gName) {	// entering nested groups
						
						groupCount++;														// increment the group counter
						groupFlag = true;													// flag to track grouping
						gName = aList.get(colCount).group.name;								// grabs Attribute's group name
						gNames[groupCount] = gName;											// saves the group name to an array
																							// used to close group tags
						
						// display XML output to the console
						System.out.print(String.format("%" + (4 * tabCnt) + "s", " "));
						System.out.println("<" + gName + ">");
						
						tabCnt++;		// increment the tab counter for whitespace format
					}
					
					String colName = aList.get(colCount).name;			// grab the current Attribute's name
					
					if (alias == null)				// check the current group for an alias
						alias = colName;			// set alias to column name if alias does not exist
					
					// display XML output to the console
					System.out.print(String.format("%" + (4 * tabCnt) + "s", " "));
					System.out.print("<" + alias.toUpperCase() + " table=\""+ tabName + "\" name=\"" + colName +"\">");
					System.out.print(prevAtName = rSet.getString(colName));
					System.out.println("</" + alias.toUpperCase() + ">");
					
					colCount++;			// increase the column counter for the inner loop
				}	// end column while loop
				
				if (groupFlag == true) {		// checks for grouping
					
					while (groupCount > 0) {	// loop through grouping to display group closing tags
						
						gName = gNames[groupCount];		// set the current group tag to the tag stored in the array		
						
						tabCnt--;		// decrement the tab counter for whitespace format
						
						// display XML output to the console
						System.out.print(String.format("%" + (4 * tabCnt) + "s", " "));
						System.out.println("</" + gName + ">");
						
						groupCount--;		// decrement the group counter
					}
					
					groupFlag = false;		// reset the flag for grouping
				}
				
				else {
					
					// display XML output to the console
					System.out.println("</A Record>");
					
					tagCnt--;		// decrement the tag counter
				}
				
				colCount = 0;		// reset the column counter for the next ResultSet data row
			}	// end row while loop
			
			while (tagCnt > 0) {	// checks that all <A Record> tags are closed
				
				// display XML output to the console
				System.out.println("</A Record>");
				
				tagCnt--;		// decrement the tag counter
			}
			// display XML output to the console
			System.out.println("</This Query>");
			
		} catch (SQLException e) {		// catch SQLException
			e.printStackTrace();		// display stack trace for thrown exception
		}
	}
	
	
	
	public static void DTD(ResultSet ret, ArrayList<Attribute> lst){		//when this function is called it will print the DTD Information
		
		
	
	
	}
	
	public static void XSD(ResultSet ret, ArrayList<Attribute> lst){		//when this function is called it will print XSD 
		
	
	
	}
	
}