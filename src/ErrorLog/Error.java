package ErrorLog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * 
 * @author dbinet
 *
 *classe permettant l'export des messages d'erreurs dans un fichier .txt 
 */
public class Error {
	String msg;
	
	public Error(String m ){
		msg = m;
	}
	public void printError() throws IOException{
		FileWriter file = new FileWriter("errorLog.txt",true);
		BufferedWriter writer = new BufferedWriter(file);
		PrintWriter out = new PrintWriter(writer,false);
	   DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	   //get current date time with Date()
	   Date date = new Date();
	   System.out.println(dateFormat.format(date));

	   //get current date time with Calendar()
	   Calendar cal = Calendar.getInstance();
		out.println(dateFormat.format(cal.getTime()) + "   " + msg + "\n");
		
		writer.close();
	}
	public static void printError(String m) throws IOException{
		FileWriter file = new FileWriter("errorLog.txt",true);
		BufferedWriter writer = new BufferedWriter(file);
		PrintWriter out = new PrintWriter(writer,false);
		  DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		   //get current date time with Date()
		   Date date = new Date();
		   System.out.println(dateFormat.format(date));

		   //get current date time with Calendar()
		   Calendar cal = Calendar.getInstance();
		out.println(dateFormat.format(cal.getTime()) + "   " + m + "\n");
		
		writer.close();
	}
}
