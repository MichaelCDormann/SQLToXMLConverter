
/*
 * XML Formatter
 * Will be passed a ResultSet object from a selection query
 * Will be passed ArrayList of Attribute object
 * 
 * Will print the correct out put
 * 
 */

import java.sql.*;
import java.io.*;
import java.util.ArrayList;

public class xmlFormat {
	static ResultSet rSet;
	 static ArrayList<Attribute> aList;
	public static void XML(ResultSet ret, ArrayList<Attribute> lst) {
		int colCount = 1; // used to keep track of what column we are in
		int rowCount = 0; // used to keep track of row
		rSet = ret;								//have the result Set Global
		aList =lst;
		boolean groupFlag = false;				//used to keep track of grouping
		boolean compressionFlag = false;		//used to keep track of Compression
		int length = aList.size();
		int numOfSpace=0;							//used to format whitespace while printing
		int groupCount = 0;
		String[] gNames = new String[5];
		
		

		try {
			System.out.println("?xml version ='1.0'?>");
			System.out.println("<This Query>");
			
			
			while (rSet.next()) // brings it to the next row
			{
				System.out.println("<A Record>");
				String tabName = aList.get(colCount).tableName;					
				String alias = aList.get(colCount).alias;
				String gName ="";
				
				while (colCount < length) {
					tabName = aList.get(colCount).tableName;			//grabs the current attributes table
					alias = aList.get(colCount).alias;					//grabs the current attributes alias
					if(aList.get(colCount).group!= null && groupFlag==false)		//if attribute has a group 
					{
						
						groupFlag = true;												
						gName =aList.get(colCount).group.name;								//grab attributes group name and print
						gNames[groupCount] = gName;											//saves the name so we can close out later
						System.out.println("< " + gName + " >");							
						groupCount++;
					}
					else if(groupFlag ==true && (aList.get(colCount).group.name!=gName))		//entering nested groups
					{	
						groupFlag = true;												
						gName =aList.get(colCount).group.name;								//grab attributes group name and print
						gNames[groupCount] = gName;											//saves the name so we can close out later
						System.out.println("	< " + gName + " > ----");							
						groupCount++;
					}
					
					
					String colName = aList.get(colCount).name;								//get the official attribute name
					
					if(alias.equals(""))
					{
						alias = colName;				//if alias doesn't exist put the official name there to print
					}
					
				//	String.format("%" + numOfSpace + "s", "Hello");
					
					System.out.println("<" + alias + "  Table="+ tabName + "  name=" + colName +">");
					rSet.getObject(colName);
					System.out.println("</" + colName + ">");
					colCount++;
				}
				
				
				if(groupFlag==true){						//prints the closing group tabs at the end of all attributes
					while(groupCount> 0)
					{
						gName = gNames[groupCount];		
						System.out.println(" </ " + gName + " >");
						//need to format to decrease whitespace with each close
						groupCount--;
					}
					
					groupFlag=false;
				}
				
				
				System.out.println("</A Record>");
				colCount = 1;
				rowCount++;
			}																	//ends the while loop
		
			
			
			System.out.println("</This Query>");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
}
