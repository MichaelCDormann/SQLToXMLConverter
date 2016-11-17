import java.sql.Connection;
<<<<<<< HEAD
   import java.sql.DriverManager;
   import java.sql.ResultSet;
   import java.sql.SQLException;
   import java.sql.Statement;
import java.util.ArrayList;
=======
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
>>>>>>> 5aab6b34ffb1a8074b4dd3f91a5d3840516f82dc

   
   public class Sample
    {
    public static void main(String[] args) throws ClassNotFoundException
     {
      // load the sqlite-JDBC driver using the current class loader
      Class.forName("org.sqlite.JDBC");

      //Connection connection = null;
      try
      {
         // create a database connection
         /*connection = DriverManager.getConnection("jdbc:sqlite:sample.db");

         Statement statement = connection.createStatement();
         statement.setQueryTimeout(30);  // set timeout to 30 sec.*/
    	  
    	 Database DB = new Database("jdbc:sqlite:sample.db", "", "");

<<<<<<< HEAD
         statement.executeUpdate("DROP TABLE IF EXISTS person");
         statement.executeUpdate("CREATE TABLE person (id INTEGER, name STRING)");
=======

         DB.query("DROP TABLE IF EXISTS person");
         DB.query("CREATE TABLE person (id INTEGER, name STRING)");
>>>>>>> 5aab6b34ffb1a8074b4dd3f91a5d3840516f82dc

         int ids [] = {1,2,3,4,5};
         String names [] = {"Peter","Pallar","William","Paul","James Bond"};

         for(int i=0;i<ids.length;i++){
              DB.query("INSERT INTO person values(' "+ids[i]+"', '"+names[i]+"')"); 
         }

         //statement.executeUpdate("UPDATE person SET name='Peter' WHERE id='1'");
         //statement.executeUpdate("DELETE FROM person WHERE id='1'");

<<<<<<< HEAD
           ResultSet resultSet = statement.executeQuery("SELECT * from person");
           
			//test attribute list
			ArrayList<Attribute> testList = new ArrayList<Attribute>();
   		
			Attribute temp = new Attribute();
			temp.name = "id";
			temp.tableName = "person";
			
			Attribute temp2 = new Attribute();
			temp2.name = "name";
			temp2.tableName = "person";
			
			testList.add(temp);
			testList.add(temp2);
	   		
=======
           ResultSet resultSet = DB.query("SELECT * from person");
>>>>>>> 5aab6b34ffb1a8074b4dd3f91a5d3840516f82dc
           while(resultSet.next())
           {
              // iterate & read the result set
              System.out.println("name = " + resultSet.getString("name"));
              System.out.println("id = " + resultSet.getInt("id"));
           }
           DB.close();
          }
      

     catch(SQLException e){  System.err.println(e.getMessage()); }       
/*      finally {         
            try {
                  if(connection != null)
                     connection.close();
                  }
            catch(SQLException e) {  // Use SQLException class instead.          
               System.err.println(e); 
             }
      }*/
  }
 }