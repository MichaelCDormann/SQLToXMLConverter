/*
 * XML Formatter
 * Will be passed a ResultSet object from a selection query
 * Will be passed ArrayList of Attribute objects
 * 
 * Will print the correct output to the console
 * 
 */

//import Java libraries
import java.sql.*;
import java.io.*;
import java.util.ArrayList;

public class xmlFormat {
	
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
	
	static ResultSet rSet;
	static ArrayList<Attribute> aList;
	
	public static void XML(ResultSet ret, ArrayList<Attribute> lst) {
		
		int colCount = 0; 						//used to keep track of what column we are in
		int rowCount = 0; 						//used to keep track of row
		rSet = ret;								//have the result set global
		aList =lst;								//have the ArrayList set global
		boolean groupFlag = false;				//used to keep track of grouping
		boolean compressionFlag = false;		//used to keep track of compression
		boolean inCompFlag = false;
		int length = aList.size();				//used to track the length of the ArrayList
		int numOfSpace=1;						//used to format whitespace while printing
		int groupCount = 0;
		String[] gNames = new String[5];
		String tempGroup = "";
		String tempAtname = "";
		String prevAtName = "";
		
		try {
			System.out.println("<?xml version ='1.0'?>");
			System.out.println("<This Query>");
		
			 while (rSet.next()) {	// brings it to the next row
				
				if(compressionFlag ==false) System.out.println("<A Record>");
				
				String tabName = aList.get(colCount).tableName;					
				String alias = aList.get(colCount).alias;
				String gName ="";
				
				colLoop: while (colCount < length) {
					tabName = aList.get(colCount).tableName;			//grabs the current attributes table
					alias = aList.get(colCount).alias;					//grabs the current attributes alias
					
					if (aList.get(colCount).compFlag == true )
					{
						if(compressionFlag ==false)				//need to save previous attributes name
							tempGroup = prevAtName;
							
						compressionFlag = true;
						tempAtname = aList.get(colCount-1).name;
					}
					
					else if (compressionFlag == true  && (tempAtname.equals(aList.get(colCount).name)))
					{
						String f = aList.get(colCount).name;
						String d =rSet.getString(f);
							if(tempGroup.equals(d)){
						colCount++;
						continue colLoop;
						}
							compressionFlag=false;
					}
					if(aList.get(colCount).group != null && groupFlag == false)		//if attribute has a group 
					{
						groupCount++;
						groupFlag = true;												
						gName =aList.get(colCount).group.name;								//grab attributes group name and print
						gNames[groupCount] = gName;											//saves the name so we can close out later
					
						System.out.print(String.format("%" + numOfSpace+ "s", " "));
						System.out.println("< " + gName + " >");							
					
					}
					else if(groupFlag == true && (aList.get(colCount).group.name != gName))		//entering nested groups
					{	
						groupCount++;
						groupFlag = true;												
						gName =aList.get(colCount).group.name;								//grab attributes group name and print
						gNames[groupCount] = gName;											//saves the name so we can close out later
						System.out.print(String.format("%" + numOfSpace+ "s", "  "));
						System.out.println("	< " + gName + " > ----");							
						
					}
								
					String colName = aList.get(colCount).name;								//get the official attribute name
					
					if(alias==null)
					{
						alias = colName;				//if alias doesn't exist put the official name there to print
					}
					
					//	String.format("%" + numOfSpace + "s", "Hello");
					System.out.print(String.format("%" + numOfSpace+ "s", "  "));
					System.out.println("<" + alias + "  Table= \""+ tabName + "\"  name=\"" + colName +"\">");
					System.out.print(String.format("%" + numOfSpace+ "s", "  "));
					System.out.println(prevAtName = rSet.getString(colName));
					System.out.print(String.format("%" + numOfSpace+ "s", "  "));
					System.out.println("</" + alias + ">");
					
					colCount++;
				}
				
				
				
				 if(groupFlag==true){						//prints the closing group tabs at the end of all attributes
					while(groupCount > 0)
					{
						gName = gNames[groupCount];		
						
						System.out.print(String.format("%" + numOfSpace+ "s", "  "));
						System.out.println("</ " + gName + " >");
						//need to format to decrease whitespace with each close
						groupCount--;
					}
					
					groupFlag=false;
				}
				else{
					System.out.println("</A Record> \n");
				}
		
				colCount = 0;
				rowCount++;
			}																	//ends the while loop
			
			System.out.println("</This Query>");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
}