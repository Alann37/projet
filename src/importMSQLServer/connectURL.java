package importMSQLServer;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import Configuration.Configuration;
import ErrorLog.Error;
import traitement.Reponse;
import traitement.TraitementEntrer;
/**
 * 
 * @author dbinet
 *
 *classe faisant le liens avec la base de donnée 
 *permet la récuperation des informations
 *
 *API utilisée : JDBC
 */
public class ConnectURL {
	public ConnectURL(){
		decalage = 0;
	}
	private int decalage;
	/**
	 *fonction de récuperation des entête de colonne pour le fichier excel.
	 * @param database
	 * @param langue
	 * @param serveur
	 * @return
	 * @throws IOException
	 */
	public List<String> getColumnLabel(String database,String langue,String serveur) throws IOException{
		List<String> lRet = new ArrayList<String>();
		String connectionUrl = "jdbc:sqlserver://"+serveur+";"
				+"databaseName="+database;
		String user = Configuration.getConf(4);
		String passwd=Configuration.getConf(5);
		Connection con=null;
		Statement stmt = null;
		List<TableImport> tablesImport = new ArrayList<TableImport>();
		ResultSet rs = null;
		 try {
			 //connection a la base
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
	         boolean firstPassage= true;
	         for(int i = 0 ; i < tablesImport.size(); i++){
	            	query = "SELECT * FROM "+ tablesImport.get(i).name;
	            	stmt = con.createStatement();
	            	rs = stmt.executeQuery(query);
	          		for(int j = 1; j <= rs.getMetaData().getColumnCount();j++){
	          		
	          			if(firstPassage){
	          				lRet.add(rs.getMetaData().getColumnLabel(j));
	          				firstPassage= false;
	          			}
	          			if(j>1){
	          				lRet.add(rs.getMetaData().getColumnLabel(j));
	          			}
            		}
	            	
	            	
	            }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ErrorLog.Error m = new ErrorLog.Error (e.getMessage());
			m.printError();
		}          
		
		return lRet;
	}
	
	private List<TraitementEntrer> doQuery(Statement stmt, ResultSet rs,Connection con,String query,List<TraitementEntrer> list,int passage) throws ParseException, IOException{
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
				  	 

	        	   for(; i <= rs.getMetaData().getColumnCount(); i++){
	        		   int type = -1;
	        		   if(rs.getMetaData().getColumnTypeName(i).contains("int")){
	        			   type = 1;
	        		   }else {
	        			   type = 2;
	        		   }
	        	
	        		   if(rs.getString(i)==null){
	        			 //  System.out.println("passage pour la colonne "+rs.getMetaData().getColumnLabel(i) + " type = " + rs.getMetaData().getColumnType(i));
	        		   }else {
	        			   int columnCount = ((passage*201) + i)-(1+passage+decalage);
	        			   //System.out.println("valeur de i = "+ i + "valeur de column = " + columnCount + " et columnLabel = " + rs.getMetaData().getColumnLabel(i));

	        			   //System.out.println("column = " + columnCount);
	        			   list.get(rs.getRow()-1).getReponses().add(new Reponse(rs.getString(i),type,rs.getMetaData().getColumnLabel(i),columnCount));
	        		   }
	        		
        		   }
	        	 
	           }
			  if(rs.getMetaData().getColumnCount() != 201){
				  decalage = decalage + ( 201 -rs.getMetaData().getColumnCount() );
			  }
			  
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			ErrorLog.Error m = new ErrorLog.Error (e.getMessage());
			m.printError();
		}
          
         return list;

        }
	
	public  List<TraitementEntrer>  importDatabase(String database,String langue,String serveur) throws IOException{
		String connectionUrl = "jdbc:sqlserver://"+serveur+";"
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
            	listTraitement=doQuery(stmt, rs, con, query,listTraitement,i);
            }
           
         }
         catch (Exception e) {
        	 ErrorLog.Error m = new ErrorLog.Error (e.getMessage());
 			m.printError();
         }
        
         finally {
            if (rs != null) try { rs.close(); } catch(Exception e) {Error.printError(e.getMessage());}
            if (stmt != null) try { stmt.close(); } catch(Exception e) {Error.printError(e.getMessage());}
            if (con != null) try { con.close(); } catch(Exception e) {Error.printError(e.getMessage());}
           
         }
       /* for(int i = 0 ; i < listTraitement.get(1).getReponses().size();i++){
        	System.out.println(listTraitement.get(1).getReponses().get(i).getColumnPosition() + " et question = "+listTraitement.get(1).getReponses().get(i).getQuestionTag() );
        }*/
        return listTraitement;
	}
}
