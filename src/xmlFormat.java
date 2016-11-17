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

	
public static void XML(ResultSet rSet, ArrayList<Attribute> aList)
{
	int colCount = 1;			//used to keep track of what column we are in
	int rowCount = 0;			//used to keep track of row
	
<<<<<<< HEAD
	try{
		
=======
>>>>>>> 5aab6b34ffb1a8074b4dd3f91a5d3840516f82dc
	
	try{
	while(rSet.next())
	{
		String colName=	aList.get(colCount).name;
		System.out.println("<" + colName + ">");
		rSet.getObject(colName);
		System.out.println("</" + colName + ">");
		
		
	}
	
	}
	 catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
	
	
	
	
}
