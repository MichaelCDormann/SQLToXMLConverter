
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

	public static void XML(ResultSet rSet, ArrayList<Attribute> aList) {
		int colCount = 1; // used to keep track of what column we are in
		int rowCount = 0; // used to keep track of row
		int length = aList.size();

		try {
			while (rSet.next()) // brings it to the next row
			{
				String tabName = aList.get(colCount).tableName;
				System.out.println("<" + tabName + ">");
				while (colCount < length) {
					String colName = aList.get(colCount).name;
					System.out.println("<" + colName + ">");
					rSet.getObject(colName);
					System.out.println("</" + colName + ">");
					colCount++;
				}
				
				
				
				System.out.println("</" + tabName + ">");
				colCount = 1;
				rowCount++;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
