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

public class Error {
	String msg;
	
	public Error(String m ){
		msg = m;
	}
	public void printError() throws IOException{
		FileWriter file = new FileWriter("debug.txt",true);
		BufferedWriter writer = new BufferedWriter(file);
		PrintWriter out = new PrintWriter(writer,false);
		out.println(msg + "\n");
		
		writer.close();
	}
}
