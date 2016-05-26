package importMSQLServer;

import java.sql.*;
import java.util.Properties;

public class connectURL {
	
	public static void test(){
		String connectionUrl = "jdbc:sqlserver://91.232.41.147;"
					+"databaseName=ETestTraitementProjet";
		String user = "alouischevrau";
		String passwd="alo922";
		Connection con=null;

		Statement stmt = null;
		
        ResultSet rs = null;

        try {
            // Establish the connection.
            Properties p = new Properties();
            con = DriverManager.getConnection(connectionUrl,user,passwd);            
            // Create and execute an SQL statement that returns some data.
            DatabaseMetaData dbm = con.getMetaData();
            ResultSet tables = dbm.getTables(null, null,"FR_%",null);
            
            int toto ;
            
            // Iterate through the data in the result set and display it.
            /*while (tables.next()) {
            	toto=0;
              // System.out.println(tables.getString(2) + " " + tables.getString(3) + " "+ tables.getString(4));
              */ String SQL = "SELECT * FROM dbo.FR_data1";
               stmt = con.createStatement();
               toto=0;
               rs = stmt.executeQuery(SQL);
               while(rs.next()){
            	   toto++;
            	   System.out.println("row numero : " + rs.getRow());
            	   for(int i = 1; i < rs.getMetaData().getColumnCount(); i++){
            		   System.out.println(rs.getString(i));
            	   }
               }
            //   System.out.println(toto);
           // }
         }

         // Handle any errors that may have occurred.
         catch (Exception e) {
            e.printStackTrace();
         }
         finally {
            if (rs != null) try { rs.close(); } catch(Exception e) {}
            if (stmt != null) try { stmt.close(); } catch(Exception e) {}
            if (con != null) try { con.close(); } catch(Exception e) {}
         }
	}
}
