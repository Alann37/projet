package importMSQLServer;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import Configuration.Configuration;
import importExcel.Reponse;
import importExcel.TraitementEntrer;

public class ConnectURL {
	public ConnectURL(){
		
	}
	private List<TraitementEntrer> test2(Statement stmt, ResultSet rs,Connection con,String query,List<TraitementEntrer> list) throws ParseException{
		boolean firstPassage= false;
		if(list.size()==0){
			firstPassage = true;
		}
           try {
			stmt = con.createStatement();
			  rs = stmt.executeQuery(query);
	
			  int i=0;
			  while(rs.next()){
				  	 if(firstPassage){
				  		 list.add(new TraitementEntrer());
				  		 i=1;
				  	 }else {
				  		 i=2;
				  	 }
				  	 

	        	   for(int j; i <= rs.getMetaData().getColumnCount(); i++){
	        		   //System.out.println(rs.getString(i));
	        		   int type = -1;
	        		   if(rs.getMetaData().getColumnTypeName(i).contains("int")){
	        			   type = 1;
	        		   }else {
	        			   type = 2;
	        		   }
	        		   list.get(rs.getRow()-1).getReponses().add(new Reponse(rs.getString(i),type,rs.getMetaData().getColumnLabel(i)));
	        		//   System.out.println( " test " + rs.getRow() + " nbRow  " + rs.getMetaData().getColumnCount());
	        		   
	        	   	}
	        	   
	           }

			//System.out.println(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

         return list;

        }
	
	public  List<TraitementEntrer>  test(String database,String langue) throws IOException{
		String connectionUrl = "jdbc:sqlserver://91.232.41.147;"
					+"databaseName="+database;
		String user = Configuration.getConf(4);
		String passwd=Configuration.getConf(5);
		Connection con=null;
		List<TableImport> tablesImport = new ArrayList<TableImport>();
		Statement stmt = null;
		List<TraitementEntrer> listTraitement = new ArrayList<TraitementEntrer>();
        ResultSet rs = null;
        try {

            con = DriverManager.getConnection(connectionUrl,user,passwd);            
            DatabaseMetaData dbm = con.getMetaData();
            ResultSet tables = dbm.getTables(null, null,langue+"_data%",null);
            //récupération des tables
            while (tables.next()) {
            	tablesImport.add(new TableImport(tables.getString(3)));
            }
            // tri des tables 
            TableImport temp ;
            for(int i = 0; i < tablesImport.size(); i ++){
            	for(int j = i ; j < tablesImport.size();j++){
            		if(tablesImport.get(i).number>tablesImport.get(j).number){
            			temp = tablesImport.get(i);
            			tablesImport.set(i, tablesImport.get(j));
            			tablesImport.set(j, temp);
            		}
            	}
            }
            String query = "";
            
            for(int i = 0 ; i < tablesImport.size(); i++){
            	query = "SELECT * FROM "+ tablesImport.get(i).name;
            	listTraitement=test2(stmt, rs, con, query,listTraitement);
            	//System.out.println("nbr rqt + " + tablesImport.size() + " et i = " +i);
            }
           
         }
         catch (Exception e) {
            e.printStackTrace();
         }
        
         finally {
            if (rs != null) try { rs.close(); } catch(Exception e) {}
            if (stmt != null) try { stmt.close(); } catch(Exception e) {}
            if (con != null) try { con.close(); } catch(Exception e) {}
           
         }

        return listTraitement;
	}
}
