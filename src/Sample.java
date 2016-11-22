import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
   
   public class Sample
    {
    public static void main(String[] args) throws ClassNotFoundException
     {
      // load the sqlite-JDBC driver using the current class loader
      Class.forName("org.sqlite.JDBC");

      //Connection connection = null;
      try
      {  
    	 Database DB = new Database("jdbc:sqlite:sample.db", "", "");

         DB.query("DROP TABLE IF EXISTS person");
         DB.query("CREATE TABLE person (id INTEGER, name STRING)");

         int ids [] = {1,2,3,4,5};
         String names [] = {"Peter","Pallar","William","Paul","James Bond"};

         //for(int i=0;i<ids.length;i++){
        	 DB.query("INSERT INTO person (id, name) VALUES ('1', 'Peter')");
        	 DB.query("INSERT INTO person (id, name) VALUES ('1', 'Joe')");
        	 DB.query("INSERT INTO person (id, name) VALUES ('1', 'Brian')");
        	 DB.query("INSERT INTO person (id, name) VALUES ('2', 'James')");
        	 DB.query("INSERT INTO person (id, name) VALUES ('3', 'Greg')");
        	 DB.query("INSERT INTO person (id, name) VALUES ('4', 'Paul')");
        	 DB.query("INSERT INTO person (id, name) VALUES ('4', 'Eric')");
              //DB.query("INSERT INTO person values(' "+ids[i]+"', '"+names[i]+"')"); 
         //}
           
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
	   		
        ResultSet resultSet = DB.query("SELECT * from person");
        while(resultSet.next())
        {
        	// iterate & read the result set
            System.out.println("name = " + resultSet.getString("name"));
            System.out.println("id = " + resultSet.getInt("id"));
        }
            DB.close();
      }
      

     catch(SQLException e){  System.err.println(e.getMessage()); }       
  }
 }